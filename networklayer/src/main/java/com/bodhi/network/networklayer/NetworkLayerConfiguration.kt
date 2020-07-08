package com.bodhi.network.networklayer

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
class NetworkLayerConfiguration(context: Context, httpConfig: HTTPConfiguration) {
    init {
        val okhttpClient = OkHttpClient.Builder().apply {
            retryOnConnectionFailure(httpConfig.getNetworkBuilder().retryPolicy)
            connectTimeout(httpConfig.getNetworkBuilder().timeoutInMillis, TimeUnit.MINUTES)
            writeTimeout(httpConfig.getNetworkBuilder().timeoutInMillis, TimeUnit.MINUTES)
            readTimeout(httpConfig.getNetworkBuilder().timeoutInMillis, TimeUnit.MINUTES)
            addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    return if (httpConfig.getNetworkBuilder().isMock) {
                        val returnString = loadJSONFromAsset(
                            context,
                            "mock/"+chain.request().url.encodedPathSegments.last()+".json"
                        )
                         Response.Builder()
                            .code(200)
                            .protocol(Protocol.HTTP_2)
                            .message(
                                returnString
                                    ?:
                                    " **************** !!!!!! No mock response found !!!! *************"
                            )
                            .request(chain.request())
                            .body(returnString?.toResponseBody("application/json".toMediaType()))
                            .addHeader("content-type", "application/json")
                            .build()
                    } else {

                        val requestBuilder = chain.request()
                        Timber.i("Request headers:%s", requestBuilder.headers.toString())
                        val builderForAddHeader = requestBuilder.newBuilder()
                        if (httpConfig.getNetworkBuilder().requestHeaders.isNotEmpty()) {
                            httpConfig.getNetworkBuilder().requestHeaders.forEach {
                                Timber.i("++ Adding Request headers: %s", it.toString())
                                builderForAddHeader.addHeader(it.key, it.value)
                                /* For add Any pre validation key, token, auth-token etc */
                            }
                        }
                        val responseRaw = chain.proceed(builderForAddHeader.build())
                        val responseBuilder = responseRaw.newBuilder()
                        val responsebodyString =
                            responseRaw.body?.source()?.buffer?.clone()
                                ?.readString(Charset.forName("UTF-8"))

                        Timber.i("Response: %s", responsebodyString)
                        if (httpConfig.getNetworkBuilder().cachePolicy) {
                            val cacheControl = CacheControl.Builder()
                                .maxAge(15, TimeUnit.MINUTES) // 15 minutes cache
                                .build()
                            responseBuilder
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .addHeader("Cache-Control", cacheControl.toString())
                        }
                        httpConfig.getNetworkBuilder().interceptor(responseBuilder.build())
                    }
                }
            })
            authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request? {
                    return httpConfig.getNetworkBuilder().authenticator(response)
                }
            })
            if (httpConfig.getNetworkBuilder().shouldUseInterceptor) {
                val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Timber.tag("OkHttp").d(message)
                    }
                })
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(interceptor)
            }
        }.build()
        httpConfig.buildOkHttpClient(okhttpClient)
    }

    private fun loadJSONFromAsset(context: Context, filename: String): String? {
        return try {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }
}

data class NetworkBuilder(
    val isMock: Boolean = false,
    val shouldUseInterceptor: Boolean = true,
    val interceptor: (response: Response) -> Response = { it },
    val retryPolicy: Boolean = true,
    val timeoutInMillis: Long = 10L,
    val cachePolicy: Boolean = false,
    val requestHeaders: Map<String, String> = mapOf(),
    val authenticator: (response: Response) -> Request? = { it.request.newBuilder().build() }
)
