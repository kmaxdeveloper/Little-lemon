package com.example.littlelemon

import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URL

class ExampleUnitTest {
    @Test
    fun testJson() {
        val url = "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/littleLemonSimpleMenu.json"
        val json = URL(url).readText()
        assertEquals("", json)
    }
}
