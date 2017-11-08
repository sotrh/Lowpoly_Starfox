package com.sotrh.lowpoly_starfox.entity

/**
 * Created by benjamin on 11/8/17
 */
class EntityManager {
    private val entityList = mutableListOf<Entity>()

    fun addEntity(entity: Entity) {
        entityList.add(entity)
    }

    fun cleanup() {
        entityList.clear()
    }

    fun forEachEntity(block: (entity: Entity) -> Unit) {
        entityList.forEach(block)
    }
}