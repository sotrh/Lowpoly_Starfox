package com.sotrh.lowpoly_starfox.entity

import com.sotrh.lowpoly_starfox.model.Model
import org.joml.Vector3f

/**
 * Created by benjamin on 11/8/17
 */
class Entity(val model: Model) {
    val position = Vector3f()
    var yaw = 0f
    var pitch = 0f
    var roll = 0f
}