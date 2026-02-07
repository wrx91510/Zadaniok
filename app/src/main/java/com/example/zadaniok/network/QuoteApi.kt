package com.example.zadaniok.network

import retrofit2.http.GET

interface QuoteApi {
    @GET("api/random")
    suspend fun getRandomQuote(): List<QuoteResponse>
}
