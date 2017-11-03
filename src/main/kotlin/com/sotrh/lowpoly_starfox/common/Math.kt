package com.sotrh.lowpoly_starfox.common

import org.joml.Math

/**
 * Created by benjamin on 11/2/17
 */

fun Float.toRadians() = this.toDouble().toRadians().toFloat()
fun Double.toRadians() = Math.toRadians(this)