package com.aplikasistunting.network

import com.aplikasistunting.model.DetectionRequest
import com.aplikasistunting.model.DetectionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("webhook/post-data")
    fun detectStunting(@Body request: DetectionRequest): Call<DetectionResponse>
}