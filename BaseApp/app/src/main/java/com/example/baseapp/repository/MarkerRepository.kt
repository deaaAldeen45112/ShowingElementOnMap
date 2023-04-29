package com.example.baseapp.repository

import com.example.baseapp.datasource.local.DataBase
import com.example.baseapp.model.MarkerData
import javax.inject.Inject

class MarkerRepository @Inject constructor() {

    fun getAllMarkers():ArrayList<MarkerData>{
        return DataBase.getCurrentMarker();
    }


}
