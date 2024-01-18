package com.mielechm.nowplaying.di

import android.content.Context
import androidx.room.Room
import com.mielechm.nowplaying.data.FavoritesDao
import com.mielechm.nowplaying.data.FavoritesDatabase
import com.mielechm.nowplaying.data.remote.MoviesApi
import com.mielechm.nowplaying.repository.DefaultMoviesRepository
import com.mielechm.nowplaying.utils.API_TOKEN
import com.mielechm.nowplaying.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoviesApi(): MoviesApi {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val request =
                chain.request().newBuilder().header("Authorization", "Bearer $API_TOKEN").build()
            chain.proceed(request)
        }.addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideFavoriteDb(@ApplicationContext context: Context): FavoritesDatabase {
        return Room.databaseBuilder(
            context,
            FavoritesDatabase::class.java,
            FavoritesDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(favoritesDatabase: FavoritesDatabase): FavoritesDao =
        favoritesDatabase.favoritesDao()

    @Singleton
    @Provides
    fun provideMoviesRepository(api: MoviesApi, dao: FavoritesDao) = DefaultMoviesRepository(api, dao)
}