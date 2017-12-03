package com.sotrh.lowpoly_starfox.common

/**
 * Created by benjamin on 12/2/17
 */
class Scanner(data: String) {
    private var position: Int = 0
    private var data: String = data

    fun peek(): Char? {
        return if (position < data.length) data[position] else null
    }

    fun read(): Char? {
        val char = peek()
        position++
        return char
    }
}
