package com.sotrh.lowpoly_starfox.model

import com.sotrh.lowpoly_starfox.camera.Camera
import com.sotrh.lowpoly_starfox.display.Display
import com.sotrh.lowpoly_starfox.shader.Shader
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class ModelRenderer {


    fun prepare() {
        // this won't be necessary once we implement a skybox
        GL11.glClearColor(0.3f, 0.3f, 0.5f, 1.0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
    }

    fun render(model: Model) {
        GL30.glBindVertexArray(model.id)
        model.attributeArrays.forEach { GL20.glEnableVertexAttribArray(it) }
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.vertexCount)
        model.attributeArrays.forEach { GL20.glDisableVertexAttribArray(it) }
        GL30.glBindVertexArray(0)
    }

    fun renderIndexed(model: Model) {
        GL30.glBindVertexArray(model.id)
        model.attributeArrays.forEach { GL20.glEnableVertexAttribArray(it) }
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.vertexCount, GL11.GL_UNSIGNED_INT, 0)
        model.attributeArrays.forEach { GL20.glDisableVertexAttribArray(it) }
        GL30.glBindVertexArray(0)
    }
}