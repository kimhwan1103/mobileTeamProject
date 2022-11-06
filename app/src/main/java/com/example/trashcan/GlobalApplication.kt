package com.example.trashcan

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class GlobalApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("키 입력")
    }
}