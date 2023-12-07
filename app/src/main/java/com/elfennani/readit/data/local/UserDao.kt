package com.elfennani.readit.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers():Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId:String): Flow<User>

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String) : Flow<User>

    @Upsert
    suspend fun upsertUser(user: User)
}