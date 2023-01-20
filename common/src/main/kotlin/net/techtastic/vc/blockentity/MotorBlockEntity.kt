package net.techtastic.vc.blockentity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.HitResult
import net.techtastic.vc.integrations.cc.ComputerCraftBlockEntities
import net.techtastic.vc.integrations.cc.ComputerCraftBlocks
import org.joml.AxisAngle4d
import org.joml.Quaterniond
import org.joml.Vector3d
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.apigame.constraints.*
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.common.*
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.mod.common.util.toJOMLD
import org.valkyrienskies.mod.common.util.toMinecraft
import org.valkyrienskies.mod.common.world.clipIncludeShips
import kotlin.math.roundToInt

class MotorBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ComputerCraftBlockEntities.MOTOR.get(), pos, state) {
    var motorId : VSConstraintId? = null
    var hingeId : VSConstraintId? = null
    var attachmentConstraintId : VSConstraintId? = null
    var otherPos : BlockPos? = null
    var currentAngle : Double = 0.0
    var shipIds : List<Long>? = null
    var activated = false
    var reversed = false

    override fun load(tag: CompoundTag) {
        super.load(tag)

        currentAngle = tag.getDouble("angle")
        shipIds = tag.getLongArray("shipIds").asList()

        if (tag.contains("otherPos")) {
            otherPos = BlockPos.of(tag.getLong("otherPos"))

            VSEvents.shipLoadEvent.on { (otherShip), handler ->
                if (otherShip.chunkClaim.contains(otherPos!!.x / 16, otherPos!!.z / 16)) {
                    handler.unregister()

                    if (!createConstraints()) {
                        VSEvents.shipLoadEvent.on { (ship), handler ->
                            if (ship.chunkClaim.contains(blockPos.x / 16, blockPos.z / 16)) {
                                handler.unregister()

                                if (!createConstraints(otherShip)) throw IllegalStateException("Could not create constraints for bearing block entity!")
                            }
                        }
                    }
                }
            }
        }

        activated = tag.getBoolean("activated")
        reversed = tag.getBoolean("reversed")
    }

    override fun saveAdditional(tag: CompoundTag) {
        tag.putDouble("angle", currentAngle)
        shipIds?.let { tag.putLongArray("shipIds", it) }

        if (otherPos != null) {
            tag.putLong("otherPos", otherPos!!.asLong())
        }

        tag.putBoolean("activated", activated)
        tag.putBoolean("reversed", reversed)

        super.saveAdditional(tag)
    }

    override fun setRemoved() {
        super.setRemoved()
        destroyConstraints()
    }

    override fun clearRemoved() {
        super.clearRemoved()
        createConstraints()
    }

    fun <T : BlockEntity?> tick(level: Level?, pos: BlockPos?, state: BlockState?, be: T) {
        if (level?.shipObjectWorld != null && !level.isClientSide) {
            System.err.println("ShipObjectWorld not null and Level not Clientside")
            val level = level as ServerLevel

            if (motorId != null && shipIds != null) {
                val targetShips = level.transformToNearbyShipsAndWorld(otherPos?.x!!.toDouble() + 0.5, otherPos?.y!!.toDouble() + 0.5, otherPos?.z!!.toDouble() + 0.5, 1.0)
                if (targetShips.isEmpty()) {
                    System.err.println("TargetShips is empty")
                    return
                }
                val shipId = shipIds!![0]
                val otherShipId = shipIds!![1]
                val lookingTowards = blockState.getValue(BlockStateProperties.FACING).normal.toJOMLD()
                val x = Vector3d(1.0, 0.0, 0.0)
                val xCross = Vector3d(lookingTowards).cross(x)
                val hingeOrientation = if (xCross.lengthSquared() < 1e-6)
                    Quaterniond()
                else
                    Quaterniond(AxisAngle4d(lookingTowards.angle(x), xCross.normalize()))

                System.err.println("Current Angle: $currentAngle")

                val otherShip = level.shipObjectWorld.loadedShips.getById(otherShipId)
                if (otherShip != null && activated) {

                    if (reversed) {
                        currentAngle = if (currentAngle > 0.0) {
                            currentAngle - 0.0174533
                        } else {
                            2 * Math.PI
                        }
                    } else {
                        currentAngle = if (currentAngle < 2 * Math.PI) {
                            currentAngle + 0.0174533
                        } else {
                            0.0
                        }
                    }

                    this.setChanged()

                    val motorConstraint = VSHingeTargetAngleConstraint(
                            shipId, otherShipId, constraintComplience, hingeOrientation, hingeOrientation, maxForce, currentAngle, currentAngle
                    )

                    motorId?.let { level.shipObjectWorld.removeConstraint(it) }
                    motorId = motorConstraint.let { level.shipObjectWorld.createNewConstraint(it) }
                }
            }
        }
    }

    fun makeOrGetTop(level: ServerLevel, pos: BlockPos) {
        val lookingTowards = blockState.getValue(BlockStateProperties.FACING).normal.toJOMLD()

        val ship = level.getShipObjectManagingPos(blockPos)

        val clipResult = level.clipIncludeShips(
                ClipContext(
                        (Vector3d(basePoint()).let {
                            ship?.shipToWorld?.transformPosition(it) ?: it
                        }).toMinecraft(),
                        (blockPos.toJOMLD()
                                .add(0.5, 0.5,0.5)
                                .add(Vector3d(lookingTowards).mul(0.8))
                                .let {
                                    ship?.shipToWorld?.transformPosition(it) ?: it
                                }).toMinecraft(),
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        null), false)

        val (otherAttachmentPoint, otherShip) = if (clipResult.type == HitResult.Type.MISS) {
            val inWorldPos = blockPos.toJOMLD().let {
                ship?.shipToWorld?.transformPosition(it) ?: it
            }
            val inWorldBlockPos = BlockPos(inWorldPos.x, inWorldPos.y, inWorldPos.z)

            val otherShip = level.shipObjectWorld.createNewShipAtBlock(
                    inWorldBlockPos.offset(blockState.getValue(BlockStateProperties.FACING).normal).toJOML(),
                    false,
                    ship?.transform?.shipToWorldScaling?.x() ?: 1.0,
                    level.dimensionId
            )

            val shipCenterPos = BlockPos(
                    (otherShip.transform.positionInShip.x() - 0.5).roundToInt(),
                    (otherShip.transform.positionInShip.y() - 0.5).roundToInt(),
                    (otherShip.transform.positionInShip.z() - 0.5).roundToInt()
            )

            val towards = blockState.getValue(BlockStateProperties.FACING)
            val topPos = shipCenterPos.offset(towards.normal)

            level.setBlock(topPos, ComputerCraftBlocks.MOTOR_TOP.get().defaultBlockState()
                    .setValue(BlockStateProperties.FACING, towards), 11)

            topPos to otherShip
        } else {
            level.getShipObjectManagingPos(clipResult.blockPos)?.let { otherShip ->
                val otherPos = clipResult.blockPos.offset(blockState.getValue(BlockStateProperties.FACING).opposite.normal)

                level.setBlock(
                        otherPos, ComputerCraftBlocks.MOTOR_TOP.get().defaultBlockState().setValue(
                        BlockStateProperties.FACING, blockState.getValue(BlockStateProperties.FACING).opposite
                ), 11
                )

                otherPos to otherShip
            } ?: (clipResult.blockPos to null)
        }

        this.otherPos = otherAttachmentPoint
        createConstraints(otherShip)
    }

    fun createConstraints(otherShip: ServerShip? = null): Boolean {
        if (otherPos != null && level != null
                && motorId == null
                && !level!!.isClientSide) {
            val level = level as ServerLevel
            val shipId = level.getShipObjectManagingPos(blockPos)?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!
            val otherShipId = otherShip?.id ?: level.getShipObjectManagingPos(otherPos!!)?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!

            // If both ships aren't loaded don't make the constraint (itl crash vs2)
            if (level.getShipManagingPos(blockPos) != null && level.getShipManagingPos(blockPos)!!.id != shipId) return false
            if (level.getShipManagingPos(otherPos!!) != null && level.getShipManagingPos(otherPos!!)!!.id != otherShipId) return false

            // Orientation
            val lookingTowards = blockState.getValue(BlockStateProperties.FACING).normal.toJOMLD()
            val x = Vector3d(1.0, 0.0, 0.0)
            val xCross = Vector3d(lookingTowards).cross(x)
            val hingeOrientation = if (xCross.lengthSquared() < 1e-6)
                Quaterniond()
            else
                Quaterniond(AxisAngle4d(lookingTowards.angle(x), xCross.normalize()))

            val motorConstraint = otherShip?.transform?.let {
                VSHingeTargetAngleConstraint(
                        shipId, otherShipId, constraintComplience, hingeOrientation, hingeOrientation, maxForce, currentAngle, currentAngle
                )
            }

            val hingeConstraint = VSHingeOrientationConstraint(
                    shipId, otherShipId, constraintComplience, hingeOrientation, hingeOrientation, maxForce
            )

            val attachmentConstraint = VSAttachmentConstraint(
                    shipId, otherShipId, constraintComplience, basePoint(), otherPoint()!!,
                    maxForce, baseOffset
            )

            motorId = motorConstraint?.let { level.shipObjectWorld.createNewConstraint(it) }
            shipIds = listOf(shipId, otherShipId)
            this.setChanged()
            hingeId = level.shipObjectWorld.createNewConstraint(hingeConstraint)
            attachmentConstraintId = level.shipObjectWorld.createNewConstraint(attachmentConstraint)
            return true
        }

        return false
    }

    fun destroyConstraints() {
        if (level?.shipObjectWorld != null && !level!!.isClientSide) {
            val level = level as ServerLevel
            motorId?.let { level.shipObjectWorld.removeConstraint(it) }
            shipIds = null
            this.setChanged()
            hingeId?.let { level.shipObjectWorld.removeConstraint(it) }
            attachmentConstraintId?.let { level.shipObjectWorld.removeConstraint(it) }
        }
    }

    private fun basePoint(): Vector3d = blockPos.toJOMLD()
            .add(0.5, 0.5,0.5)
            .add(blockState.getValue(BlockStateProperties.FACING).normal.toJOMLD().mul(0.5 + baseOffset))

    private fun otherPoint(): Vector3d? = otherPos?.toJOMLD()
            ?.add(0.5, 0.5, 0.5)
            ?.add(blockState.getValue(BlockStateProperties.FACING).normal.toJOMLD().mul(0.5))

    companion object {
        private val baseOffset = 0.5
        private val constraintComplience = 1e-10
        private val maxForce = 1e10
    }
}