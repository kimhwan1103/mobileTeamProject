package com.example.trashcan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.Location
import android.os.Build
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : AppCompatActivity() {

    //권한 가져오기
    var permission = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)

    private lateinit var naverMap: NaverMap
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: com.google.android.gms.location.LocationRequest

    private val REQUEST_PERMISSION_LOCATION = 10

    lateinit var button: Button
    lateinit var text1: TextView
    lateinit var text2: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        button = findViewById(R.id.locationBtn)
        text1 = findViewById(R.id.text1)
        text2 = findViewById(R.id.text2)

        mLocationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        button.setOnClickListener {
            if(checkPermissionForLocation(this)) {
                startLocationUpdate()
            }
        }
        /*
        val fm = supportFragmentManager

        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)
        */
    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    private fun startLocationUpdate() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
        }
        mFusedLocationProviderClient !!.requestLocationUpdates(mLocationRequest, mLocationCllback, Looper.myLooper())
    }

    private val mLocationCllback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location){
        mLastLocation = location
        text2.text = "위도 : " +mLastLocation.latitude
        text1.text = "경도 : " +mLastLocation.longitude
    }
    /*
    @UiThread
    override fun onMapReady(map: NaverMap) {
        this.naverMap = naverMap
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(mLastLocation.latitude, mLastLocation.longitude)).animate(CameraAnimation.Fly, 1000)
        naverMap.moveCamera(cameraUpdate)
        startLocationUpdate()

    }
    */
}