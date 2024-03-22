package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This is a Retrofit service interface encapsulating communication with the OpenWeather API.
 */
interface MusicService {
    @GET("tracks")
    suspend fun loadMusic(
        @Query("client_id") clientId: String,
        @Query("tags") tags: String?,
        @Query("limit") limit: Int?,
    ) : Response<MusicForecast>

    companion object {
        private const val BASE_URL = "https://api.jamendo.com/v3.0/"

        /**
         * This method can be called as `MusicService.create()` to create an object
         * implementing the MusicService interface and which can be used to make calls to
         * the Jamendo API.
         */
        fun create() : MusicService {
            val moshi = Moshi.Builder()
                .add(MusicJsonAdapter())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MusicService::class.java)
        }
    }
}