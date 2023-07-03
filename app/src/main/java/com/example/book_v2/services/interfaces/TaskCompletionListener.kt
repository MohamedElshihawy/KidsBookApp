package com.example.book_v2.services.interfaces

interface TaskCompletionListener {

    fun onTaskCompleted(accuracy: Double)

    fun onOutOfPathLineDetected(x: Float, y: Float, newScore: Double)

    fun onProgressAchieved(progress: Double, overAll: Int)

}