package com.example.baseapp.ui.showingPlaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.baseapp.model.MarkerData
import com.example.baseapp.repository.MarkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowingPlacesViewModel @Inject constructor(private val markerRepository: MarkerRepository):
ViewModel(){
private val getAllMarkersMutableLiveData=MutableLiveData<ArrayList<MarkerData>>()

    fun getAllMarkers(){
          getAllMarkersMutableLiveData.value= markerRepository.getAllMarkers()
    }
    fun getAllMarkersLiveData():LiveData<ArrayList<MarkerData>>{return this.getAllMarkersMutableLiveData}


}