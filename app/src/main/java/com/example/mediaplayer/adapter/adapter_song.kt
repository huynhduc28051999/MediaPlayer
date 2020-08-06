package com.example.mediaplayer.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.PlayerProcessing
import com.example.mediaplayer.R
import com.example.mediaplayer.Service
import com.example.mediaplayer.model.music_model


class adaptersong(var mlistSong: MutableList<music_model>, val context: Context) :
    RecyclerView.Adapter<adaptersong.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adaptersong.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_listmusic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(mlistSong.size>6)
        {
            return 6
        }
        else
        {
            return mlistSong.size
        }
    }

    override fun onBindViewHolder(holder: adaptersong.ViewHolder, position: Int) {
        val msong: music_model = mlistSong[position]
        var maxTitle = msong.name.length
        var maxAuthorName = msong.author.length
        if (maxTitle > 20) maxTitle = 20
        if (maxAuthorName > 20) maxAuthorName = 20
        holder.namesong.text=msong.name.substring(0, maxTitle)
        holder.Athornamesong.text=msong.author.substring(0, maxAuthorName)
        holder.play.setOnClickListener {
            try {
                Service.startPlay(position)
                var intent = Intent(context, PlayerProcessing::class.java)
                val bundle = Bundle()
                bundle.putString("mTitle", msong.name)
                bundle.putString("mAuthorName", msong.author)
                bundle.putString("mSongURL", msong.url)
                bundle.putString("mSize", msong.duration.toString())
                intent.putExtras(bundle)
                context.startActivity(intent)
            } catch (ex: java.lang.Exception) {
                Log.e("Player", ex.toString())
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var namesong = itemView.findViewById(R.id.tvSongName) as TextView
        var Athornamesong=itemView.findViewById(R.id.tvAuthor)as TextView
        var play: ImageView = itemView.findViewById(R.id.buttonPlay)
    }
}