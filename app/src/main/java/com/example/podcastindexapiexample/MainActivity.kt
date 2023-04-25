package com.example.podcastindexapiexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(PodcastIndexAuthInterceptor())
            .build()

        val podcastIndexService = Retrofit.Builder()
            .baseUrl(PODCAST_INDEX_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PodcastIndexService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = podcastIndexService.getTrendingPodcasts()
                val inputStream = response.byteStream()
                val reader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (withContext(Dispatchers.IO) {
                        reader.readLine()
                    }.also { line = it } != null) {
                    stringBuilder.append(line)
                }
                val result = stringBuilder.toString()
                Log.e("API RESPONSE", "result= $result")

            } catch (e: Exception) {
                // Handle the exception
                throw Exception("Exception: $e")
            }
        }
    }
}