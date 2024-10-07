package com.paul.weatherapp.dependencyinjection

import android.content.Context
import com.paul.weatherapp.data.api.AppIdInterceptor
import com.paul.weatherapp.data.api.EndpointConstants
import com.paul.weatherapp.data.api.OpenWeatherMapApi
import com.paul.weatherapp.data.api.isNetworkAvailable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherAppModule {



    @Provides
    @Singleton
    fun provideOpenWeatherMapApi(builder: Retrofit.Builder): OpenWeatherMapApi {
        return builder.build().create(OpenWeatherMapApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024L
        return Cache(File(context.cacheDir, "http_cache"), cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, @ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(AppIdInterceptor())
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                val maxAge = 60 * 60 // 1 hour
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build()
            }
            .addInterceptor { chain ->
                var request = chain.request()
                if (!isNetworkAvailable(context)) {
                    val maxStale = 60 * 60 * 24 // 1 day
                    request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
                chain.proceed(request)
            }
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(EndpointConstants.BASE_URL)
            .client(okHttpClient) // Use the OkHttpClient with cache
            .addConverterFactory(GsonConverterFactory.create())
    }
}