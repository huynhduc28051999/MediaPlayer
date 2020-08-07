package com.example.mediaplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kotlinx.android.synthetic.main.itemlist_album.view.*

class AlbumAdapter(private var context: Context?, private var albumList: RealmResults<MyAlbum>)

:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.itemlist_album, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.title_album.text = albumList[position]!!.albumName

    }

    class ViewHolder(view:View?):RecyclerView.ViewHolder(view!!){
        var albumName = itemView.findViewById<TextView>(R.id.title_album)
    }

}