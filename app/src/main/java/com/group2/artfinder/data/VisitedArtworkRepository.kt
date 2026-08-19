package com.group2.artfinder.data

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.group2.artfinder.BuildConfig
import com.group2.artfinder.model.ArtworkItem
import com.group2.artfinder.model.VisitedArtwork
import kotlinx.coroutines.tasks.await

class VisitedArtworkRepository {
    private val auth    = FirebaseAuth.getInstance()
    private val db      = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val USE_FIREBASE_STORAGE = false  // change to false if no Firebase Storage
    private fun uid() = auth.currentUser!!.uid

    private fun userCollection() =
        db.collection("users").document(uid()).collection("visitedArtworks")

    suspend fun addVisitedArtwork(artwork: ArtworkItem) {
        val docRef = userCollection().document(artwork.id.toString())
        if (docRef.get().await().exists()) return

        val visited = VisitedArtwork(
            id                 = artwork.id,
            title              = artwork.title,
            artist_display     = artwork.artist_display,
            artwork_type_title = artwork.artwork_type_title,
            image_id           = artwork.image_id,
            gallery_title      = artwork.gallery_title,
            date_display       = artwork.date_display,
            place_of_origin    = artwork.place_of_origin,
            medium_display     = artwork.medium_display,
            latitude           = artwork.latitude,
            longitude          = artwork.longitude,
            visitedAt          = Timestamp.now(),
            photos             = emptyList(),
            pointsAwarded      = 0
        )
        docRef.set(visited).await()
        db.collection("users").document(uid())
            .update("visitedCount", FieldValue.increment(1)).await()
    }

    suspend fun getVisitedArtworks(): List<VisitedArtwork> {
        return userCollection()
            .orderBy("visitedAt", Query.Direction.DESCENDING)
            .get().await()
            .toObjects(VisitedArtwork::class.java)
    }

    suspend fun deleteVisitedArtwork(artworkId: Int) {
        val doc = userCollection().document(artworkId.toString()).get().await()
        val artwork = doc.toObject(VisitedArtwork::class.java)
        val pointsToRemove = artwork?.pointsAwarded ?: 0

        userCollection().document(artworkId.toString()).delete().await()

        db.collection("users").document(uid()).update(
            mapOf(
                "visitedCount" to FieldValue.increment(-1),
                "points"       to FieldValue.increment(-pointsToRemove.toLong())
            )
        ).await()
        updateBadge()
    }

    suspend fun isVisited(artworkId: Int): Boolean {
        return userCollection().document(artworkId.toString()).get().await().exists()
    }

    suspend fun addPhoto(context: Context, artworkId: Int, uri: Uri): String {
        return if (USE_FIREBASE_STORAGE) {
            uploadToFirebaseStorage(artworkId, uri)
        } else {
            encodeToBase64(context, uri)
        }
    }

    private suspend fun uploadToFirebaseStorage(artworkId: Int, uri: Uri): String {
        val ref = storage.reference
            .child("users/${uid()}/artworks/$artworkId/${System.currentTimeMillis()}.jpg")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    private fun encodeToBase64(context: Context, uri: Uri): String {
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: return ""
        return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    suspend fun savePhotoAndUpdatePoints(context: Context, artworkId: Int, uri: Uri) {
        val photoData = addPhoto(context, artworkId, uri)
        val docRef   = userCollection().document(artworkId.toString())
        val doc      = docRef.get().await()
        val artwork  = doc.toObject(VisitedArtwork::class.java) ?: return

        val currentPhotos  = artwork.photos.toMutableList()
        val previousPoints = artwork.pointsAwarded

        currentPhotos.add(photoData)
        val newPhotoCount = currentPhotos.size

        val newPoints = PointsCalculator.pointsForPhotoCount(newPhotoCount)

        val pointsDiff = newPoints - previousPoints

        docRef.update(
            mapOf(
                "photos"        to currentPhotos,
                "pointsAwarded" to newPoints
            )
        ).await()

        if (pointsDiff > 0) {
            db.collection("users").document(uid())
                .update("points", FieldValue.increment(pointsDiff.toLong())).await()
            updateBadge()
        }
    }

    suspend fun getPhotos(artworkId: Int): List<String> {
        val doc = userCollection().document(artworkId.toString()).get().await()
        return doc.toObject(VisitedArtwork::class.java)?.photos ?: emptyList()
    }

    private suspend fun updateBadge() {
        val userDoc = db.collection("users").document(uid()).get().await()
        val points  = (userDoc.getLong("points") ?: 0).toInt()
        val badge   = PointsCalculator.badgeForPoints(points)
        db.collection("users").document(uid()).update("badge", badge).await()
    }
}