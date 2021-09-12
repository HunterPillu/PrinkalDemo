package com.example.myapplication.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.example.myapplication.network.MainRepository
import com.example.myapplication.network.RetrofitService
import com.google.gson.Gson
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback


class DashboardFragment : Fragment(), OnMapReadyCallback, MapboxMap.OnMapLongClickListener,
    LocationEngineListener {

    private val MY_PERMISSIONS_REQUEST_LOCATION: Int=1234
    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private var mapView: MapView? = null
    private var myMap: MapboxMap? = null
    private var locationEngine : LocationEngine? = null
    private val retrofitService = RetrofitService.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mapView = binding.mapView
        mapView?.getMapAsync(this)

        dashboardViewModel.routeData.observe(viewLifecycleOwner, Observer {
            try {
                Log.e("routeData", "onCreate: $it ${Gson().toJson(it)}" )
                createPolyline(it.routes[0].geometry.coordinates)
                updateDistanceText(it.routes[0].distance,it.routes[0].duration)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })



        return root
    }

    private fun updateDistanceText(distance: Double, duration: Double) {
        binding.tvDistance.text= "${Math.round((distance/1000) * 100.0) / 100.0} KM\n${Math.round((duration/60) * 10.0) / 10.0} Min"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: MapboxMap?) {
        Log.d("onMapReady", "onMapReady")
        myMap = map
        myMap?.addOnMapLongClickListener(this)


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("onMapReady","Permission not granted")
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestLocationPermission()
            return
        }

        fetchCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        val options: LocationComponentOptions = LocationComponentOptions.builder(context)
            .trackingGesturesManagement(true)
            .accuracyColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
            .build()
        val locationComponent = myMap?.locationComponent
        locationComponent?.activateLocationComponent(requireContext(), options)
        locationComponent?.isLocationComponentEnabled = true
        locationEngine = locationComponent?.locationEngine!!
        locationEngine?.addLocationEngineListener(this)
        locationComponent.cameraMode = CameraMode.TRACKING
        locationComponent.renderMode = RenderMode.COMPASS


    }

    override fun onMapError(p0: Int, p1: String?) {
        Log.e("onMapError", p1!!)
    }


    private fun createMarker(latLng: LatLng) {
        val markerOptions: MarkerOptions = MarkerOptions().position(latLng).icon(
            IconFactory.getInstance(requireContext()).fromResource(R.drawable.ic_location_on)
        )
        markerOptions.title = "Marker"
        markerOptions.snippet = "This is a Marker"
        myMap?.addMarker(markerOptions)
        markerList.add(latLng)
    }

    private var markerList:ArrayList<LatLng> = ArrayList()
    private var polyline:PolylineOptions?=null

    private fun createPolyline(coordinates : List<List<Double>>){
        if (null!=polyline){
            myMap?.removePolyline(polyline!!.polyline)
        }
        var points:ArrayList<LatLng> = ArrayList()
        coordinates.forEach {

            points.add(LatLng(it[1],it[0]))
        }

        polyline=PolylineOptions()
            .addAll(points)
            .color(Color.parseColor("#3bb2d0"))
            .width(5f)
        myMap?.addPolyline(polyline!!)
    }

    override fun onMapLongClick(latLng: LatLng) {
        //val string: String = String.format("User long clicked at: %s", latLng.toString())
        //Toast.makeText(context, string, Toast.LENGTH_LONG).show()
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)//LatLng(22.8978, 77.3245))
            .zoom(15.0)
            .tilt(45.0)
            .build()
        myMap?.cameraPosition = cameraPosition
        createMarker(latLng)

        if(markerList.size>1) {
            var pointStr = ""
            markerList.forEach {
                if (!pointStr.equals("")) {
                    pointStr += ";"
                }
                pointStr += "" + it.longitude
                pointStr += "," + it.latitude
            }
            dashboardViewModel.getRoutes(pointStr)
        }
    }

    override fun onConnected() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           Log.e("onConnected","Permission not granted")
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationEngine?.requestLocationUpdates();
    }

    override fun onLocationChanged(location: Location?) {

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(
                location!!.latitude,
                location.longitude
            ))//LatLng(22.8978, 77.3245))
            .zoom(15.0)
            .tilt(45.0)
            .build()
        myMap?.cameraPosition = cameraPosition

//        myMap?.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    location!!.latitude,
//                    location.longitude
//                ), 16.0
//            )
//        )


    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                       fetchCurrentLocation()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

}

