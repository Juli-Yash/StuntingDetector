package com.aplikasistunting.model

data class DetectionResponse(
    val response: String,
    val status_underweight: String,
    val status_stunting: String,
    val status_wasting: String
)