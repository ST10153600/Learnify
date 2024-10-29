package com.learnify.learnify.models

import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.Timestamp

data class MatchmakingEntry(
    val playerId: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null
)
