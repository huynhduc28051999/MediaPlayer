package com.example.duan.dbhelper

import android.provider.BaseColumns

object AlbumContract {

    /* Inner class that defines the table contents */
    class AlbumEntry : BaseColumns {
        companion object {
            val TABLE_NAME="album"
            val COLUMN_ID_ALBUM="id_album"
            val COLUMN_NAME_ALBUM = "name_album"
            val COLUMN_ISLIKE_ALBUM = "isLike_album"
        }
    }
}