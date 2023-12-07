package com.elfennani.readit.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SessionDao {
    @Query("SELECT * FROM session WHERE userId = :userId")
    suspend fun getSessionByUserId(userId:String) : Session
    @Upsert
    suspend fun upsertSession(session: Session)
}