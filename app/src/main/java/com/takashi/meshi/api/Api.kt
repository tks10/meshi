package com.takashi.meshi.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.takashi.meshi.BuildConfig
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
                    if (Token.get() != null){
                        header("Authorization", "JWT ${Token.get()!!.token}")
                    }
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
        suspend fun signIn(email: String, password: String): Token {
            return apiService.signIn(SignIn(email, password)).await()
        }

        suspend fun signInWithUuid(uuid: String): Token {
            return apiService.signInWithUuid(Uuid(uuid)).await()
        }

        suspend fun signUpWithUuid(uuid: String): Token {
            return apiService.signUpWithUuid(Uuid(uuid)).await()
        }

        // TalkRoom
        suspend fun getTalkRoomList(): TalkRoomContainer {
            return apiService.getTalkRooms().await()
        }

        suspend fun getTalkRoom(roomId: String): TalkRoom {
            return apiService.getTalkRoom(roomId).await()
        }

        suspend fun createTalkRoom(roomId: String, iconBase64: String): TalkRoom {
            return apiService.createTalkRoom(TalkRoomCreator(roomId, iconBase64)).await()
        }

        suspend fun joinToTalkRoom(roomId: String): TalkRoom {
            return apiService.joinToTalkRoom(roomId).await()
        }

        suspend fun getMessages(roomId: String): MessageContainer {
            return apiService.getMessages(roomId).await()
        }

        // User
        suspend fun getUser(): User {
            return apiService.getUser().await()
        }

        suspend fun updateUser(name: String, iconBase64: String): User {
            return apiService.updateUser(UserUpdater(name, iconBase64)).await()
        }

    }
}