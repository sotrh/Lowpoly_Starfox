package com.sotrh.lowpoly_starfox.texture

import com.sotrh.lowpoly_starfox.file.FileUtil
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImageResize
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import kotlin.experimental.and

/**
 * Created by benjamin on 11/10/17
 */
class TextureManager {

    private val textureMap = mutableMapOf<String, Texture>()

    fun cleanup() {
        textureMap.forEach { (_, texture) ->
            GL11.glDeleteTextures(texture.textureId)
        }
        textureMap.clear()
    }

    fun loadTexture2DFromResource(resource: String): Texture {
        if (textureMap.containsKey(resource)) {
            return textureMap[resource]!!
        }

        val imageBuffer = FileUtil.getResourceAsByteBuffer(resource)

        MemoryStack.stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val comp = stack.mallocInt(1)

            if (!STBImage.stbi_info_from_memory(imageBuffer, w, h, comp)) {
                throw IllegalStateException("Failed to read image information: ${STBImage.stbi_failure_reason()}")
            }

            println("w = ${w.get(0)}")
            println("h = ${h.get(0)}")
            println("components = ${comp.get(0)}")

            val isHdrFromMemory = STBImage.stbi_is_hdr_from_memory(imageBuffer)
            println("isHdrFromMemory = $isHdrFromMemory")

            val image = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 0)
                    ?: throw IllegalStateException("Failed to load image: ${STBImage.stbi_failure_reason()}")

            val width = w.get(0)
            val height = h.get(0)
            val components = comp.get(0)
            val textureId = createOpenGLTexture(components, width, height, image)

            val texture = Texture(textureId, resource, width, height, components, isHdrFromMemory)
            textureMap.put(resource, texture)
            return texture
        }
    }

    private fun createOpenGLTexture(components: Int, width: Int, height: Int, image: ByteBuffer): Int {
        val textureId = GL11.glGenTextures()

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)

        val format = if (components == 3) {
            if ((width and 3) != 0) {
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 2 - (width and 1))
            }
            GL11.GL_RGB
        } else {
            premultiplyAlpha(width, height, image)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.GL_RGBA
        }

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width, height, 0, format,
                GL11.GL_UNSIGNED_BYTE, image)

        createMipmaps(image, width, height, components, format)

        return textureId
    }

    private fun premultiplyAlpha(width: Int, height: Int, image: ByteBuffer) {
        val stride = width * 4
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                val i = y * stride + x * 4

                val alpha = (image.get(i + 3) and 0xFF.toByte()) / 255.0f
                image.put(i + 0, Math.round((image.get(i + 0) and 0xFF.toByte()) * alpha).toByte())
                image.put(i + 1, Math.round((image.get(i + 1) and 0xFF.toByte()) * alpha).toByte())
                image.put(i + 2, Math.round((image.get(i + 2) and 0xFF.toByte()) * alpha).toByte())
            }
        }
    }

    private fun createMipmaps(image: ByteBuffer, width: Int, height: Int, components: Int, format: Int) {
        var inputPixels = image
        var inputWidth = width
        var inputHeight = height
        var mipmapLevel = 0

        while (inputWidth > 1 && inputHeight > 1) {
            println("inputWidth = $inputWidth, inputHeight = $inputHeight")
            val outputWidth = Math.max(1, inputWidth shr 1)
            val outputHeight = Math.max(1, inputHeight shr 1)

            val outputPixels = MemoryUtil.memAlloc(outputWidth * outputHeight * components)
            STBImageResize.stbir_resize_uint8_generic(inputPixels, inputWidth, inputHeight,
                    inputWidth * components, outputPixels, outputWidth, outputHeight,
                    outputWidth * components, components,
                    if (components == 4) 3 else STBImageResize.STBIR_ALPHA_CHANNEL_NONE,
                    STBImageResize.STBIR_FLAG_ALPHA_PREMULTIPLIED,
                    STBImageResize.STBIR_EDGE_CLAMP,
                    STBImageResize.STBIR_FILTER_MITCHELL,
                    STBImageResize.STBIR_COLORSPACE_SRGB)

            if (mipmapLevel == 0) {
                STBImage.stbi_image_free(image)
            } else {
                MemoryUtil.memFree(inputPixels)
            }

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, ++mipmapLevel, format, outputWidth, outputHeight, 0,
                    format, GL11.GL_UNSIGNED_BYTE, outputPixels)

            inputPixels = outputPixels
            inputWidth = outputWidth
            inputHeight = outputHeight
        }

        if (mipmapLevel == 0) {
            STBImage.stbi_image_free(image)
        } else {
            MemoryUtil.memFree(inputPixels)
        }
    }
}