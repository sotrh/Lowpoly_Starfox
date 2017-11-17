package com.sotrh.lowpoly_starfox.font

import org.lwjgl.stb.STBTTBakedChar
import org.lwjgl.stb.STBTTFontinfo

/**
 * Created by benjamin on 11/16/17
 */
data class Font(
        val info: STBTTFontinfo,
        val ascent: Int,
        val descent: Int,
        val lineGap: Int, val cdata: STBTTBakedChar.Buffer, val texture: Int
)