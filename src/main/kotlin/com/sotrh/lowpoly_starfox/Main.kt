package com.sotrh.lowpoly_starfox

import com.sotrh.lowpoly_starfox.camera.Camera
import com.sotrh.lowpoly_starfox.display.DisplayManager
import com.sotrh.lowpoly_starfox.model.ModelLoader
import com.sotrh.lowpoly_starfox.model.ModelRenderer
import com.sotrh.lowpoly_starfox.shader.DebugShader
import org.lwjgl.glfw.GLFW

/**
 * Created by benjamin on 10/30/17
 */

fun main(args: Array<String>) {
    if (!GLFW.glfwInit()) throw IllegalStateException("GLFW failed to init")

    val displayManager = DisplayManager()
    val display = displayManager.createDisplay(800, 600)

    val modelLoader = ModelLoader()
    val modelRenderer = ModelRenderer()

    val quadModel = modelLoader.loadSimpleIndexedQuad()

    val debugShader = DebugShader()

    val camera = Camera()
    camera.position.set(0f, 0f, -4f)

    while (!display.shouldClose) {
        debugShader.applyTransform(camera, display)

        modelRenderer.prepare()
        modelRenderer.renderIndexed(quadModel)

        display.swapBuffers()
        display.pollEvents()
    }

    debugShader.cleanup()
    modelLoader.cleanup()
    displayManager.cleanup()
}