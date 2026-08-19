package com.group2.artfinder

import com.group2.artfinder.data.PointsCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class PointsCalculatorTest {

    @Test
    fun pointsAreCalculatedCorrectlyForPhotoCounts() {
        assertEquals(0, PointsCalculator.pointsForPhotoCount(0))
        assertEquals(10, PointsCalculator.pointsForPhotoCount(1))
        assertEquals(10, PointsCalculator.pointsForPhotoCount(5))
        assertEquals(20, PointsCalculator.pointsForPhotoCount(6))
        assertEquals(20, PointsCalculator.pointsForPhotoCount(10))
    }

    @Test
    fun badgeIsAssignedCorrectlyForPointTotals() {
        assertEquals("Explorer", PointsCalculator.badgeForPoints(0))
        assertEquals("Explorer", PointsCalculator.badgeForPoints(100))
        assertEquals("Curator", PointsCalculator.badgeForPoints(101))
        assertEquals("Curator", PointsCalculator.badgeForPoints(250))
        assertEquals("Archivist", PointsCalculator.badgeForPoints(251))
        assertEquals("Archivist", PointsCalculator.badgeForPoints(500))
    }
}