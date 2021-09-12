package com.example.myapplication

import android.app.Application
import com.mapbox.mapboxsdk.MapmyIndia
import com.mmi.services.account.MapmyIndiaAccountManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapmyIndiaAccountManager.getInstance().restAPIKey = "bbc5dd2d6e760d0d12ca59c4e9a302ea"
        MapmyIndiaAccountManager.getInstance().mapSDKKey = "bbc5dd2d6e760d0d12ca59c4e9a302ea"
        // MapmyIndiaAccountManager.getInstance().atlasGrantType = getAtlasGrantType()
        MapmyIndiaAccountManager.getInstance().atlasClientId =
            "33OkryzDZsLfJKVFpYWFOKlx0OQF6vjih4ZI4egAIsqzSysqR2yvgUSKBnzy9CwOTXplL20Us6Nh-eQht9oM7g=="
        MapmyIndiaAccountManager.getInstance().atlasClientSecret =
            "lrFxI-iSEg_GHeovW2hvkutHsQA-bTVnZfd5QesA1xOb8SzIXX3hthyDRa7C8ImYJTLgSpAXyaOwwK1O8CZZFFamRZ8Rjcf8"
        MapmyIndia.getInstance(applicationContext)

    }
}