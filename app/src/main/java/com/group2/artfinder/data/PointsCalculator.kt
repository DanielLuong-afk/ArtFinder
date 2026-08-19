package com.group2.artfinder.data

object PointsCalculator {

    fun pointsForPhotoCount(photoCount: Int): Int = when {
        photoCount <= 0        -> 0
        photoCount in 1..5     -> 10
        else                   -> 20
    }

    fun badgeForPoints(points: Int): String = when {
        points <= 100 -> "Explorer"
        points <= 250 -> "Curator"
        else          -> "Archivist"
    }
}