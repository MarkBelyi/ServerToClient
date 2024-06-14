package com.example.server.Manager

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.server.Database.LogDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideLogDatabase(context: Context): LogDatabase {
        return Room.databaseBuilder(
            context,
            LogDatabase::class.java,
            "log_database"
        ).build()
    }
}