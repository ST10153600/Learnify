package com.learnify.learnify.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.learnify.learnify.models.GameSession
import com.learnify.learnify.models.GameStatus
import com.learnify.learnify.models.MatchmakingEntry
import com.learnify.learnify.ui.quizScreen.QuizQuestion

object FirebaseService {
    val auth by lazy { FirebaseAuth.getInstance() }
    val firestore by lazy { FirebaseFirestore.getInstance() }

    fun addLikedQuestion(questionId: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("likedQuestions", FieldValue.arrayUnion(questionId))
    }

    fun removeLikedQuestion(questionId: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("likedQuestions", FieldValue.arrayRemove(questionId))
    }

    fun addFlaggedQuestion(questionId: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("flaggedQuestions", FieldValue.arrayUnion(questionId))
    }

    fun removeFlaggedQuestion(questionId: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("flaggedQuestions", FieldValue.arrayRemove(questionId))
    }

    fun addCompletedQuiz(quizTopic: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("completedQuizzes", FieldValue.arrayUnion(quizTopic))
    }
    fun joinMatchmakingQueue(
        onMatchFound: (String) -> Unit,
        onWaiting: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val playerId = auth.currentUser?.uid
        if (playerId == null) {
            onError(Exception("User not authenticated"))
            return
        }

        val matchmakingEntry = MatchmakingEntry(playerId = playerId)
        firestore.collection("matchmaking")
            .add(matchmakingEntry)
            .addOnSuccessListener {
                listenForMatch(playerId, onMatchFound, onWaiting, onError)
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    private fun listenForMatch(
        playerId: String,
        onMatchFound: (String) -> Unit,
        onWaiting: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("matchmaking")
            .whereNotEqualTo("playerId", playerId)
            .orderBy("timestamp")
            .limit(1)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    onError(e)
                    return@addSnapshotListener
                }
                if (snapshots != null && snapshots.documents.isNotEmpty()) {
                    val matchEntry = snapshots.documents[0].toObject(MatchmakingEntry::class.java)
                    if (matchEntry != null) {
                        createGameSession(matchEntry.playerId, playerId, onMatchFound, onError)
                    }
                } else {
                    onWaiting()
                }
            }
    }

    private fun createGameSession(
        opponentId: String,
        playerId: String,
        onMatchFound: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val gameId = firestore.collection("games").document().id
        val questions = generateMultiplayerQuestions()

        val gameSession = GameSession(
            gameId = gameId,
            player1Id = opponentId,
            player2Id = playerId,
            questions = questions,
            status = GameStatus.IN_PROGRESS
        )

        firestore.collection("games").document(gameId)
            .set(gameSession)
            .addOnSuccessListener {
                removeMatchmakingEntries(opponentId, playerId)
                onMatchFound(gameId)
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    private fun removeMatchmakingEntries(vararg playerIds: String) {
        val matchmakingRef = firestore.collection("matchmaking")
        for (playerId in playerIds) {
            matchmakingRef.whereEqualTo("playerId", playerId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        document.reference.delete()
                    }
                }
        }
    }

    private fun generateMultiplayerQuestions(): List<QuizQuestion> {
        return List(10) { index ->
            QuizQuestion(
                id = "MQ$index",
                question = "Multiplayer Question ${index + 1}",
                options = listOf("Option A", "Option B", "Option C", "Option D"),
                correctAnswer = "Option A"
            )
        }
    }
}
