package com.example.weathers.data.source.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

const val TAG_INTERCEPTOR = "Interceptor"

class MyLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        Log.i(TAG_INTERCEPTOR, """
            Sending request >>>
                ${request.url}
                ${chain.connection()}
                ${request.headers}
        """.trimIndent())

        Log.i(TAG_INTERCEPTOR, "\n")
        Log.i(TAG_INTERCEPTOR, """
            Received response >>>
                ${response.request.url}
                ${response.headers}
                ${response.body}
        """.trimIndent())

        return response
    }
}