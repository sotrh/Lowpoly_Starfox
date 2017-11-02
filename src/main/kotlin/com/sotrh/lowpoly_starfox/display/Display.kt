package com.sotrh.lowpoly_starfox.display

import org.lwjgl.glfw.GLFW

class Display(val windowId: Long, val id: Int) {

    var width = 0
        private set
    var height = 0
        private set

    val shouldClose get() =  GLFW.glfwWindowShouldClose(windowId)

    init {
        GLFW.glfwSetFramebufferSizeCallback(windowId) { _, width, height ->
            this.width = width
            this.height = height
        }
    }

    fun show() {
        GLFW.glfwShowWindow(windowId)
    }

    fun hide() {
        GLFW.glfwHideWindow(windowId)
    }

    fun close() {
        GLFW.glfwSetWindowShouldClose(windowId, true)
    }

    fun swapBuffers() {
        GLFW.glfwSwapBuffers(windowId)
    }

    fun pollEvents() {
        GLFW.glfwPollEvents()
    }
}