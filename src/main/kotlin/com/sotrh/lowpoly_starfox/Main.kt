package com.sotrh.lowpoly_starfox

import com.sotrh.lowpoly_starfox.model.ModelLoader
import com.sotrh.lowpoly_starfox.model.ModelRenderer
import com.sotrh.lowpoly_starfox.shader.DebugShader
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL

/**
 * Created by benjamin on 10/30/17
 */

fun main(args: Array<String>) {
    if (!GLFW.glfwInit()) throw IllegalStateException("GLFW failed to init")

    GLFW.glfwDefaultWindowHints()
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE)
    val window = GLFW.glfwCreateWindow(800, 600, "Low-Poly Starfox", 0L, 0L)

    if (window == 0L) throw IllegalStateException("Failed to create GLFW window")

    GLFW.glfwSwapInterval(1)
    GLFW.glfwShowWindow(window)

    GLFW.glfwMakeContextCurrent(window)
    GL.createCapabilities()

    val modelLoader = ModelLoader()
    val modelRenderer = ModelRenderer()

    val quadModel = modelLoader.loadSimpleIndexedQuad()

    val debugShader = DebugShader()

    while (!GLFW.glfwWindowShouldClose(window)) {
        modelRenderer.prepare()
        debugShader.bind()
        modelRenderer.renderIndexed(quadModel)
        debugShader.unbind()

        GLFW.glfwSwapBuffers(window)
        GLFW.glfwPollEvents()
    }

    debugShader.cleanup()
    modelLoader.cleanup()

    Callbacks.glfwFreeCallbacks(window)
    GLFW.glfwDestroyWindow(window)

}