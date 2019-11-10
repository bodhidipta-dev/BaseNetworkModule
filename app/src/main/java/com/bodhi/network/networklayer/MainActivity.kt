package com.bodhi.network.networklayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        RetrofitClient(
            baseurl = "https://samples.openweathermap.org/",
            networkBuilder = NetworkBuilder(
                shouldUseInterceptor = true,
                retryPolicy = true,
                timeoutInMillis = 2000,
                cachePolicy = true
            )
        ) {
            it.baseUrl()
            it.create(WeatherApi::class.java).gunSimpleGet(
                "London,uk",
                "b6907d289e10d714a6e88b30761fae22"
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.i("${it?.string()}")
                },
                    {
                        Timber.i("Error :${it.message.toString()}")
                    })
        }
    }

    interface WeatherApi {
        @GET("/data/2.5/weather")
        fun gunSimpleGet(
            @Query("q") place: String,
            @Query("appid") apikey: String
        ): Observable<ResponseBody>
    }
}
