package net.techtastic.vc.util

import org.joml.Quaterniondc
import org.joml.Vector3dc

class SpecialLuaTables {

    companion object {
        @JvmStatic
        fun getVectorAsTable(x: Double, y: Double, z: Double): HashMap<Any, Any> {
            val table : HashMap<Any, Any> = HashMap()

            table["x"] = x
            table["y"] = y
            table["z"] = z

            return table
        }

        @JvmStatic
        fun getVectorAsTable(vec: Vector3dc): HashMap<Any, Any> {
            val table : HashMap<Any, Any> = HashMap()

            table["x"] = vec.x()
            table["y"] = vec.y()
            table["z"] = vec.z()

            return table
        }

        @JvmStatic
        fun getQuaternionAsTable(rot : Quaterniondc): HashMap<Any, Any> {
            val table : HashMap<Any, Any> = HashMap()

            table["x"] = rot.x()
            table["y"] = rot.y()
            table["z"] = rot.z()
            table["w"] = rot.w()

            return table
        }

        @JvmStatic
        fun getObjectAsArrayList(obj : Any) : ArrayList<Any> {
            val list : ArrayList<Any> = ArrayList()
            list.add(obj)
            return list
        }
    }
}