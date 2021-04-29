package com.example.giftplanner.data.dao

import androidx.room.*
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.Entity.Recipient
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipientDao {

    @Query("SELECT * FROM recipients")
    fun getAllRecipientsNames(): List<Recipient>

    @Query("SELECT * FROM recipients")
    fun getAllRecipients(): Flow<List<Recipient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipient: Recipient)

    @Update
    suspend fun update(recipient: Recipient)

    @Delete
    suspend fun delete(recipient: Recipient)
}