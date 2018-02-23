package net.marcorighini.spotifyreleases.misc.utils

import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


inline fun <reified T> createService(debug: Boolean, baseUrl: HttpUrl): T {
    val client = OkHttpClient.Builder()

    if (debug) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(loggingInterceptor)
    }

    return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client.build())
            .build()
            .create(T::class.java)
}