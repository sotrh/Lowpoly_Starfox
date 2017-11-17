package com.sotrh.lowpoly_starfox.font

import com.sotrh.lowpoly_starfox.file.FileUtil
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.stb.STBTTBakedChar
import org.lwjgl.stb.STBTTFontinfo
import org.lwjgl.stb.STBTruetype
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

/**
 * Created by benjamin on 11/16/17
 */
class FontManager {
    fun loadFontFromResource(resource: String, fontHeight: Float): Font {
        val trueTypeFont = FileUtil.getResourceAsByteBuffer(resource, 512 * 1024)
                ?: throw IllegalStateException("Unable to load font resource $resource")

        val info = STBTTFontinfo.create()
        if (!STBTruetype.stbtt_InitFont(info, trueTypeFont)) {
            throw IllegalStateException("Failed to intialize TrueType font information")
        }

        var ascent = 0
        var descent = 0
        var lineGap = 0

        MemoryStack.stackPush().use { stack ->
            val pAscent = stack.mallocInt(1)
            val pDescent = stack.mallocInt(1)
            val pLineGap = stack.mallocInt(1)

            STBTruetype.stbtt_GetFontVMetrics(info, pAscent, pDescent, pLineGap)

            ascent = pAscent.get(0)
            descent = pDescent.get(0)
            lineGap = pLineGap.get(0)
        }

        val cdata = STBTTBakedChar.malloc(96)

        val texture = createOpenGLTexture(trueTypeFont, fontHeight, cdata)

        return Font(info, ascent, descent, lineGap, cdata, texture)
    }

    private fun createOpenGLTexture(trueTypeFont: ByteBuffer, fontHeight: Float, cdata: STBTTBakedChar.Buffer?): Int {
        val bwidth = 512
        val bheight = 512
        val bitmap = BufferUtils.createByteBuffer(bwidth * bheight)
        STBTruetype.stbtt_BakeFontBitmap(trueTypeFont, fontHeight, bitmap, bwidth, bheight, 32, cdata)

        val texture = GL11.glGenTextures()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, bwidth, bheight, 0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, bitmap)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
        return texture
    }
}