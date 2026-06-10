package com.group2.artfinder.repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun register(email: String, password: String, name: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return false
            val user = hashMapOf("name" to name, "email" to email, "points" to 0)
            db.collection("users").document(uid).set(user).await()
            true
        } catch (e: Exception) { false }
    }

    suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) { false }
    }

    fun getCurrentUserId() = auth.currentUser?.uid

    fun isLoggedIn() = auth.currentUser != null

    suspend fun getUserProfile(): Map<String, Any>? {
        val uid = getCurrentUserId() ?: return null
        return db.collection("users").document(uid).get().await().data
    }

    suspend fun updateUserProfile(name: String): Boolean {
        val uid = getCurrentUserId() ?: return false
        return try {
            db.collection("users").document(uid).update("name", name).await()
            true
        } catch (e: Exception) { false }
    }

    fun logout() = auth.signOut()
}