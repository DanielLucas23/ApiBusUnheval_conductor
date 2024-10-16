package com.systemdk.apibusunheval_conductor.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.systemdk.apibusunheval_conductor.R
import com.systemdk.apibusunheval_conductor.databinding.ActivityMapBinding
import com.systemdk.apibusunheval_conductor.fragments.ModalBottonSheetMenu
import com.systemdk.apibusunheval_conductor.providers.AuthProvider
import com.systemdk.apibusunheval_conductor.providers.GeoProvider

class MapActivity : AppCompatActivity(), OnMapReadyCallback, Listener {

    //Variables
    private lateinit var binding: ActivityMapBinding
    private var googleMap: GoogleMap? = null
    private var easyWayLocation: EasyWayLocation?= null
    private var myLocationLatLng: LatLng?= null
    private var markerConductor: Marker?= null
    private val geoProvider = GeoProvider()
    private val authProvider = AuthProvider()

    private val modalMenu = ModalBottonSheetMenu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = Priority.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 1f
        }

        easyWayLocation = EasyWayLocation(this,locationRequest, false, false, this)
        locationPermission.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        binding.btnConnect.setOnClickListener { connectConductor() }
        binding.btnDisconnect.setOnClickListener { disconnectConductor() }

        binding.imageViewMenu.setOnClickListener { showModalMenu() }
    }

    val locationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            when{
                permission.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ->{
                    Log.d("LOCALIZACIÓN", "Permiso concedido")
                    //easyWayLocation?.startLocation()
                    checkIfDriverIsConnected()
                }
                permission.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) ->{
                    Log.d("LOCALIZACIÓN", "Permiso concedido con limitación")
                   //easyWayLocation?.startLocation()
                    checkIfDriverIsConnected()

                }
                else -> {
                        Log.d("LOCALIZACIÓN", "Permiso no concedido")
                }
            }
        }

    }

    private fun showModalMenu(){
        modalMenu.show(supportFragmentManager, ModalBottonSheetMenu.TAG)
    }

    private fun checkIfDriverIsConnected(){
        geoProvider.getLocation(authProvider.getId()).addOnSuccessListener { document ->
            if (document.exists()){
                if (document.contains("l")){
                    connectConductor()
                }else{
                    showButtonConnect()
                }
            }else{
                showButtonConnect()
            }
        }
    }

    private fun saveLocation(){
        if (myLocationLatLng != null) {
            geoProvider.saveLocation(authProvider.getId(), myLocationLatLng!!)
        }
    }

    private fun disconnectConductor(){
        easyWayLocation?.endUpdates()
        if (myLocationLatLng != null){
            geoProvider.removeLocation(authProvider.getId())
            showButtonConnect()
        }
    }

    private fun connectConductor(){
        easyWayLocation?.endUpdates() //Otros hilos de ejecución
        easyWayLocation?.startLocation()
        showButtonDisconnect()

    }

    private fun showButtonConnect() {
        binding.btnDisconnect.visibility = View.GONE // Ocultando el botón de desconectarse
        binding.btnConnect.visibility = View.VISIBLE // Mostrando el botón de conectarse
    }

    private fun showButtonDisconnect() {
        binding.btnDisconnect.visibility = View.VISIBLE// Mostrando el botón de desconectarse
        binding.btnConnect.visibility = View.GONE // Ocultando el botón de conectarse
    }

    private fun addMarker() {
        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.icon_autobus)
        val markerIcon = getMarkerFromDrawable(drawable!!)
        if (markerConductor != null){
            markerConductor?.remove() //No redibujar el icono
        }

        if (myLocationLatLng != null){
            markerConductor = googleMap?.addMarker(
                MarkerOptions()
                    .position(myLocationLatLng!!)
                    .anchor(0.5f, 0.5f)
                    .flat(true)
                    .icon(markerIcon)
            )
        }
    }

    private fun getMarkerFromDrawable(drawable: Drawable): BitmapDescriptor{
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            70,
            150,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0,0,70, 150)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() { //Cierra la APP o pasamos a otra actividad
        super.onDestroy()
        easyWayLocation?.endUpdates()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        //easyWayLocation?.startLocation()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        googleMap?.isMyLocationEnabled = false
    }

    override fun locationOn() {

    }

    override fun currentLocation(location: Location) { //Actualización  de la posición en tiempo real
        myLocationLatLng = LatLng(location.latitude, location.longitude) //Latitud y Longitud de la Posición Actual

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
            CameraPosition.builder().target(myLocationLatLng!!).zoom(17f).build()
        ))
        addMarker()
        saveLocation()
    }

    override fun locationCancelled() {

    }


}