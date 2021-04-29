package com.example.giftplanner.data.dao

import androidx.room.*
import com.example.giftplanner.data.Entity.Plan
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query("SELECT * FROM plans ORDER BY date")
    fun getAllPlans(): Flow<List<Plan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plan: Plan)

    @Update
    suspend fun update(plan: Plan)

    @Delete
    suspend fun delete(plan: Plan)
}