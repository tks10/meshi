package com.takashi.meshi.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.takashi.meshi.BuildConfig
import com.takashi.meshi.model.Meshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class Api {
    companion object {
        private val apiService: ApiService by lazy { createService() }

        private fun createService(): ApiService {
            val apiUrl = BuildConfig.IP_API
            val client = builderHttpClient() // OkHttpClient に logging の設定を追加
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(client)
                    .build()
            return retrofit.create(ApiService::class.java)
        }

        private fun builderHttpClient(): OkHttpClient {
            val client = OkHttpClient.Builder()
            client.addInterceptor { chain ->
                val original = chain.request()
                val request = with(original.newBuilder()){
                    method(original.method(), original.body())
                }.build()
                chain.proceed(request)
            }

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logging)
            }

            return client.build()
        }

        // Auth
        suspend fun getMeshi(id: String): List<Meshi> {
            return apiService.getMeshi(id).await()
        }

        suspend fun registMeshi(meshi: Meshi): Any {
            return apiService.registMeshi(meshi).await()
        }
    }
}
