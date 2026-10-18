package com.example.gamestream.net

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HostAddressTest {
    @Test
    fun plainAddressUsesDefaultPort() {
        assertEquals("192.168.1.10" to DEFAULT_PORT, parseHostAddress("192.168.1.10"))
    }

    @Test
    fun addressWithPortKeepsThatPort() {
        assertEquals("192.168.1.10" to 5000, parseHostAddress("192.168.1.10:5000"))
    }

    @Test
    fun surroundingSpacesAreIgnored() {
        assertEquals("10.0.0.2" to DEFAULT_PORT, parseHostAddress("  10.0.0.2  "))
    }

    @Test
    fun blankInputIsRejected() {
        assertNull(parseHostAddress("   "))
    }

    @Test
    fun invalidPortsAreRejected() {
        assertNull(parseHostAddress("10.0.0.2:0"))
        assertNull(parseHostAddress("10.0.0.2:99999"))
        assertNull(parseHostAddress("10.0.0.2:abc"))
        assertNull(parseHostAddress("10.0.0.2:"))
    }

    @Test
    fun missingHostOrExtraColonsAreRejected() {
        assertNull(parseHostAddress(":5000"))
        assertNull(parseHostAddress("a:b:c"))
    }
}
