package com.sotrh.lowpoly_starfox.texture

/**
 * Created by benjamin on 11/13/17
 */
data class Texture(val textureId: Int,
                   val name: String,
                   val width: Int,
                   val height: Int,
                   val components: Int,
                   val isHdrFromMemory: Boolean)