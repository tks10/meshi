package com.takashi.meshi.api

import com.takashi.meshi.model.Id
import com.takashi.meshi.model.Meshi
import com.takashi.meshi.model.MeshiUploader
import com.takashi.meshi.model.TestContainer
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*


interface ApiService {
    // Test
    @POST("test/")
    fun test(@Body id: Id): Deferred<TestContainer>

    @GET("test/")
    fun test(): Deferred<Any>

    // Meshi
    @GET("api/example/")
    fun getMeshi(@Path("id") id: String): Deferred<List<Meshi>>

    // Auth
    @POST("https://kdc93adogk.execute-api.us-east-1.amazonaws.com/beta/meshi/")
    fun registMeshi(@Body meshiUploader: MeshiUploader): Deferred<Any>

}
