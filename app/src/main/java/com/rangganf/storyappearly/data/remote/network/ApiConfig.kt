package com.rangganf.storyappearly.data.remote.network

import android.content.Context
import com.rangganf.storyappearly.BuildConfig
import com.rangganf.storyappearly.utils.Preference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {

        // Fungsi ini digunakan untuk mendapatkan OkHttpClient dengan interceptor sesuai kebutuhan
        private fun getInterceptor(token: String?): OkHttpClient {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            return OkHttpClient.Builder()
                .apply {
                    // AuthInterceptor jika token tidak kosong
                    if (!token.isNullOrEmpty()) {
                        addInterceptor(AuthInterceptor(token))
                    }
                    // loggingInterceptor untuk melihat log request dan response
                    addInterceptor(loggingInterceptor)
                }
                .build()
        }

        // Fungsi ini digunakan untuk mendapatkan instance ApiService
        fun getApiService(context: Context): ApiService {
            // Inisialisasi SharedPreferences untuk mendapatkan token
            val sharedPref = Preference.initPref(context, "onSignIn")
            val token = sharedPref.getString("token", null).toString()

            // Konfigurasi Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getInterceptor(token))
                .build()

            // Mengembalikan instance ApiService yang telah dikonfigurasi
            return retrofit.create(ApiService::class.java)
        }
    }
}