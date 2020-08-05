package com.example.duan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.duan.R
import com.example.duan.model.music_model

class adaptersong(var mlistSong: MutableList<music_model>) :
    RecyclerView.Adapter<adaptersong.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adaptersong.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.column_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(mlistSong.size>3)
        {
            return 3
        }
        else
        {
            return mlistSong.size
        }
    }

    override fun onBindViewHolder(holder: adaptersong.ViewHolder, position: Int) {
        val msong: music_model = mlistSong[position]
        holder.namesong.text=msong.name
        holder.Athornamesong.text=msong.author
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var namesong = itemView.findViewById(R.id.txt_namesong) as TextView
        var Athornamesong=itemView.findViewById(R.id.txtAthornamesong)as TextView
    }
}