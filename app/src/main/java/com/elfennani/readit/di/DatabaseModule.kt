package com.elfennani.readit.di

import android.content.Context
import com.elfennani.readit.data.local.AppDatabase
import com.elfennani.readit.data.local.SessionDao
import com.elfennani.readit.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideUsersDao(appDatabase: AppDatabase) : UserDao {
        return appDatabase.usersDao()
    }

    @Provides
    fun provideSessionDao(appDatabase: AppDatabase) : SessionDao {
        return appDatabase.sessionDao()
    }

}