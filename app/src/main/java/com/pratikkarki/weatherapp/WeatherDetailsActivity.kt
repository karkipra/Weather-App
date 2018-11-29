package com.pratikkarki.weatherapp

import android.graphics.drawable.Drawable
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.pratikkarki.weatherapp.adapter.TodoAdapter.Companion.CITY_NAME
import com.pratikkarki.weatherapp.data.WeatherResult
import com.pratikkarki.weatherapp.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_weather_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherDetailsActivity : AppCompatActivity() {

    private val HOST_URL = "https://api.openweathermap.org/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        showDetails(intent.getStringExtra(CITY_NAME))
    }

    fun showDetails(city: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val weatherAPI = retrofit.create(WeatherAPI::class.java)
        val weatherCall =
                weatherAPI.getWeather(city,
                        getString(R.string.unit),
                        getString(R.string.api_key))

        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                imIcon.setImageResource(R.drawable.error)
                tvCityName.text = getString(R.string.cannot_connect)

            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val cityResult = response.body()

                if(cityResult?.name != null){
                    tvCityName.text =  cityResult?.name?.toUpperCase()
                    tvTemperature.text = getString(R.string.temp) + " " + cityResult?.main?.temp.toString() + getString(R.string.temp_unit)
                    tvHumidity.text = getString(R.string.humidity) + " " +cityResult?.main?.humidity.toString()+ getString(R.string.hum_unit)
                    tvDescription.text =  cityResult?.weather?.get(0)?.description?.capitalize()
                    tvWind.text = getString(R.string.wind) + " " + cityResult?.wind?.speed.toString() + " " + getString(R.string.wind_unit)
                    Glide.with(this@WeatherDetailsActivity)
                            .load(
                                    ("https://openweathermap.org/img/w/"
                                            + response.body()?.weather?.get(0)?.icon + getString(R.string.file_type)))
                            .into(imIcon)
                } else {
                    imIcon.setImageResource(R.drawable.error)
                    tvCityName.text = getString(R.string.no_city_error)
                }

            }

        })


    }
}

