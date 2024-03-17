package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This class is used to help parse the JSON forecast data returned by the OpenWeather API's
 * 5-day/3-hour forecast.
 */
@JsonClass(generateAdapter = true)
data class MusicForecast(
    @Json(name = "list") val songs: List<MusicFormat>
)