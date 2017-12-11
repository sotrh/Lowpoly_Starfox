package com.sotrh.lowpoly_starfox.font

import com.sotrh.lowpoly_starfox.model.ModelLoader
import org.joml.Vector4f
import org.lwjgl.opengl.GL11

/**
 * Created by benjamin on 12/3/17
 */
class TextRenderer(private val font: BitmapFont, modelLoader: ModelLoader, maxCharQuads: Int = 1024) {

    private var totalCharsToRender = 0

    // Todo: don't allocate arrays
    private val charQuadBatchModel = modelLoader.loadDynamic2DTexturedIndexedModel(
            FloatArray(maxCharQuads * 4 * 4),
            IntArray(maxCharQuads * 4)
    )

    fun prepare() {
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    }

    fun pushQuadForChar(char: Char) {
        val bmfChar = font.charMap[char.toInt()]!!
        val texCoords = calcTexCoordsForChar(bmfChar)

        

        totalCharsToRender++
    }

    // Todo: make this private
    fun calcTexCoordsForChar(char: BitmapFont.Char): Vector4f {
        // Todo: don't recreate a vector object
        val texCoords = Vector4f()

        texCoords.x = char.x / font.common.scaleW.toFloat() // left
        texCoords.y = (char.y + char.height) / font.common.scaleH.toFloat() // bottom
        texCoords.z = (char.x + char.width) / font.common.scaleW.toFloat() // right
        texCoords.w = char.y / font.common.scaleH.toFloat() // top

        return texCoords
    }
}