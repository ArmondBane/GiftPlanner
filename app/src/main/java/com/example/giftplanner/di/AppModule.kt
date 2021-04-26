package com.example.giftplanner.di

import android.app.Application
import androidx.room.Room
import com.example.giftplanner.data.GiftPlannerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application, callback: GiftPlannerDatabase.Callback)
            = Room.databaseBuilder(app, GiftPlannerDatabase::class.java, "gift_planner_database")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun providePlanDao(db: GiftPlannerDatabase) = db.planDao()

    @Provides
    fun providePresentDao(db: GiftPlannerDatabase) = db.presentDao()

    @Provides
    fun provideRecipientDao(db: GiftPlannerDatabase) = db.recipientDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope