package com.sotrh.lowpoly_starfox

import com.sotrh.lowpoly_starfox.camera.Camera
import com.sotrh.lowpoly_starfox.camera.DebugCameraController
import com.sotrh.lowpoly_starfox.common.PIf
import com.sotrh.lowpoly_starfox.display.DisplayManager
import com.sotrh.lowpoly_starfox.entity.Entity
import com.sotrh.lowpoly_starfox.entity.EntityManager
import com.sotrh.lowpoly_starfox.input.InputManager
import com.sotrh.lowpoly_starfox.light.Light
import com.sotrh.lowpoly_starfox.model.ModelLoader
import com.sotrh.lowpoly_starfox.model.ModelRenderer
import com.sotrh.lowpoly_starfox.model.ObjLoader
import com.sotrh.lowpoly_starfox.shader.DebugShader
import com.sotrh.lowpoly_starfox.texture.TextureManager
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

/**
 * Created by benjamin on 10/30/17
 */

fun main(args: Array<String>) {
    if (!GLFW.glfwInit()) throw IllegalStateException("GLFW failed to init")

    var lastTime = GLFW.glfwGetTime()

    val displayManager = DisplayManager()
    val display = displayManager.createDisplay(800, 600)

    val inputManager = InputManager(display)
    val debugCameraController = DebugCameraController()
    inputManager.addKeyListener(debugCameraController)
    inputManager.addMouseListener(debugCameraController)

    val modelLoader = ModelLoader()
    val modelRenderer = ModelRenderer()

    val objModelLoader = ObjLoader()
    val objModel = objModelLoader.loadObjWithTextureAndNormals("egg1_msd_uv.obj", modelLoader)

    val textureManager = TextureManager()
    val texture = textureManager.loadTexture2DFromResource("textures/test_texture.png")

    val light = Light(color = Vector3f(1f, 1f, 1f))

    val debugShader = DebugShader()

    val camera = Camera()
    camera.position.set(0f, 0f, 4f)

    // create the entities
    val entityManager = EntityManager()
    val range = -5..5
    range.forEach { x ->
        range.forEach { y ->
            range.forEach { z ->
                val entity = Entity(objModel)
                entity.position.set(x * 10f, y * 10f, z * 10f)
                entity.yaw = y * PIf / 10f
                entity.pitch = x * PIf / 10f
                entity.roll = z* PIf / 10f
                entityManager.addEntity(entity)
            }
        }
    }

    val modelMatrix = Matrix4f()

    while (!display.shouldClose) {
        val currentTime = GLFW.glfwGetTime()
        val deltaTime = (currentTime - lastTime).toFloat()
        lastTime = currentTime

        debugCameraController.updateCamera(camera, deltaTime)
        light.position.set(camera.position)

        debugShader.bind()
        modelRenderer.prepare()

        debugShader.bindTexture(texture)
        debugShader.setLightUniforms(light)
        debugShader.setReflectivityAndLightDamper(0.5f, 32f)
        entityManager.forEachEntity { entity ->
            modelMatrix.identity()
                    .translate(entity.position)
                    .rotateZ(entity.roll)
                    .rotateX(entity.pitch)
                    .rotateY(entity.yaw)
            debugShader.applyTransform(camera, display, modelMatrix)
            modelRenderer.render(entity.model)
        }

        debugShader.unbind()

        display.swapBuffers()
        display.pollEvents()
    }

    debugShader.cleanup()
    modelLoader.cleanup()
    inputManager.cleanup()
    textureManager.cleanup()
    displayManager.cleanup()
}