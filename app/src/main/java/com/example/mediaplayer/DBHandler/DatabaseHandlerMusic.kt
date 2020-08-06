package com.example.mediaplayer.DBHandler

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.mediaplayer.SongInfo
import kotlin.math.log


public class DatabaseHandlerMusic(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        val create_students_table = String.format(
            "CREATE TABLE %s(" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT," +
                    "%s INT8 NOT NULL," +
                    "%s BOOLEAN" +
                    ")",
            TABLE_NAME,
            KEY_ID,
            KEY_URL,
            KEY_NAME,
            KEY_AUTHOR,
            KEY_DURATION,
            KEY_IS_LIKE
        )
        db.execSQL(create_students_table)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        val drop_students_table =
            String.format("DROP TABLE IF EXISTS %s", TABLE_NAME)
        db.execSQL(drop_students_table)
        onCreate(db)
    }

    fun addSong(song: SongInfo) {
        val query = String.format(
            "SELECT * FROM %s WHERE %s = \"%s\"",
            TABLE_NAME,
            KEY_URL,
            song.mSongURL
        )

        val dbread = this.readableDatabase
        val cursor: Cursor = dbread.rawQuery(query, null)
        cursor.moveToFirst()

        if (cursor.count === 0) {
            val dbwrite = this.writableDatabase
            val values = ContentValues()
            values.put(KEY_URL, song.mSongURL)
            values.put(KEY_NAME, song.mTitle)
            values.put(KEY_AUTHOR, song.mAuthorName)
            values.put(KEY_DURATION, song.mSize)
            values.put(KEY_IS_LIKE, false)
            dbwrite.insert(TABLE_NAME, null, values)
            dbwrite.close()
        }
        dbread.close()
    }
    companion object {
        private const val DATABASE_NAME = "MediaPlayer"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Music"
        private const val KEY_ID = "_id"
        private const val KEY_URL = "url"
        private const val KEY_NAME = "name"
        private const val KEY_AUTHOR = "author"
        private const val KEY_DURATION = "duration"
        private const val KEY_IS_LIKE = "isLike"
    }
}
