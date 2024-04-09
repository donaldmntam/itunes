package com.example.itunes.services

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.example.itunes.models.SongListResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SongListProviderImpl(
    private val queue: RequestQueue
) : SongListProvider {
    override suspend fun fetch(): Result<SongListResponse> {
        val url = "https://itunes.apple.com/search?term=Taylor+Swift&limit=200&media=music"
        return suspendCoroutine { continuation ->
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { responseString ->
                    val gson = Gson()
                    val result = try {
                        Result.success(gson.fromJson(responseString, SongListResponse::class.java))
                    } catch (e: JsonSyntaxException) {
                        Result.failure(e)
                    }
                    continuation.resume(result);
                },
                { error ->
                    continuation.resume(Result.failure(error))
                },
            )
            queue.add(stringRequest);
        }
    }
}