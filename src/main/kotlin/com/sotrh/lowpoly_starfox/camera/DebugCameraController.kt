package com.sotrh.lowpoly_starfox.camera

import com.sotrh.lowpoly_starfox.common.toRadians
import com.sotrh.lowpoly_starfox.input.InputManager
import org.lwjgl.glfw.GLFW

class DebugCameraController : InputManager.KeyListener, InputManager.MouseListener {

    private var upSpeed = 0.0f
    private var downSpeed = 0.0f
    private var leftSpeed = 0.0f
    private var rightSpeed = 0.0f
    private var forwardSpeed = 0.0f
    private var backwardSpeed = 0.0f

    var movementSpeed = 5.0f

    private var mouseX = 0.0f
    private var mouseY = 0.0f
    private var lastMouseX = 0.0f
    private var lastMouseY = 0.0f
    private var mouseButtonPressed = false

    var rotationSpeed = 80.0f

    fun updateCamera(camera: Camera, delta: Float) {
        val rotSpeed = (rotationSpeed * delta).toRadians()

        if (mouseButtonPressed) {
            camera.yaw += (mouseX - lastMouseX) * rotSpeed
            camera.pitch += (mouseY - lastMouseY) * rotSpeed
        }

        lastMouseX = mouseX
        lastMouseY = mouseY

        val speed = movementSpeed * delta
        var dx = 0f
        var dy = 0f
        var dz = 0f

        dy += speed * upSpeed
        dy -= speed * downSpeed
        dx -= speed * leftSpeed
        dx += speed * rightSpeed
        dz += speed * forwardSpeed
        dz -= speed * backwardSpeed

        camera.position.x += Math.sin(camera.yaw.toDouble()).toFloat() * dz
        camera.position.z += Math.cos(camera.yaw.toDouble()).toFloat() * -1f * dz

        camera.position.x += Math.cos(camera.yaw.toDouble()).toFloat() * dx
        camera.position.z += Math.sin(camera.yaw.toDouble()).toFloat() * dx

        camera.position.y += dy
    }

    override fun onKeyEvent(key: Int, scancode: Int, action: Int, mods: Int) {
        val keyPressedOrRepeating = action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT
        val speed = if(keyPressedOrRepeating) 1.0f else 0.0f
        when (key) {
            GLFW.GLFW_KEY_SPACE -> upSpeed = speed
            GLFW.GLFW_KEY_LEFT_SHIFT -> downSpeed = speed
            GLFW.GLFW_KEY_A -> leftSpeed = speed
            GLFW.GLFW_KEY_D -> rightSpeed = speed
            GLFW.GLFW_KEY_W -> forwardSpeed = speed
            GLFW.GLFW_KEY_S -> backwardSpeed = speed
        }
    }

    override fun onMouseEnter(entered: Boolean) {

    }

    override fun onMouseMove(xpos: Double, ypos: Double) {
        mouseX = xpos.toFloat()
        mouseY = ypos.toFloat()
    }

    override fun onMouseButton(button: Int, action: Int, mods: Int) {
        mouseButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS
    }

    override fun onMouseScroll(xoffset: Double, yoffset: Double) {

    }
}