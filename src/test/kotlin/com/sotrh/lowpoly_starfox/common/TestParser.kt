package com.sotrh.lowpoly_starfox.common

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by benjamin on 12/2/17
 */
class TestParser {

    @Test
    fun testRequire_expected() {
        val expected = "expected"
        val parser = Parser(expected)
        parser.require(expected)
    }

    @Test
    fun testRequire_invalid() {
        val invalid = "invalid"
        val actual = "actual"
        val parser = Parser(actual)
        assertThrows(Throwable::class.java) { parser.require(invalid) }
    }

    @Test
    fun testConsumeWhile_true() {
        val expected = "asdfasdf;klasd;jf;alsdkfj"
        val parser = Parser(expected)
        assertEquals(expected, parser.consumeWhile { true })
    }

    @Test
    fun testConsumeWhile_whitespace() {
        val expected = "   \r\n\t"
        val parser = Parser(expected)
        assertEquals(expected, parser.consumeWhile(Char::isWhitespace))
    }

    @Test
    fun testParseId_valid() {
        val expected = "valid"
        val parser = Parser(expected)
        assertEquals(expected, parser.parseId())
    }

    @Test
    fun testParseId_invalid() {
        val _invalid = " invalid"
        val parser = Parser(_invalid)
        assertThrows(Throwable::class.java) { parser.parseId() }
    }

    @Test
    fun testParseId_valid_as_well() {
        val expected = "valid_as_well"
        val parser = Parser(expected)
        assertEquals(expected, parser.parseId())
    }

    @Test
    fun testParseNumber_123() {
        val expected = "123"
        assertEquals(expected, Parser(expected).parseNumber())
    }

    @Test
    fun testParseNumber_3point14() {
        val expected = "3.14"
        assertEquals(expected, Parser(expected).parseNumber())
    }

    @Test
    fun testParseNumber_point5() {
        val expected = ".5"
        assertEquals(expected, Parser(expected).parseNumber())
    }

    @Test
    fun testParseNumber_neg123() {
        val expected = "-123"
        assertEquals(expected, Parser(expected).parseNumber())
    }

    @Test
    fun testParseNumber_neg3point14() {
        val expected = "-3.14"
        assertEquals(expected, Parser(expected).parseNumber())
    }

    @Test
    fun testParseNumber_negPoint5() {
        val expected = "-.5"
        assertEquals(expected, Parser(expected).parseNumber())
    }

    @Test
    fun testParseNumber_neg() {
        val invalid = "-"
        assertThrows(Throwable::class.java) { Parser(invalid).parseNumber() }
    }
}