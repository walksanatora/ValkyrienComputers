package net.techtastic.vc.ship

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.impl.api.ServerShipUser
import org.valkyrienskies.core.impl.api.Ticked
import org.valkyrienskies.mod.api.SeatedControllingPlayer
import java.util.UUID

class ApplyThrust : ServerShipUser, Ticked {
    override var ship : ServerShip? = null

    var directionMap : MutableMap<UUID, String> = mutableMapOf()
    var tickMap : MutableMap<UUID, Int> = mutableMapOf()
    var worldMap : MutableMap<UUID, Level> = mutableMapOf()
    var posMap : MutableMap<UUID, BlockPos> = mutableMapOf()

    override fun tick() {
        if (tickMap.isNotEmpty() && directionMap.isNotEmpty() && worldMap.isNotEmpty() && posMap.isNotEmpty()) {
            for (key in tickMap.keys) {
                applyThrust(directionMap[key], tickMap[key], worldMap[key], posMap[key], key)
            }
        }
    }

    fun applyThrust(direction: String?, gameTicks: Int?, world: Level?, pos: BlockPos?, key: UUID) {
        if (direction == null || gameTicks == null || world == null || pos == null) return

        var fakePlayer = ship!!.getAttachment(SeatedControllingPlayer::class.java)
        if (fakePlayer == null) { //Is there a SeatedControllingPlayer already?
            fakePlayer = SeatedControllingPlayer(world.getBlockState(pos).getValue<Direction>(BlockStateProperties.HORIZONTAL_FACING).getOpposite())
            ship!!.saveAttachment(SeatedControllingPlayer::class.java, fakePlayer)
        }
        var originTime: Long = world.getGameTime()
        var ticks = 0
        while (ticks < gameTicks) {
            //If one tick of time has passed, set originTime to current and increment ticks
            if (world.getGameTime() - originTime == 1L) {
                originTime = world.getGameTime()
                ticks++
            }
            when (direction) {
                "forward" -> fakePlayer.forwardImpulse = 1.0f
                "left" -> fakePlayer.leftImpulse = 1.0f
                "right" -> fakePlayer.leftImpulse = -1.0f
                "back" -> fakePlayer.forwardImpulse = -1.0f
                "up" -> fakePlayer.upImpulse = 1.0f
                else -> fakePlayer.upImpulse = -1.0f
            }
            ship!!.saveAttachment(SeatedControllingPlayer::class.java, fakePlayer)
        }
        when (direction) {
            "forward", "back" -> fakePlayer.forwardImpulse = 0.0f
            "left", "right" -> fakePlayer.leftImpulse = 0.0f
            else -> fakePlayer.upImpulse = 0.0f
        }
        ship!!.saveAttachment(SeatedControllingPlayer::class.java, fakePlayer)
        reset(key)
    }

    fun reset(key: UUID) {
        tickMap.remove(key)
        directionMap.remove(key)
        worldMap.remove(key)
        posMap.remove(key)

    }

    fun set(world: Level, pos: BlockPos, direction: String, tick: Int) {
        val key: UUID = UUID.randomUUID()

        worldMap.remove(key, world)
        posMap.remove(key, pos)
        directionMap.put(key, direction)
        tickMap.remove(key, tick)
    }
}