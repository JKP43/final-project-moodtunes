package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This class is used to help parse the JSON son results from the api endpoint.
 */
@JsonClass(generateAdapter = true)
data class MusicForecast(
    @Json(name = "results") val songs: List<MusicFormat>
)