package com.cocahonka.saltomaru.database.entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import strikt.api.expectThrows

class LocateTest {

    @Test
    fun `test validation data correct`(){
        Locate(0, 0,0,0)
        Locate(0, 0,-60,0)
        Locate(0, 0,300,0)

        Locate(1, 0,255,0)
        Locate(1, 0,0,0)
    }

    @Test
    fun `test validation data incorrect`(){
        expectThrows<IllegalArgumentException> { Locate(4, -29_000_000, 0, 0) }
        expectThrows<IllegalArgumentException> { Locate(0, 30_000_000, 0, 0) }

        expectThrows<IllegalArgumentException> { Locate(0, -29_000_000, 400, 0) }
        expectThrows<IllegalArgumentException> { Locate(0, -29_000_000, -80, 0) }

        expectThrows<IllegalArgumentException> { Locate(1, -29_000_000, -10, 0) }
        expectThrows<IllegalArgumentException> { Locate(1, -29_000_000, 256, 0) }

        expectThrows<IllegalArgumentException> { Locate(2, -29_000_000, -10, 0) }
        expectThrows<IllegalArgumentException> { Locate(2, -29_000_000, 256, 0) }
    }
}