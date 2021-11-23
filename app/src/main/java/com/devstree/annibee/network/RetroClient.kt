package com.devstree.annibee.network

import com.devstree.annibee.BuildConfig
import com.devstree.annibee.utility.AppHelper
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetroClient {
    private var retrofit: Retrofit? = null
    private val interceptor: Interceptor
        get() {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY.takeIf { BuildConfig.DEBUG }
                ?: HttpLoggingInterceptor.Level.NONE)
            return logging
        }

    private val builder = OkHttpClient.Builder()
        .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)

    private val networkInterceptor: Interceptor
        get() {
            return Interceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                if (AppHelper.isLoggedIn()) requestBuilder.addHeader("Authorization","Bearer " + AppHelper.getAccessToken().toString())
                chain.proceed(requestBuilder.build())
            }
        }


    /*private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        if (AppHelper.isLogin()) {
            Log.d("${NetworkURL.HEADER_AUTHORIZATION}: ${AppHelper.getAuthToken()}")
            request.addHeader(NetworkURL.HEADER_AUTHORIZATION, AppHelper.getAuthToken())
        }
        chain.proceed(request.build())
    }*/

    private val client = builder.addInterceptor(networkInterceptor).addInterceptor(interceptor).build()

    private val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
//                    .baseUrl("http://174.129.238.169/annibee/")
                    .baseUrl("http://174.129.238.169/annibee/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    val apiService: ApiService = retrofitInstance.create(ApiService::class.java)

    /*
    * Firebase
    * */
//    val fireBaseApiService = fireBaseApiInstance.create(ApiService::class.java)
/*
    private val fireBaseApiInstance: Retrofit
        get() {
            builder.addInterceptor(interceptor)
            return Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }*/

}