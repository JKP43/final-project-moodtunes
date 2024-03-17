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
    /**
     * This method is used to query the OpenWeather API's 5-day/3-hour forecast method:
     * https://openweathermap.org/forecast5.  This is a suspending function, so it must be called
     * in a coroutine or within another suspending function.
     *
     * @param location Specifies the location for which to fetch forecast data.  For US cities,
     *   this should be specified as "<city>,<state>,<country>" (e.g. "Corvallis,OR,US"), while
     *   for international cities, it should be specified as "<city>,<country>" (e.g. "London,GB").
     * @param units Specifies the type of units that should be returned by the OpenWeather API.
     *   Can be one of: "standard", "metric", and "imperial".
     * @param apiKey Should be a valid OpenWeather API key.
     *
     * @return Returns a Retrofit `Response<>` object that will contain a [FiveDayForecast] object
     *   if the API call was successful.
     */
    @GET("forecast")
    suspend fun loadMusic(
        @Query("client_id") clientId: String,
        @Query("tags") tags: String?
    ) : Response<MusicForecast>

    companion object {
        private const val BASE_URL = "https://api.jamendo.com/v3.0/tracks/"

        /**
         * This method can be called as `OpenWeatherService.create()` to create an object
         * implementing the OpenWeatherService interface and which can be used to make calls to
         * the OpenWeather API.
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