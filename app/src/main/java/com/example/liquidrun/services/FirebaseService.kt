package com.example.liquidrun.services

import com.example.liquidrun.models.AppUser
import com.example.liquidrun.models.Club
import com.example.liquidrun.models.TrainingSession
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseService {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getUser(userId: String): AppUser? {
        return try {
            val document = db.collection("users").document(userId).get().await()
            document.toObject(AppUser::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveUser(user: AppUser): Boolean {
        return try {
            db.collection("users").document(user.id).set(user).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getClub(clubId: String): Club? {
        return try {
            val document = db.collection("clubs").document(clubId).get().await()
            document.toObject(Club::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveTrainingSession(session: TrainingSession): Boolean {
        return try {
            db.collection("sessions").document(session.id).set(session).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
