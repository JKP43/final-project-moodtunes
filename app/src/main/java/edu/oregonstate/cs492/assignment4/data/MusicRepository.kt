package edu.oregonstate.cs492.assignment4.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

/**
 * This class manages data operations associated with the Jamendo tracks api endpoint.
 */
class MusicRepository (
    private val service: MusicService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /*
     * These three properties are used to implement a basic caching strategy, where an API call
     * is only executed if the requested tags don't match the ones from the previous
     * API call.
     */
    private var currentTags: String? = null
    private var cachedMusic: MusicForecast? = null

    /*
     * These values are used to help measure the age of the cached forecast.  See the Kotlin
     * documentation on time measurement for details:
     *
     * https://kotlinlang.org/docs/time-measurement.html
     */
    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    /**
     * This method executes a new query to the OpenWeather API's 5-day/3-hour forecast method.  It
     * is a suspending function and executes within the coroutine context specified by the
     * `dispatcher` argument to the Repository class's constructor.
     *
     * @param clientId Specifies the id of the user.
     * @param tags Specifies the tags of songs that should be returned by the api.
     *
     * @return Returns a Kotlin Result object wrapping the [MusicForecast] object that
     *   represents the fetched forecast.  If the API query is unsuccessful for some reason, the
     *   Exception associated with the Result object will provide more info about why the query
     *   failed.
     */
    suspend fun loadMusic(
        clientId: String,
        tags: String?,
        limit: Int?,
    ) : Result<MusicForecast?> {
        /*
         * If we can do so, return the cached forecast without making a network call.  Otherwise,
         * make an API call to fetch the forecast and cache it.
         */
        return if (shouldFetch(tags)) {
            withContext(ioDispatcher) {
                try {
                    val response = service.loadMusic(clientId, tags, limit)
                    if (response.isSuccessful) {
                        cachedMusic = response.body()
                        Log.d("MusicRepository", "Response: $cachedMusic")
                        timeStamp = timeSource.markNow()
                        currentTags = tags
                        Result.success(cachedMusic)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Log.e("MusicRepository", "Exception: $e")
                    Result.failure(e)

                }
            }
        } else {
            Result.success(cachedMusic!!)
        }
    }

    /**
     * Determines whether the forecast should be fetched by making a new HTTP call or whether
     * the cached forecast can be returned.  The cached forecast should be used if the requested
     * location and units match the ones corresponding to the cached forecast and the cached
     * forecast is not stale.
     *
     * @param tags The tags for which type of music is to be potentially fetched, as passed
     *   to `loadMusic()`.
     *
     * @return Returns true if the forecast should be fetched and false if the cached version
     *   should be used.
     */
    private fun shouldFetch(tags: String?): Boolean =
        cachedMusic == null
        || tags != currentTags
        || (timeStamp + cacheMaxAge).hasPassedNow()
}