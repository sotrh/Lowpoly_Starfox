package com.sotrh.lowpoly_starfox.shader

import com.sotrh.lowpoly_starfox.camera.Camera
import com.sotrh.lowpoly_starfox.display.Display
import com.sotrh.lowpoly_starfox.light.Light
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

            out vec3 pass_toCameraVector;
            out vec3 pass_toLightVector;
            out vec3 pass_surfaceNormal;
            out vec2 pass_textureCoords;

            uniform mat4 mvp;
            uniform mat4 model;
            uniform mat4 invView;
            uniform vec3 lightPosition;

            void main(void) {
                vec4 worldPosition = model * vec4(position, 1.0);

                pass_toLightVector = lightPosition - worldPosition.xyz;
                pass_surfaceNormal = (model * vec4(normal, 0.0)).xyz;
                pass_toCameraVector = (invView * vec4(0.0, 0.0, 0.0, 1.0)).xyz;

                pass_textureCoords = texCoord;

                gl_Position = mvp * vec4(position, 1.0);
            }
            """

        val FS_CODE = """
            #version 150 core

            in vec3 pass_toCameraVector;
            in vec3 pass_toLightVector;
            in vec3 pass_surfaceNormal;
            in vec2 pass_textureCoords;

            out vec4 out_color;

            uniform sampler2D textureSampler;
            uniform vec3 lightColor;
            uniform float shineDamper;
            uniform float reflectivity;

            void main(void) {
                vec3 unitSurfaceNormal = normalize(pass_surfaceNormal);
                vec3 unitToLightVector = normalize(pass_toLightVector);
                vec3 unitToCameraVector = normalize(pass_toCameraVector);

                float brightness = max(dot(unitSurfaceNormal, unitToLightVector), 0.0);
                vec3 diffuse = brightness * lightColor;

                vec3 lightDirection = -unitToLightVector;
                vec3 reflectedLightDirection = reflect(lightDirection, unitSurfaceNormal);

                float specularFactor = max(dot(reflectedLightDirection, unitToCameraVector), 0.0);
                float dampedFactor = pow(specularFactor, shineDamper);
                vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

                out_color = vec4(finalSpecular, 1.0) + vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords);
            }
            """
    }

    private val transform = Matrix4f()
    private val view = Matrix4f()
    private val projection = Matrix4f()

    private val mvpUniform = Uniform(programId, "mvp")
    private val modelUniform = Uniform(programId, "model")
    private val invViewUniform = Uniform(programId, "invView")

    private val lightColorUniform = Uniform(programId, "lightColor")
    private val lightPositionUniform = Uniform(programId, "lightPosition")
    private val shineDamperUniform = Uniform(programId, "shineDamper")
    private val reflectivityUniform = Uniform(programId, "reflectivity")

    override fun bindAttributes() {
        bindAttribute(0, "position")
        bindAttribute(1, "normal")
        bindAttribute(2, "texCoord")
    }

    fun applyTransform(camera: Camera, display: Display, modelMatrix: Matrix4f) {
        transform.identity()
        camera.applyCameraTransformation(view)
        display.applyDisplayTransformation(projection)

        projection.mul(view.mul(modelMatrix, transform), transform)

        mvpUniform.bindMatrix4f(transform)
        modelUniform.bindMatrix4f(modelMatrix)

        transform.set(view).invert()
        invViewUniform.bindMatrix4f(transform)
    }

    fun bindTexture(texture: Texture, activeTextureSlot: Int = 0) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + activeTextureSlot)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.textureId)
    }

    fun setLightUniforms(light: Light) {
        lightColorUniform.bindVector3f(light.color)
        lightPositionUniform.bindVector3f(light.position)
    }

    fun setReflectivityAndLightDamper(reflectivity: Float, shineDamper: Float) {
        reflectivityUniform.bindScalar(reflectivity)
        shineDamperUniform.bindScalar(shineDamper)
    }
}