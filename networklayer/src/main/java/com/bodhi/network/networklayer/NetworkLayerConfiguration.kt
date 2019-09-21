package com.bodhi.network.networklayer

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


class NetworkLayerConfiguration(val httpconfig: HTTPConfiguration) {
    private var okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        retryOnConnectionFailure(httpconfig.getNetworkBuilder().retryPolicy)
        connectTimeout(httpconfig.getNetworkBuilder().timeoutInMillis, TimeUnit.MILLISECONDS)
        addNetworkInterceptor(NetworkInterceptor())
        authenticator(TokenAuthenticator())
        addInterceptor(logging)
    }.build().also {
        httpconfig.buildOkHttpClient(it)
    }

    private inner class NetworkInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request()
            Timber.i("Request headers:%s", requestBuilder.headers.toString())
            val builderForAddHeader = requestBuilder.newBuilder()
            if (httpconfig.getNetworkBuilder().requestHeaders.isNotEmpty()) {
                httpconfig.getNetworkBuilder().requestHeaders.forEach {
                    Timber.i("++ Adding Request headers: %s", it.toString())
                    builderForAddHeader.addHeader(it.key, it.value)
                    /* For add Any pre validation key, token, auth-token etc */
                }
            }
            val responseRaw = chain.proceed(builderForAddHeader.build())
            val responseBuilder = responseRaw.newBuilder()
            val responsebodyString =
                responseRaw.body?.source()?.buffer?.clone()?.readString(Charset.forName("UTF-8"))

            Timber.i("Response: %s", responsebodyString)
            if (httpconfig.getNetworkBuilder().cachePolicy) {
                val cacheControl = CacheControl.Builder()
                    .maxAge(15, TimeUnit.MINUTES) // 15 minutes cache
                    .build()
                responseBuilder
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .addHeader("Cache-Control", cacheControl.toString())
            }
            return httpconfig.getNetworkBuilder().interceptor(responseBuilder.build())
        }
    }

    private inner class TokenAuthenticator : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
            return httpconfig.getNetworkBuilder().authenticator(response)
        }
    }

    private var logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.tag("OkHttp").d(message)
        }
    })
}

data class NetworkBuilder(
    val shouldUseInterceptor: Boolean = true,
    val interceptor: (response: Response) -> Response = { it },
    val retryPolicy: Boolean = false,
    val timeoutInMillis: Long = 50000L,
    val cachePolicy: Boolean = false,
    val requestHeaders: Map<String, String> = mapOf(),
    val authenticator: (response: Response) -> Request? = { it.request.newBuilder().build() }
)