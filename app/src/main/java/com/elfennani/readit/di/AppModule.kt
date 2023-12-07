package com.elfennani.readit.di

import android.content.Context
import com.elfennani.readit.data.local.SessionDao
import com.elfennani.readit.data.remote.AuthApi
import com.elfennani.readit.data.remote.UserApi
import com.elfennani.readit.data.repository.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

  @Provides
  @Singleton
  fun provideAuthApi(): AuthApi {
    return Retrofit.Builder()
      .baseUrl("https://www.reddit.com")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(AuthApi::class.java)
  }

  @Provides
  @Singleton
  fun provideUserApi(): UserApi {
    return Retrofit.Builder()
      .baseUrl("https://oauth.reddit.com")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(UserApi::class.java)
  }

  @Provides
  @Singleton
  fun provideSessionManager(
    @ApplicationContext context: Context,
    authApi: AuthApi,
    sessionDao: SessionDao,
    userApi: UserApi
  ): SessionManager {
    return SessionManager(
      sessionDao = sessionDao,
      authApi = authApi,
      userApi = userApi,
      context = context
    )
  }
}
