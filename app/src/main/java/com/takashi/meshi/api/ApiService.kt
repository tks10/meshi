package com.takashi.meshi.api

import com.takashi.meshi.model.Meshi
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*


interface ApiService {
    // Meshi
    @GET("api/example/")
    fun getMeshi(@Path("id") id: String): Deferred<List<Meshi>>

    // Auth
    @POST("api/example/")
    fun registMeshi(@Body meshi: Meshi): Deferred<Any>

}
