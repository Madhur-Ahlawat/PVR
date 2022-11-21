package com.net.pvr1.di

import com.net.pvr1.BuildConfig
import com.net.pvr1.api.AuthInterceptor
import com.net.pvr1.api.UserAPI
import com.net.pvr1.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
//            .baseUrl("https://api1.pvrcinemas.com/PVRCinemasCMS/api/")
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI {
        val client = OkHttpClient.Builder()
        client.readTimeout(600, TimeUnit.SECONDS)
        client.connectTimeout(600, TimeUnit.SECONDS)
        client.addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        })

        val logging = HttpLoggingInterceptor()

        // if (BuildConfig.DEBUG) {
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }else{
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        client.addInterceptor(logging)

        return retrofitBuilder
            .client(client.build())
            .build()
            .create(UserAPI::class.java)
    }

}