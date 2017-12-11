package com.sotrh.lowpoly_starfox.model

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class ModelLoader {
    private val vaos = mutableListOf<Int>()
    private val vbos = mutableListOf<Int>()

    fun cleanup() {
        vaos.forEach { GL30.glDeleteVertexArrays(it) }
        vbos.forEach { GL15.glDeleteBuffers(it) }

        vaos.clear()
        vbos.clear()
    }

    fun loadSimpleQuad(): Model {
        return loadSimpleModel(floatArrayOf(
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,

                -0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f
        ))
    }

    fun loadSimpleModel(vertices: FloatArray): Model {
        val vao = GL30.glGenVertexArrays()
        vaos.add(vao)

        GL30.glBindVertexArray(vao)
        storeDataInAttributeList(0, vertices, 3)
        GL30.glBindVertexArray(0)

        return Model(vao, vertices.size / 3, intArrayOf(0))
    }

    fun loadSimpleIndexedQuad(): Model {
        return loadSimpleIndexedModel(
                floatArrayOf(
                        -0.5f, -0.5f, 0.0f,
                        0.5f, -0.5f, 0.0f,
                        0.5f, 0.5f, 0.0f,
                        -0.5f, 0.5f, 0.0f
                ),
                intArrayOf(
                        0, 1, 2,
                        0, 2, 3
                )
        )
    }

    fun loadSimpleIndexedModel(vertices: FloatArray, indices: IntArray): Model {
        val vao = GL30.glGenVertexArrays()
        vaos.add(vao)

        GL30.glBindVertexArray(vao)
        storeIndices(indices)
        storeDataInAttributeList(0, vertices, 3)
        GL30.glBindVertexArray(0)

        return Model(vao, indices.size, intArrayOf(0))
    }

    private fun storeIndices(indices: IntArray) {
        val buffer = BufferUtils.createIntBuffer(indices.size)
        buffer.put(indices)
        buffer.flip()

        val ebo = GL15.glGenBuffers()
        vbos.add(ebo)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
    }

    private fun storeDataInAttributeList(attributeNumber: Int, data: FloatArray, components: Int) {
        val buffer = BufferUtils.createFloatBuffer(data.size)
        buffer.put(data)
        buffer.flip()

        val vbo = GL15.glGenBuffers()
        vbos.add(vbo)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(attributeNumber, components, GL11.GL_FLOAT, false, 0, 0L)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    fun loadDynamic2DTexturedIndexedModel(vertices: FloatArray, indices: IntArray): Model {
        val vao = GL30.glGenVertexArrays()
        vaos.add(vao)
        GL30.glBindVertexArray(vao)

        // Todo: dynamic buffers shouldn't pass in vertices
        val ibuffer = BufferUtils.createIntBuffer(indices.size)
        ibuffer.put(indices)
        ibuffer.flip()

        val ebo = GL15.glGenBuffers()
        vbos.add(ebo)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibuffer, GL15.GL_DYNAMIC_DRAW)

        // Todo: dynamic buffers shouldn't pass in vertices
        val buffer = BufferUtils.createFloatBuffer(vertices.size)
        buffer.put(vertices)
        buffer.flip()

        val vbo = GL15.glGenBuffers()
        vbos.add(vbo)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW)

        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * 4, 0L)
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * 4, 2L * 4L)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

        GL30.glBindVertexArray(0)

        return Model(vao, indices.size, intArrayOf(0, 1))
    }

    fun loadNormalTexturedModel(data: FloatArray): Model {
        val vao = GL30.glGenVertexArrays()
        vaos.add(vao)

        GL30.glBindVertexArray(vao)

        val buffer = BufferUtils.createFloatBuffer(data.size)
        buffer.put(data)
        buffer.flip()

        val vbo = GL15.glGenBuffers()
        vbos.add(vbo)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * 4, 0L)
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 8 * 4, 3L * 4L)
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 8 * 4, 6L * 4L)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

        GL30.glBindVertexArray(0)

        return Model(vao, data.size / 8, intArrayOf(0, 1, 2))
    }
}