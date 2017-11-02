package com.sotrh.lowpoly_starfox.shader

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL32

abstract class Shader @JvmOverloads constructor(vsCode: String, fsCode: String, gsCode: String? = null) {
    val programId: Int

    private val vertexShader: Int
    private val fragmentShader: Int
    private val geometryShader: Int

    init {
        vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        GL20.glShaderSource(vertexShader, vsCode)
        GL20.glCompileShader(vertexShader)
        assertShaderCompiled(vertexShader, "VERTEX")

        fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        GL20.glShaderSource(fragmentShader, fsCode)
        GL20.glCompileShader(fragmentShader)
        assertShaderCompiled(fragmentShader, "FRAGMENT")

        if (gsCode != null) {
            geometryShader = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER)
            GL20.glShaderSource(geometryShader, gsCode)
            GL20.glCompileShader(geometryShader)
            assertShaderCompiled(geometryShader, "GEOMETRY")
        } else {
            geometryShader = 0
        }

        programId = GL20.glCreateProgram()
        GL20.glAttachShader(programId, vertexShader)
        GL20.glAttachShader(programId, fragmentShader)
        if (geometryShader != 0) GL20.glAttachShader(programId, geometryShader)
        bindAttributes()
        GL20.glLinkProgram(programId)
        GL20.glValidateProgram(programId)
        assertProgramLinked()
    }

    fun bind() {
        GL20.glUseProgram(programId)
    }

    fun unbind() {
        GL20.glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        GL20.glDetachShader(programId, vertexShader)
        GL20.glDetachShader(programId, fragmentShader)
        if (geometryShader != 0) GL20.glDetachShader(programId, geometryShader)

        GL20.glDeleteShader(vertexShader)
        GL20.glDeleteShader(fragmentShader)
        if (geometryShader != 0) GL20.glDeleteShader(geometryShader)

        GL20.glDeleteProgram(programId)
    }

    protected abstract fun bindAttributes()

    protected fun bindAttribute(attribute: Int, variableName: String) {
        GL20.glBindAttribLocation(programId, attribute, variableName)
    }

    private fun assertShaderCompiled(shaderId: Int, shaderName: String) {
        val status = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS)
        if (status != GL11.GL_TRUE)
            throw IllegalStateException("Failed to compile $shaderName:\n${GL20.glGetShaderInfoLog(shaderId)}")
    }

    private fun assertProgramLinked() {
        val status = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS)
        if (status != GL11.GL_TRUE)
            throw IllegalStateException("Failed to link program:\n${GL20.glGetProgramInfoLog(programId)}")
    }
}