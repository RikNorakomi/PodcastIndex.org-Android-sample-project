package com.example.podcastindexapiexample

import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


const val PODCAST_INDEX_BASE_URL = "https://api.podcastindex.org/"

class PodcastIndexAuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val apiHeaderTime = getHeaderTime()
        val hashString = createHashString(BuildConfig.API_KEY, BuildConfig.API_SECRET, apiHeaderTime)

        val request = chain.request().newBuilder()
            .addHeader("X-Auth-Date", apiHeaderTime)
            .addHeader("X-Auth-Key", BuildConfig.API_KEY)
            .addHeader("Authorization", "$hashString")
            .addHeader("User-Agent", "SuperPodcastPlayer/1.8")
            .build()

        return chain.proceed(request)
    }

    /**
     * Create a SHA1 hash string by combining apiKey + apiSecret + apiHeaderTime
     */
    private fun createHashString(apiKey: String, apiSecret: String, apiHeaderTime: String): String? {
        // Hash them to get the Authorization token
        val data4Hash = apiKey + apiSecret + apiHeaderTime
        val hashString: String? = createSHA1(data4Hash)
        println("hashString=$hashString")

        return hashString
    }

    private fun getHeaderTime(): String {
        // prep for crypto
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            .apply {
                clear()
                time = Date()
            }
        val secondsSinceEpoch = calendar.timeInMillis / 1000L
        val apiHeaderTime = "" + secondsSinceEpoch

        println("secs=$secondsSinceEpoch")
        return apiHeaderTime
    }

    private fun createSHA1(clearString: String): String? {
        return try {
            val messageDigest = MessageDigest.getInstance("SHA-1")
            messageDigest.update(clearString.toByteArray(charset("UTF-8")))
            byteArrayToString(messageDigest.digest())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            null
        }
    }

    private fun byteArrayToString(bytes: ByteArray): String {
        val buffer = StringBuilder()
        for (b in bytes) buffer.append(String.format(Locale.getDefault(), "%02x", b))
        return buffer.toString()
    }
}