package com.sotrh.lowpoly_starfox.display

import com.sotrh.lowpoly_starfox.common.toRadians
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW

class Display(val windowId: Long, val id: Int, startWidth: Int, startHeight: Int) {

    var width = startWidth
        private set
    var height = startHeight
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

    fun applyDisplayTransformation(matrix: Matrix4f) {
        matrix.identity().perspective(80f.toRadians(), width.toFloat() / height, 0.1f, 1000f)
    }
}