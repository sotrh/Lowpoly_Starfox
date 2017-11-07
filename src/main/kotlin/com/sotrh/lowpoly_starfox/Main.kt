package com.sotrh.lowpoly_starfox

import com.sotrh.lowpoly_starfox.camera.Camera
import com.sotrh.lowpoly_starfox.camera.DebugCameraController
import com.sotrh.lowpoly_starfox.display.DisplayManager
import com.sotrh.lowpoly_starfox.input.InputManager
import com.sotrh.lowpoly_starfox.model.ModelLoader
import com.sotrh.lowpoly_starfox.model.ModelRenderer
import com.sotrh.lowpoly_starfox.model.ObjLoader
import com.sotrh.lowpoly_starfox.shader.DebugShader
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

    val quadModel = modelLoader.loadSimpleIndexedQuad()

    val objModelLoader = ObjLoader()
    val objModel = objModelLoader.loadObjWithTextureAndNormals("cube.obj", modelLoader)

    val debugShader = DebugShader()

    val camera = Camera()
    camera.position.set(0f, 0f, 4f)

    while (!display.shouldClose) {
        val currentTime = GLFW.glfwGetTime()
        val deltaTime = (currentTime - lastTime).toFloat()
        lastTime = currentTime

        debugCameraController.updateCamera(camera, deltaTime)

        debugShader.bind()
        debugShader.applyTransform(camera, display)
        modelRenderer.prepare()
        modelRenderer.render(objModel)
        modelRenderer.renderIndexed(quadModel)
        debugShader.unbind()

        display.swapBuffers()
        display.pollEvents()
    }

    debugShader.cleanup()
    modelLoader.cleanup()
    inputManager.cleanup()
    displayManager.cleanup()
}