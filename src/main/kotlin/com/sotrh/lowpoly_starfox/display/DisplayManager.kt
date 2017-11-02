package com.sotrh.lowpoly_starfox.display

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL

class DisplayManager {
    private val displayList = mutableListOf<Display>()

    fun createDisplay(width: Int, height: Int): Display {
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE)

        val window = GLFW.glfwCreateWindow(width, height, "Low-Poly Starfox", 0L, 0L)
        if (window == 0L) throw IllegalStateException("Failed to create GLFW window")

        val display = Display(window, displayList.size)
        displayList.add(display)

        GLFW.glfwSwapInterval(1)

        GLFW.glfwMakeContextCurrent(window)
        GL.createCapabilities()

        return display
    }

    fun getDisplay(id: Int): Display {
        return displayList[id]
    }

    fun cleanup() {
        displayList.forEach {
            Callbacks.glfwFreeCallbacks(it.windowId)
            GLFW.glfwDestroyWindow(it.windowId)
        }
        displayList.clear()
    }
}