package com.github.novotnyr.presentr

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// pripravime si interface pre REST API
interface PresentrApi {

    @POST("available-users/{login}")
    suspend fun login(@Path("login") login: String)

    @GET("available-users")
    suspend fun getUsers(): List<User>
}

private const val BASE_URL = "https://ics.upjs.sk/~novotnyr/android/demo/presentr/index.php/"

val presentr: PresentrApi by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PresentrApi::class.java)
}