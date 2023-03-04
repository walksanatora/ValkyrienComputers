package net.techtastic.vc.ship

import com.fasterxml.jackson.annotation.JsonIgnore
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.techtastic.vc.blockentity.MotorBlockEntity
import org.joml.Vector3d
import org.joml.Vector3dc
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.impl.api.ServerShipUser
import org.valkyrienskies.core.impl.api.ShipForcesInducer
import org.valkyrienskies.core.impl.api.Ticked
import org.valkyrienskies.core.impl.api.shipValue
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.mod.common.util.toJOMLD
import java.lang.Double.min

class MotorControl : ShipForcesInducer {
    var controlData : MotorControlData? = null

    override fun applyForces(physShip: PhysShip) {
        physShip as PhysShipImpl
        val curControlData = controlData ?: return

        if (curControlData.activated) {
            curControlData.computeTorqueBody0(physShip as PhysShipImpl)
        }
    }
}

data class MotorControlData(
    val activated : Boolean,
    val reversed : Boolean,
    val direction : Direction,
    val constantAngularVel : Double?,
    val constantAngle : Double?,
    val isBaseInWorld : Boolean
) {
    @JsonIgnore private var physShip0 : PhysShipImpl? = null
    @JsonIgnore private var physShip1 : PhysShipImpl? = null
    fun computeTorqueBody0(physShip0: PhysShipImpl) {
        this.physShip0 = physShip0
        if (!isBaseInWorld) {
            if (physShip1 != null) {
                computeTorquesTwoShips()
            }
        } else {
            computeTorquesOneShip()
        }
    }
    fun computeTorqueBody1(physShip1: PhysShipImpl) {
        this.physShip1 = physShip1
        if (physShip0 != null) {
            computeTorquesTwoShips()
        }
    }
    private fun computeTorquesTwoShips() {
        if (constantAngularVel != null) {
            val vec : Vector3dc = physShip0!!.poseVel.omega.sub(physShip1!!.poseVel.omega, Vector3d())
            val motorAxis : Vector3dc = physShip1!!.poseVel.rot.transform(direction.normal.toJOMLD())
            val angularVel = motorAxis.dot(vec)
            val angularVelDiff = constantAngularVel - angularVel
            val torque : Vector3dc = motorAxis.mul(Mth.clamp(angularVelDiff * 100000, -20000.0, 20000.0), Vector3d())
            physShip0!!.applyInvariantTorque(torque)
            physShip1!!.applyInvariantTorque(torque.mul(-1.0, Vector3d()))
        }

        physShip0 = null
        physShip1 = null
    }
    private fun computeTorquesOneShip() {
        if (constantAngularVel != null) {
            val vec : Vector3dc = physShip0!!.poseVel.omega
            val motorAxis : Vector3dc = direction.normal.toJOMLD()
            val angularVel = motorAxis.dot(vec)
            val angularVelDiff = constantAngularVel - angularVel
            val torque : Vector3dc = motorAxis.mul(Mth.clamp(angularVelDiff * 100000, -20000.0, 20000.0), Vector3d())
            physShip0!!.applyInvariantTorque(torque)
        }

        physShip0 = null
        physShip1 = null
    }
}