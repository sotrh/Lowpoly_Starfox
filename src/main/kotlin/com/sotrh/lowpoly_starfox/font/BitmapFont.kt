package com.sotrh.lowpoly_starfox.font

import com.sotrh.lowpoly_starfox.file.FileUtil
import com.sotrh.lowpoly_starfox.texture.Texture

/**
 * Created by benjamin on 11/24/17
 */
class BitmapFont(fontFilename: String) {

//    val info: Info
//    val common: Common

    init {
        val reader = FileUtil.getResourceAsReader(fontFilename)

        val infoLine = reader.readLine()

        var index = 0;

    }

    class Info(val face: String, val size: Int, val bold: Int, val italic: Int, val charset: String, val stretchH: Int, val smooth: Int, val aa: Int, val padding: Padding, val spacing: Spacing)
    class Padding(val top: Int, val right: Int, val bottom: Int, val left: Int)
    class Spacing(val x: Int, val y: Int)
    class Common(val lineHeight: Int, val base: Int, val scaleW: Int, val scaleH: Int, val pages: Int, val packed: Int)
    class Page(val id: Int, val file: String, val texture: Texture)
    class Char(val id: Int, val x: Int, val y: Int, val width: Int, val height: Int, val xoffset: Int, val yoffset: Int, val xadvance: Int, val page: Int, val chnl: Int)
    class Kerning(val first: Int, val second: Int, val amount: Int)
}