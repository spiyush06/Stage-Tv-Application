package com.stage.tv

import android.app.Application
import com.stage.tv.api.ApiService
import com.stage.tv.api.RetrofitHelper
import com.stage.tv.api.TmdbRepo

class MyApplication : Application() {

    lateinit var tmdbRepo : TmdbRepo

    override fun onCreate() {
        super.onCreate()

        init()
    }

    private fun init(){
        val service = RetrofitHelper.getInstance().create(ApiService::class.java)
        tmdbRepo = TmdbRepo(service)
    }
}