package com.sotrh.lowpoly_starfox.file

import org.lwjgl.BufferUtils
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.channels.Channels

/**
 * Created by benjamin on 11/6/17
 */
object FileUtil {
    fun getResourceAsReader(resource: String): BufferedReader {
        val stream = getResourceAsStream(resource)
        return BufferedReader(InputStreamReader(stream))
    }

    fun getResourceAsStream(resource: String): InputStream {
        val classLoader = this::class.java.classLoader
        return classLoader.getResourceAsStream(resource)
                ?: throw FileNotFoundException("Unable to find resource \"$resource\"")
    }

    fun getResourceAsByteBuffer(resource: String, bufferSize: Int = 1024): ByteBuffer? {
        var buffer = BufferUtils.createByteBuffer(bufferSize)

        getResourceAsStream(resource).use { stream ->
            Channels.newChannel(stream).use { readableByteChannel ->

                while (true) {
                    val bytes = readableByteChannel.read(buffer)
                    if (bytes == -1) break
                    else if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2)
                    }
                }
            }
        }

        buffer.flip()
        return buffer.slice()
    }

    private fun resizeBuffer(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
        val newBuffer = BufferUtils.createByteBuffer(newCapacity)
        buffer.flip()
        newBuffer.put(buffer)
        return newBuffer
    }
}