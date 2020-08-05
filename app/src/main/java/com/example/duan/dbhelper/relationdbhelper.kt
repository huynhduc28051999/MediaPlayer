package com.example.duan.dbhelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.duan.model.relation_model

class relationdbhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "nhac.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RelationContract.RelationEntry.TABLE_NAME + " (" +
                    RelationContract.RelationEntry.COLUMN_ID_Relation + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    RelationContract.RelationEntry.COLUMN_ID_Music + " INTEGER," +
                    RelationContract.RelationEntry.COLUMN_ID_Album + " INTEGER)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RelationContract.RelationEntry.TABLE_NAME
    }
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }
    fun insertRelation(relation:relation_model){
        // Gets the data repository in write mode
            val db = this.writableDatabase
            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(RelationContract.RelationEntry.COLUMN_ID_Music, relation.idMusic)
            values.put(RelationContract.RelationEntry.COLUMN_ID_Album, relation.idAlbum)

            // Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(RelationContract.RelationEntry.TABLE_NAME, null, values)
    }
    fun readAllRelation():MutableList<relation_model> {
        val relation_model = mutableListOf<relation_model>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + RelationContract.RelationEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return mutableListOf()
        }

        var relation_id: Int
        var relation_music:Int
        var relation_album:Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
             relation_id = cursor.getInt(cursor.getColumnIndex(RelationContract.RelationEntry.COLUMN_ID_Relation))
                relation_music= cursor.getInt(cursor.getColumnIndex(RelationContract.RelationEntry.COLUMN_ID_Music))
                relation_album = cursor.getInt(cursor.getColumnIndex(RelationContract.RelationEntry.COLUMN_ID_Album))

                relation_model.add(relation_model(relation_id, relation_music,relation_album))
                cursor.moveToNext()
            }
        }
        return relation_model
    }
}