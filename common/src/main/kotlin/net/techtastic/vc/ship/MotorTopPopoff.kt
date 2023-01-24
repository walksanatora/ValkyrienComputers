package net.techtastic.vc.ship

import org.joml.Vector3dc
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.impl.api.ShipForcesInducer

class MotorTopPopoff : ShipForcesInducer {
    var forceVec : Vector3dc? = null

    override fun applyForces(physShip: PhysShip) {
        if (forceVec != null) {
            physShip.applyInvariantForce(forceVec!!)
            forceVec = null
        }
    }
}