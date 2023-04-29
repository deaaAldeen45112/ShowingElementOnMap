package com.example.baseapp.ui.showingPlaces


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseapp.R
import com.example.baseapp.adapter.RecyclerViewMarkersAdapter
import com.example.baseapp.model.MarkerData
import com.example.baseapp.resource.external.ExpandedMBTilesTileProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.kml.KmlLayer

import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.InputStream


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint
class ShowingPlacesFragment : Fragment() {



    private lateinit var  recyclerViewMarkers :RecyclerView
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mMap: GoogleMap
    private var cameraZoom=5.0f

    private val callback = OnMapReadyCallback { googleMap ->
        mMap=googleMap
        val markerManager = MarkerManager(mMap)
        val groundOverlayManager = GroundOverlayManager(mMap!!)
        val polygonManager = PolygonManager(mMap)
        val polylineManager = PolylineManager(mMap)
        val kmlLayerFromFile1 = KmlLayer(mMap,R.raw.kml_file_1,
            context, markerManager, polygonManager, polylineManager,
            groundOverlayManager, null)

        val kmlLayerFromFileTow = KmlLayer(mMap, R.raw.kml_file_2,
            context, markerManager, polygonManager, polylineManager,
            groundOverlayManager, null)


        kmlLayerFromFile1.addLayerToMap()
        kmlLayerFromFile1.setOnFeatureClickListener { feature: Feature ->
            Log.d("TAG", "layer1: ")
            Toast.makeText(context, "placeMark from Kml file1", Toast.LENGTH_SHORT).show()


        }

        kmlLayerFromFileTow.addLayerToMap()
        kmlLayerFromFileTow.setOnFeatureClickListener { feature: Feature ->
            Log.d("TAG", "layer2: ")
            Toast.makeText(context, "placeMark from Kml file2", Toast.LENGTH_SHORT).show()


        }

        val file: File = createTempFile()
        val inputStream: InputStream =requireContext().assets.open("mbtiles_file.mbtiles")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val tileProvider: TileProvider = ExpandedMBTilesTileProvider(file, 256, 256)
        val tileOverlayOptions = TileOverlayOptions().tileProvider(tileProvider)
        tileOverlayOptions.transparency(0f)
        val tileOverlay= googleMap.addTileOverlay(tileOverlayOptions)



        showingPlacesViewModel.getAllMarkersLiveData().observe(viewLifecycleOwner,Observer<ArrayList<MarkerData>>{ markerData->
            val markerCollection = markerManager.newCollection()

            markerData.forEach {
                val height = 100
                val width = 100
                val bitmapdraw = resources.getDrawable(it.imageLink) as BitmapDrawable
                val b = bitmapdraw.bitmap
                val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

                markerCollection.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.latitude.toDouble(),it.longitude.toDouble()))
                        .title(it.title)
                        //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                )

            }
            markerCollection.setOnMarkerClickListener { marker: Marker ->

                val details=markerData.filter { markerData -> markerData.longitude.toDouble()==marker.position.longitude
                        &&markerData.latitude.toDouble()==marker.position.latitude}.first().details
                showDialog(details)
                false
            }

            mMap.moveCamera(CameraUpdateFactory.
            newLatLngZoom(LatLng(markerData.get(0).latitude.toDouble()
                ,markerData.get(0).longitude.toDouble()),cameraZoom))


//            markerCollection.setOnInfoWindowClickListener { marker: Marker ->
//                Toast.makeText(context, "Clicked on actor:", Toast.LENGTH_SHORT).show()
//                val details=markerData.filter { markerData -> markerData.longitude.toDouble()==marker.position.longitude
//                        &&markerData.latitude.toDouble()==marker.position.latitude}.first().details
//                showDialog(details)
//            }

            recyclerViewMarkers.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = RecyclerViewMarkersAdapter(markerData) { markerData, position ->
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(markerData.latitude.toDouble(),markerData.longitude.toDouble()),cameraZoom+7.0f))
                markerCollection.markers.forEach {
                     if(it.position.longitude==markerData.longitude.toDouble()
                        &&it.position.latitude==markerData.latitude.toDouble()){


                        val height = 100
                        val width = 100
                        val bitmapdraw = resources.getDrawable(markerData.imageLink) as BitmapDrawable
                        val b = bitmapdraw.bitmap
                        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
                        markerCollection.markers.forEach{it.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))}
                        it.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        it.showInfoWindow()



                    }
                }
                }
            }
        })
        showingPlacesViewModel.getAllMarkers()


    }
    private val showingPlacesViewModel:ShowingPlacesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_showing_places, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         recyclerViewMarkers= view.findViewById(R.id.recyclerView_markers)
         val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
         mapFragment?.getMapAsync(callback)

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShowingPlaces.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowingPlacesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun showDialog(details:String){

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Detail About Place")
        builder.setMessage(details)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(context,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(context,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.show()
        Log.d("TAG", "marker: ")
    }


}