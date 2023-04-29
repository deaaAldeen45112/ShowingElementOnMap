package com.example.baseapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.baseapp.model.MarkerData
import com.example.baseapp.R


class RecyclerViewMarkersAdapter(
    private val markers: ArrayList<MarkerData>,
    private val listener: (MarkerData, Int) -> Unit) : RecyclerView.Adapter<RecyclerViewMarkersAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.marker_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.textView.text=markers[position].title
        viewHolder.imageView.setImageResource(markers[position].imageLink)


        Log.d("TAG", "onBindViewHolder: "+viewHolder.textView.text)
        viewHolder.itemView.setOnClickListener{
            listener(markers[position],position)
        }
    }

    override fun getItemCount(): Int {
        return markers.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView: ImageView


        init {
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.name)
            imageView=view.findViewById(R.id.image)
        }
    }



//    class ViewHolder(var itemBinding: ItemBinding) :
//        RecyclerView.ViewHolder(itemBinding.root) {
//        fun bindItem(marker: MarkerData) {
//            itemBinding.image.setImageResource(marker.imageLink)
//            itemBinding = marker.title
//        }
//    }
}
