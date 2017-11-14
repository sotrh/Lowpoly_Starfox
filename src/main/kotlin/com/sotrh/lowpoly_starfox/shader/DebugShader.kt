package com.sotrh.lowpoly_starfox.shader

import com.sotrh.lowpoly_starfox.camera.Camera
import com.sotrh.lowpoly_starfox.display.Display
import com.sotrh.lowpoly_starfox.texture.Texture
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20

class DebugShader: Shader(VS_CODE, FS_CODE) {
    companion object {
        val VS_CODE = """
            #version 150 core

            in vec3 position;
            in vec3 normal;
            in vec2 texCoord;

            out vec3 pass_color;
            out vec2 pass_textureCoords;

            uniform mat4 mvp;

            void main(void) {
                pass_color = vec3(normal.x + 0.5, normal.y + 0.5, normal.z + 0.5);
                pass_textureCoords = texCoord;
                gl_Position = mvp * vec4(position, 1.0);
            }
            """

        val FS_CODE = """
            #version 150 core

            in vec3 pass_color;
            in vec2 pass_textureCoords;

            out vec4 out_Color;

            uniform sampler2D textureSampler;

            void main(void) {
                out_Color = texture(textureSampler, pass_textureCoords);
            }
            """
    }

    private val mvp = Matrix4f()
    private val view = Matrix4f()
    private val projection = Matrix4f()
    private val arrayMvp = FloatArray(16)
    private val uniformMvp = GL20.glGetUniformLocation(programId, "mvp")

    override fun bindAttributes() {
        bindAttribute(0, "position")
        bindAttribute(1, "normal")
        bindAttribute(2, "texCoord")
    }

    fun applyTransform(camera: Camera, display: Display) {
        mvp.identity()
        camera.applyCameraTransformation(view)
        display.applyDisplayTransformation(projection)

        projection.mul(view, mvp)

        GL20.glUniformMatrix4fv(uniformMvp, false, mvp.get(arrayMvp))
    }

    fun applyTransform(camera: Camera, display: Display, modelMatrix: Matrix4f) {
        mvp.identity()
        camera.applyCameraTransformation(view)
        display.applyDisplayTransformation(projection)

        projection.mul(view.mul(modelMatrix, mvp), mvp)

        GL20.glUniformMatrix4fv(uniformMvp, false, mvp.get(arrayMvp))
    }

    fun bindTexture(texture: Texture, activeTextureSlot: Int = 0) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + activeTextureSlot)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.textureId)
    }
}