package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

///**
// * This class encapsulates data about the city fetched from the OpenWeather API's 5-day/3-hour
// * forecast.  It does not directly correspond to the JSON data.  The classes below are used for
// * JSON parsing, and information from them is used by the custom JSON adapter at the bottom of
// * this file to construct this class.
// */
//data class Music(
//    val name: String,
//    val lat: Double,
//    val lon: Double,
//    val tzOffsetSec: Int
//)

/* ******************************************************************************************
 * Below is a set of classes used to parse the JSON response from the OpenWeather API into
 * a ForecastCity object.  The first two classes are designed to match the structure of the
 * the `city` field in the OpenWeather 5-day forecast API's JSON response.  The last is a
 * custom type adapter that can be used with Moshi to parse OpenWeather JSON directly into
 * a ForecastCity object.
 * ******************************************************************************************/


data class MusicFormat(
    val artist: String,
    val songTitle: String,
    val shortUrl: String,
    val shareUrl: String,
    val songImage: String,
    val duration: Int
)

/**
 * This class represents the `city` field of the JSON response from the OpenWeather API.
 */
@JsonClass(generateAdapter = true)
data class Track(
    val id: String,
    val name: String,
    val duration: Int,
    @Json(name = "artist_id") val artistId: String,
    @Json(name = "artist_name") val artistName: String,
    @Json(name = "artist_idstr") val artistIdStr: String,
    @Json(name = "album_name") val albumName: String,
    @Json(name = "album_id") val albumId: String,
    @Json(name = "license_ccurl") val licenseCcurl: String,
    val position: Int,
    val releaseDate: String,
    @Json(name = "album_image") val albumImage: String,
    val audio: String,
    @Json(name = "audiodownload") val audioDownload: String,
    @Json(name = "prourl") val proUrl: String,
    @Json(name = "shorturl") val shortUrl: String,
    @Json(name = "shareurl") val shareUrl: String,
    val waveform: String,
    val image: String,
    @Json(name = "audiodownload_allowed") val audioDownloadAllowed: Boolean
)

@JsonClass(generateAdapter = true)
data class Headers(
    val status: String,
    val code: Int,
    @Json(name = "error_message") val errorMessage: String,
    val warnings: String,
    @Json(name = "results_count") val resultsCount: Int,
    val next: String
)

@JsonClass(generateAdapter = true)
data class MusicJson(
    @Json(name = "headers") val headers: Headers,
    @Json(name = "results") val results: MusicResults
)

@JsonClass(generateAdapter = true)
data class MusicResults(
    @Json(name = "tracks") val tracks: List<Track>
)


class MusicJsonAdapter {
    @FromJson
    fun musicFormatFromJson(list: Track) = MusicFormat(
        artist = list.artistName,
        songTitle = list.name,
        shortUrl = list.shortUrl,
        shareUrl = list.shareUrl,
        songImage = list.image,
        duration = list.duration
    )

    @ToJson
    fun musicFormatToJson(musicFormat: MusicFormat): String {
        throw UnsupportedOperationException("encoding MusicFormat to JSON is not supported")
    }
}
