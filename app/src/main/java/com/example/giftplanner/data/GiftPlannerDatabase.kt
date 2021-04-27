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
import java.time.LocalDateTime
import java.time.ZoneId
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
                recipientDao.insert(
                    Recipient(
                        name = "Папа"
                    ))
                recipientDao.insert(
                    Recipient(
                        name = "Мама"
                    ))
                recipientDao.insert(
                    Recipient(
                        name = "Сестра"
                    ))
                recipientDao.insert(
                    Recipient(
                        name = "Парень"
                    ))
                recipientDao.insert(
                    Recipient(
                        name = "Бабушка"
                    ))
                presentDao.insert(
                    Present(
                        name = "Бритва",
                        cost = 800.0
                    ))
                presentDao.insert(
                    Present(
                        name = "Косметика",
                        cost = 1500.0
                    ))
                presentDao.insert(
                    Present(
                        name = "Сковорода",
                        cost = 1000.0
                    ))
                presentDao.insert(
                    Present(
                        name = "Цепочка",
                        cost = 2000.0
                    ))
                presentDao.insert(
                    Present(
                        name = "Компьютерная игра",
                        cost = 1799.99
                    ))
                presentDao.insert(
                    Present(
                        name = "Кухонный нож",
                        cost = 3500.0
                    ))
                presentDao.insert(
                    Present(
                        name = "Мультиварка",
                        cost = 4500.0
                    ))
                presentDao.insert(
                    Present(
                        name = "Телефон",
                        cost = 10000.0
                    ))
                planDao.insert(
                    Plan(
                        date = LocalDateTime.of(
                            2021,
                            5,
                            3,
                            0,
                            0,
                            0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        holidayName = "День рождение мамы",
                        recipient_id = 2,
                        present_id = 2
                    ))
                planDao.insert(
                    Plan(
                        date = LocalDateTime.of(
                            2021,
                            6,
                            12,
                            0,
                            0,
                            0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        holidayName = "День России",
                        recipient_id = 1,
                        present_id = 1
                    ))
                planDao.insert(
                    Plan(
                        date = LocalDateTime.of(
                            2021,
                            9,
                            1,
                            0,
                            0,
                            0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        holidayName = "Первое сентября",
                        recipient_id = 3,
                        present_id = 8
                    ))
            }
        }
    }
}