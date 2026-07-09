package com.example.liquidrun.models

import java.util.Date
import java.util.UUID

data class AppUser(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val birthday: Date? = null,
    val avatarBase64: String? = null,
    val colorHex: String? = null,
    val passwordHash: String? = null
)

data class CapturedZone(
    val id: String, // The grid ID, e.g. "52.123_13.456"
    val latitude: Double,
    val longitude: Double,
    val capturedAt: Date,
    val ownerId: String,
    val ownerName: String,
    val ownerColorHex: String
)

data class TrainingSession(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val startTime: Date = Date(),
    val endTime: Date = Date(),
    val distanceMeters: Double = 0.0,
    val activityType: String = "Laufen",
    val assignmentId: String? = null
) {
    val durationSeconds: Double
        get() = ((endTime.time - startTime.time) / 1000).toDouble()

    val averageSpeedKmh: Double
        get() {
            if (durationSeconds <= 0) return 0.0
            return (distanceMeters / 1000.0) / (durationSeconds / 3600.0)
        }

    val averagePaceMinPerKm: Double
        get() {
            if (distanceMeters <= 0) return 0.0
            return (durationSeconds / 60.0) / (distanceMeters / 1000.0)
        }
}

data class Club(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val ownerId: String = "",
    val memberIds: List<String> = emptyList(),
    val adminIds: List<String> = emptyList()
) {
    fun isAdmin(userId: String): Boolean {
        return adminIds.contains(userId) || ownerId == userId
    }
}

enum class AssignmentType(val displayName: String) {
    DAUERLAUF("Dauerlauf"),
    SPRINT("Sprint"),
    INTERVALL("Intervall")
}

data class Assignment(
    val id: String = UUID.randomUUID().toString(),
    val clubId: String = "",
    val title: String = "",
    val type: String = AssignmentType.DAUERLAUF.name,
    val targetDistanceMeters: Double = 0.0,
    val targetTimeSeconds: Double? = null,
    val createdAt: Date = Date(),
    val creatorId: String = ""
)

data class PersonalGoal(
    val id: String = UUID.randomUUID().toString(),
    val targetDistanceMeters: Double = 0.0,
    val targetTimeSeconds: Double? = null
)

data class AssignmentCompletion(
    val id: String = UUID.randomUUID().toString(),
    val assignmentId: String = "",
    val userId: String = "",
    val sessionId: String = "",
    val completedAt: Date = Date(),
    val actualDistanceMeters: Double = 0.0,
    val completed: Boolean = false
)
