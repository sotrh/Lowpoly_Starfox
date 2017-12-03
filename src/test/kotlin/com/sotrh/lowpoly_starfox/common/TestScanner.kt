package com.sotrh.lowpoly_starfox.common

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Created by benjamin on 12/2/17
 */

class TestScanner {
    @Test
    fun testPeek_someData() {
        val data = "someData"
        val scanner = Scanner(data)
        assertEquals(data[0], scanner.peek())
    }

    @Test
    fun testPeek_emptyString() {
        val scanner = Scanner("")
        assertNull(scanner.read())
    }

    @Test
    fun testRead_someData() {
        val data = "someData"
        val scanner = Scanner(data)
        assertEquals(data[0], scanner.read())
        assertEquals(data[1], scanner.read())
    }

    @Test
    fun testRead_emptyString() {
        val scanner = Scanner("")
        assertNull(scanner.read())
    }
}