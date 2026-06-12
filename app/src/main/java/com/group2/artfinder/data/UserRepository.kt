package com.group2.artfinder.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.group2.artfinder.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()


    fun getCurrentUserId() = auth.currentUser?.uid
    fun isLoggedIn()       = auth.currentUser != null
    fun logout()           = auth.signOut()

    suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) { false }
    }

    suspend fun register(
        email:     String,
        password:  String,
        firstName: String,
        lastName:  String
    ): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid    = result.user?.uid ?: return false
            val user   = User(uid = uid, email = email, firstName = firstName, lastName = lastName)
            db.collection("users").document(uid).set(user.toMap()).await()
            true
        } catch (e: Exception) { false }
    }


    suspend fun getUserProfile(): User? {
        val uid = getCurrentUserId() ?: return null
        return try {
            val snapshot = db.collection("users").document(uid).get().await()
            val map      = snapshot.data ?: return null
            User.fromMap(uid, map)
        } catch (e: Exception) { null }
    }

    suspend fun updateProfile(
        firstName: String,
        lastName:  String,
        username:  String,
        dob:       String
    ): UpdateResult {
        val uid = getCurrentUserId() ?: return UpdateResult.Error("Not authenticated")
        return try {
            val existing = db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()
            if (existing.documents.any { it.id != uid }) return UpdateResult.UsernameTaken

            db.collection("users").document(uid).update(
                mapOf(
                    "firstName" to firstName,
                    "lastName"  to lastName,
                    "username"  to username,
                    "dob"       to dob
                )
            ).await()
            UpdateResult.Success
        } catch (e: Exception) {
            UpdateResult.Error(e.message ?: "Unknown error")
        }
    }
}

sealed class UpdateResult {
    object Success       : UpdateResult()
    object UsernameTaken : UpdateResult()
    data class Error(val message: String) : UpdateResult()
}