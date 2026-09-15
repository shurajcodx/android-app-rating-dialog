package com.shurajcodx.appratingdialog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AppRatingTest {

    @Test
    fun testRatingModesExist() {
        assertEquals(3, RatingMode.values().size)
        assertNotNull(RatingMode.valueOf("HYBRID"))
        assertNotNull(RatingMode.valueOf("IN_APP_REVIEW_ONLY"))
        assertNotNull(RatingMode.valueOf("CUSTOM_DIALOG"))
    }

    @Test
    fun testStoreTypesExist() {
        assertEquals(5, StoreType.values().size)
        assertNotNull(StoreType.valueOf("GOOGLE_PLAY"))
        assertNotNull(StoreType.valueOf("AMAZON"))
        assertNotNull(StoreType.valueOf("HUAWEI"))
        assertNotNull(StoreType.valueOf("SAMSUNG"))
        assertNotNull(StoreType.valueOf("CUSTOM"))
    }
}
