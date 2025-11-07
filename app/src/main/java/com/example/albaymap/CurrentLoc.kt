package com.example.albaymap

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.Manifest
import android.view.ScaleGestureDetector
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class CurrentLoc : AppCompatActivity() {

    private var mapView: MapView? = null
    private var fusedLocationProvider: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_loc)
        mapView = findViewById(R.id.mapView)

        mapView!!.setTileSource(TileSourceFactory.MAPNIK)
        mapView!!.setMultiTouchControls(true)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()


    }

    private fun getCurrentLocation(){

        val task = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101)
        } else {
            val task = fusedLocationProvider!!.getLastLocation()

            task.addOnSuccessListener ( object : OnSuccessListener<Location>{
                override fun onSuccess(location: Location?){
                    if(location!=null){
                        val latitude = location.latitude
                        val longitude = location.longitude

                        val currentLocation = GeoPoint(latitude, longitude)

                        mapView!!.controller.setZoom(12.0)
                        mapView!!.controller.setCenter(currentLocation)

                        val marker = Marker(mapView)
                        marker.position = currentLocation
                        marker.title = "You are here"
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        mapView!!.overlays.add(marker)
                    }
                    else{
                        Toast.makeText(this@CurrentLoc, "Could not fetch location",
                            Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

    }

}