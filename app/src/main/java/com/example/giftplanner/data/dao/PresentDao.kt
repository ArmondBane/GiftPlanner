package com.example.giftplanner.data.dao

import androidx.room.*
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.Entity.Present
import kotlinx.coroutines.flow.Flow

@Dao
interface PresentDao {

    @Query("SELECT * FROM presents")
    fun getAllPresents(): Flow<List<Present>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(present: Present)

    @Update
    suspend fun update(present: Present)

    @Delete
    suspend fun delete(present: Present)
}