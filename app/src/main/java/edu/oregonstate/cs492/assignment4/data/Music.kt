package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson



data class MusicFormat(
    val artist: String,
    val songTitle: String,
    val shortUrl: String,
    val shareUrl: String,
    val songImage: String,
    val duration: Int
)

/**
 * This class represents the Track data from the api response.
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
data class MusicJson(
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
