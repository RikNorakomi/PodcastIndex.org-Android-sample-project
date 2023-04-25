package com.example.podcastindexapiexample

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * For detail api documentation see:
 * https://podcastindex-org.github.io/docs-api/
 */
interface PodcastIndexService {

    @GET("/api/1.0/podcasts/trending")
    suspend fun getTrendingPodcasts(
        @Query("cat") category: String = "", // f.e. News,Religion,65
        @Query("notcat") excludeCategory: String = "", // f.e. News,Religion,65
        @Query("lang") language: String = "en", // You can specify multiple languages by separating them with commas. If you also want to return episodes that have no language given, use the token "unknown". (ex. en,es,ja,unknown).
        @Query("max") maxResults: Int = 15,
        @Query("since") since: Int = 0, // Return items since the specified time. The value can be a unix epoch timestamp or a negative integer that represents a number of seconds prior to right now.
    ): ResponseBody
}