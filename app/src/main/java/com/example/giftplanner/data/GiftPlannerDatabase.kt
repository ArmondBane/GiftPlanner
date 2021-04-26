package com.example.giftplanner.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.data.Entity.Recipient
import com.example.giftplanner.data.dao.PlanDao
import com.example.giftplanner.data.dao.PresentDao
import com.example.giftplanner.data.dao.RecipientDao
import com.example.giftplanner.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Plan::class, Recipient::class, Present::class], version = 1)
abstract class GiftPlannerDatabase : RoomDatabase() {

    abstract fun planDao(): PlanDao
    abstract fun presentDao(): PresentDao
    abstract fun recipientDao(): RecipientDao

    class Callback @Inject constructor(
        private val database: Provider<GiftPlannerDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
    ) : RoomDatabase.Callback() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val planDao = database.get().planDao()
            val presentDao = database.get().presentDao()
            val recipientDao = database.get().recipientDao()

            applicationScope.launch {

            }
        }
    }
}