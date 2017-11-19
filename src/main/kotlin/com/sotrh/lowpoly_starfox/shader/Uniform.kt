package com.sotrh.lowpoly_starfox.shader

import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL20

class Uniform(programId: Int, name: String) {
    companion object {
        val MATRIX_ARRAY = FloatArray(16)
    }

    val location = GL20.glGetUniformLocation(programId, name)

    fun bindScalar(value: Int) {
        GL20.glUniform1i(location, value)
    }

    fun bindScalar(value: Float) {
        GL20.glUniform1f(location, value)
    }

    fun bindVector3f(value: Vector3f) {
        bindVector3f(value.x, value.y, value.z)
    }

    fun bindVector3f(x: Float, y: Float, z: Float) {
        GL20.glUniform3f(location, x, y, z)
    }

    fun bindMatrix4f(value: Matrix4f, transpose: Boolean = false) {
        GL20.glUniformMatrix4fv(location, transpose, value.get(MATRIX_ARRAY))
    }
}