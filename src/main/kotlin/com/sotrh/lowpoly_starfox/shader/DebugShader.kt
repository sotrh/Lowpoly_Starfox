package com.sotrh.lowpoly_starfox.shader

import com.sotrh.lowpoly_starfox.camera.Camera
import com.sotrh.lowpoly_starfox.display.Display
import org.joml.Matrix4f
import org.lwjgl.opengl.GL20

class DebugShader: Shader(VS_CODE, FS_CODE) {
    companion object {
        val VS_CODE = """
            #version 150 core
            in vec3 position;
            out vec3 color;
            uniform mat4 mvp;
            void main(void) {
                gl_Position = /*mvp * */vec4(position, 1.0);
                color = vec3(position.x + 0.5, 1.0, position.y + 0.5);
            }
            """

        val FS_CODE = """
            #version 150 core
            in vec3 color;
            out vec4 out_Color;
            void main(void) {
                out_Color = vec4(color, 1.0);
            }
            """
    }

    private val mvp = Matrix4f()
    private val arrayMvp = FloatArray(16)
    private val uniformMvp = GL20.glGetUniformLocation(programId, "mvp")
    override fun bindAttributes() {
        bindAttribute(0, "position")
    }

    fun applyTransform(camera: Camera, display: Display) {
        mvp.identity()
        camera.applyCameraTransformation(mvp)
        display.applyDisplayTransformation(mvp)

        mvp.get(arrayMvp)

        GL20.glUniform4fv(uniformMvp, arrayMvp)
    }
}