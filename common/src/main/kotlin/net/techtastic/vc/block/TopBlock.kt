package net.techtastic.vc.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.techtastic.vc.blockentity.MotorBlockEntity
import net.techtastic.vc.util.DirectionalShape
import net.techtastic.vc.util.RotShapes
import org.joml.Vector3d
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.transformToNearbyShipsAndWorld

class TopBlock(properties: Properties) : Block(properties) {
    val TOP = RotShapes.or(
            RotShapes.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0)
    )

    val TOP_SHAPE = DirectionalShape.up(TOP)

    init {
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.FACING)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return defaultBlockState()
                .setValue(BlockStateProperties.FACING, ctx.clickedFace)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return TOP_SHAPE[state.getValue(BlockStateProperties.FACING)]
    }

    override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
        super.onRemove(state, level, pos, newState, isMoving)
        if (!level.isClientSide()) {
            val level = level as ServerLevel

            var bePos = pos.relative(state.getValue(BlockStateProperties.FACING))
            val initialShip = level.getShipManagingPos(bePos)
            if (initialShip != null) {
                val vec = initialShip.transform.shipToWorld.transformPosition(bePos.x.toDouble(), bePos.y.toDouble(), bePos.z.toDouble(), Vector3d())
                bePos = BlockPos(vec.x, vec.y, vec.z)
            }

            val potentialShips = level.transformToNearbyShipsAndWorld(bePos.x.toDouble(), bePos.y.toDouble(), bePos.z.toDouble(), 0.5)
            if (potentialShips.isNotEmpty()) {
                val shipVec = potentialShips.first()
                val ship = level.getShipManagingPos(shipVec.x, shipVec.y, shipVec.z)
                val vec = ship?.transform?.worldToShip?.transformPosition(bePos.x.toDouble(), bePos.y.toDouble(), bePos.z.toDouble(), Vector3d())
                if (vec != null) {
                    bePos = BlockPos(vec.x, vec.y, vec.z)
                }
            }

            val be = level.getBlockEntity(bePos)
            if (be is MotorBlockEntity) {
                be.destroyConstraints()
            }
        }
    }
}