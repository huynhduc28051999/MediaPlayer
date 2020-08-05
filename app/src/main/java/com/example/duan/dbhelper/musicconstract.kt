package com.example.duan.dbhelper

import android.provider.BaseColumns

object MusicContract {

    /* Inner class that defines the table contents */
    class MusicEntry : BaseColumns {
        companion object {
            val TABLE_NAME="music"
            val COLUMN_ID_MUSIC="id_music"
            val COLUMN_URL_MUSIC = "url_music"
            val COLUMN_NAME_MUSIC = "name_music"
            val COLUMN_AUTHOR_MUSIC = "author_music"
            val COLUMN_DURATION_MUSIC = "duration_music"
            val COLUMN_ISLIKE_MUSIC = "isLike_music"
        }
    }
}