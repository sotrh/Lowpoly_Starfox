package com.sotrh.lowpoly_starfox.camera

import org.joml.Matrix4f
import org.joml.Vector3f

/**
 * Created by benjamin on 11/2/17
 */
class Camera {
    val position: Vector3f = Vector3f()
    var pitch: Float = 0.0f
    var yaw: Float = 0.0f

    fun applyCameraTransformation(matrix: Matrix4f) {
        matrix.identity().rotateX(pitch).rotateY(yaw).translate(-position.x, -position.y, -position.z)
    }
}