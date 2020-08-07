package com.example.mediaplayer


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MyAlbum(

    @PrimaryKey
    var id:Int?=null,
    var albumName: String?= null
) : RealmObject()
