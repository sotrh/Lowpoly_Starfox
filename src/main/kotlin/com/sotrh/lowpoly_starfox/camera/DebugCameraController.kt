package com.sotrh.lowpoly_starfox.camera

import com.sotrh.lowpoly_starfox.input.InputManager
import org.lwjgl.glfw.GLFW

class DebugCameraController : InputManager.KeyListener, InputManager.MouseListener {

    var upPressed = false; private set
    var downPressed = false; private set
    var leftPressed = false; private set
    var rightPressed = false; private set
    var forwardPressed = false; private set
    var backwardPressed = false; private set

    var movementSpeed = 0.2f

    fun updateCamera(camera: Camera, delta: Float) {
        val speed = movementSpeed * delta
        var dx = 0f
        var dy = 0f
        var dz = 0f

        if (upPressed) dy += speed
        if (downPressed) dy -= speed
        if (leftPressed) dx -= speed
        if (rightPressed) dx += speed
        if (forwardPressed) dz -= speed
        if (backwardPressed) dz += speed

        camera.position.x += Math.sin(camera.yaw.toDouble()).toFloat() * dz
        camera.position.z += Math.cos(camera.yaw.toDouble()).toFloat() * dz

        camera.position.x += Math.cos(camera.yaw.toDouble()).toFloat() * dx
        camera.position.z += Math.sin(camera.yaw.toDouble()).toFloat() * dx

        camera.position.y += dy
    }

    override fun onKeyEvent(key: Int, scancode: Int, action: Int, mods: Int) {
        val keyPressedOrRepeating = action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT
        when (key) {
            GLFW.GLFW_KEY_SPACE -> upPressed = keyPressedOrRepeating
            GLFW.GLFW_KEY_LEFT_SHIFT -> downPressed = keyPressedOrRepeating
            GLFW.GLFW_KEY_A -> leftPressed = keyPressedOrRepeating
            GLFW.GLFW_KEY_D -> rightPressed = keyPressedOrRepeating
            GLFW.GLFW_KEY_W -> forwardPressed = keyPressedOrRepeating
            GLFW.GLFW_KEY_S -> backwardPressed = keyPressedOrRepeating
        }
    }

    override fun onMouseEnter(entered: Boolean) {

    }

    override fun onMouseMove(xpos: Double, ypos: Double) {

    }

    override fun onMouseButton(button: Int, action: Int, mods: Int) {

    }

    override fun onMouseScroll(xoffset: Double, yoffset: Double) {

    }
}