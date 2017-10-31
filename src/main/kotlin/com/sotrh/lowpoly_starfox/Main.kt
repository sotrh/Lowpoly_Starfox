package com.sotrh.lowpoly_starfox

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11

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

    while (!GLFW.glfwWindowShouldClose(window)) {
        GL11.glClearColor(0.4f, 0.4f, 0.5f, 1.0f)
        GL11.glEnable(GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        // render code here

        GL11.glDisable(GL11.GL_DEPTH_BUFFER_BIT)

        GLFW.glfwSwapBuffers(window)
        GLFW.glfwPollEvents()
    }

    Callbacks.glfwFreeCallbacks(window)
    GLFW.glfwDestroyWindow(window)

}