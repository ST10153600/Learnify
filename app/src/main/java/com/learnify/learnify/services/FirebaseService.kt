package com.learnify.learnify.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

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
}
