package com.group2.artfinder.model

data class User(
    val uid:        String  = "",
    val email:      String  = "",
    val firstName:  String  = "",
    val lastName:   String  = "",
    val username:   String  = "",
    val dob:        String  = "",
    val points:     Int     = 0,
    val visitedCount: Int    = 0
) {
    val displayName: String
        get() = "$firstName $lastName".trim()

    val initial: String
        get() = firstName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    fun toMap(): Map<String, Any> = mapOf(
        "email"     to email,
        "firstName" to firstName,
        "lastName"  to lastName,
        "username"  to username,
        "dob"       to dob,
        "points"    to points,
        "visitedCount" to visitedCount
    )

    companion object {
        fun fromMap(uid: String, map: Map<String, Any>): User = User(
            uid       = uid,
            email     = map["email"]     as? String ?: "",
            firstName = map["firstName"] as? String ?: "",
            lastName  = map["lastName"]  as? String ?: "",
            username  = map["username"]  as? String ?: "",
            dob       = map["dob"]       as? String ?: "",
            points    = (map["points"]   as? Long)?.toInt() ?: 0,
            visitedCount = (map["visitedCount"] as? Long)?.toInt() ?: 0
        )
    }
}