package net.techtastic.vc.ship

import net.minecraft.core.BlockPos
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.impl.api.ShipForcesInducer
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.mod.common.util.toJOMLD
import java.util.concurrent.ConcurrentHashMap

class MotorBaseControl : ShipForcesInducer{
    val motorsMap = ConcurrentHashMap<BlockPos, MotorControlData>()

    override fun applyForces(physShip: PhysShip) {
        for (data in motorsMap.elements()) {
            val curControlData = data ?: continue

            if (curControlData.activated) {
                curControlData.computeTorqueBody1(physShip as PhysShipImpl)
            }
        }
    }
}