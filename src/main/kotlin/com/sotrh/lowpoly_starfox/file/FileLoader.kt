package com.sotrh.lowpoly_starfox.file

import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

/**
 * Created by benjamin on 11/6/17
 */
object FileLoader {
    fun getResourceReader(resource: String): BufferedReader {
        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(resource)
                ?: throw FileNotFoundException("Unable to find resource \"$resource\"")
        return BufferedReader(InputStreamReader(stream))
    }
}