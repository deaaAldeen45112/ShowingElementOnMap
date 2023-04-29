package com.example.baseapp.datasource.local

import com.example.baseapp.R
import com.example.baseapp.model.MarkerData

object DataBase {
    fun getCurrentMarker():ArrayList<MarkerData>{
        val markers=ArrayList<MarkerData>()


        markers.add( MarkerData("Irbid","Irbid is a city located in the north of Jordan, near the Syrian border. It is the second-largest city in the country, after the capital city Amman, and is an important economic and cultural center in the region.",
            R.drawable.irbid,"35.8479461","32.5570966"))
        markers.add( MarkerData("Amman","Amman is the capital and largest city of Jordan, located in the northwest of the country. It is a vibrant and cosmopolitan city, with a rich history and culture, and is an important economic and political center in the region.",
            R.drawable.amman,"35.6179577","31.8359188"))
        markers.add( MarkerData("Madaba","Madaba is a small town located in central Jordan, about 30 kilometers (19 miles) southwest of the capital city, Amman. It is known for its ancient mosaic art and is often referred to as the \"City of Mosaics\". ",
            R.drawable.madaba,"35.7572003","31.7158235"))
        markers.add( MarkerData("Aqaba","Aqaba is a coastal city located in the southernmost part of Jordan, on the Gulf of Aqaba, which is a northern extension of the Red Sea",
            R.drawable.aqaba,"34.9103199","29.5812447"))
        markers.add( MarkerData("Petra","Petra is an ancient city located in the southwestern part of Jordan, and is one of the most famous archaeological sites in the world",
            R.drawable.petra,"35.471868","30.3220515"))
        markers.add( MarkerData("Mafraq","Mafraq is a city located in northern Jordan, near the country's borders with Syria and Iraq",
            R.drawable.mafraq,"36.1403702","32.3403894"))
        return markers
    }
}