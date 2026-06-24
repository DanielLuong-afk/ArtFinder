package com.group2.artfinder.data

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.group2.artfinder.model.ArtworkItem
import com.group2.artfinder.model.VisitedArtwork
import kotlinx.coroutines.tasks.await

class VisitedArtworkRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

    private fun userCollection() =
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("visitedArtworks")

    suspend fun addVisitedArtwork(artwork: ArtworkItem) {
        val docRef = userCollection().document(artwork.id.toString())
        val exists = docRef.get().await().exists()
        if (exists) return  // already visited, do nothing

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
            visitedAt          = Timestamp.now()
        )
        docRef.set(visited).await()
        db.collection("users").document(auth.currentUser!!.uid)
            .update("visitedCount", com.google.firebase.firestore.FieldValue.increment(1))
            .await()
    }

    suspend fun getVisitedArtworks(): List<VisitedArtwork> {
        return userCollection()
            .orderBy("visitedAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(VisitedArtwork::class.java)
    }

    suspend fun deleteVisitedArtwork(artworkId: Int) {
        userCollection().document(artworkId.toString()).delete().await()
        db.collection("users").document(auth.currentUser!!.uid)
            .update("visitedCount", com.google.firebase.firestore.FieldValue.increment(-1))
            .await()
    }

    suspend fun isVisited(artworkId: Int): Boolean {
        return userCollection().document(artworkId.toString()).get().await().exists()
    }
}