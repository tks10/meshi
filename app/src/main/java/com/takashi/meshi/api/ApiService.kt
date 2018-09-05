package com.takashi.meshi.api

import com.takashi.meshi.model.*
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*


interface ApiService {
    // Test
    @POST("test/")
    fun test(@Body id: Id): Deferred<TestContainer>

    @GET("test/")
    fun test(): Deferred<Any>

    // Meshi
    @GET("meshi/")
    fun getMeshi(@Query("id") id: String): Deferred<MeshiContainer>

    // Auth
    @POST("meshi/")
    fun registMeshi(@Body meshiUploader: MeshiUploader): Deferred<Any>
}
