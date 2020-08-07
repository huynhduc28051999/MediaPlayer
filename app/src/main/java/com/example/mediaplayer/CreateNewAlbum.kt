package com.example.mediaplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.realm.DynamicRealm
import io.realm.Realm

class CreateNewAlbum : AppCompatActivity() {

    private lateinit var nameAlbumED: EditText
    private lateinit var btnAddNewAlbum: Button
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_album)
        // innit views
        realm = Realm.getDefaultInstance()
        nameAlbumED = findViewById(R.id.name_album)
        btnAddNewAlbum = findViewById(R.id.btn_add_newAlbum)
        // onclick Listner
        btnAddNewAlbum.setOnClickListener {
            addAlbumTODB()
        }
    }

    private fun addAlbumTODB() {
        try {

            // auto Increment ID
            realm.beginTransaction()

            val currentIdNum: Number? = realm.where(MyAlbum::class.java).max("id")

            val nextId: Int

            nextId = if (currentIdNum == null) {
                1
            } else {
                currentIdNum.toInt() + 1
            }
            val album = MyAlbum()

            album.albumName = nameAlbumED.text.toString()
            album.id = nextId

            realm.copyToRealmOrUpdate(album)
            realm.commitTransaction()
            Toast.makeText(this, "Album Added Successfully", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } catch (e: Exception) {
            Toast.makeText(this, "Failed $e", Toast.LENGTH_SHORT).show()
        }

    }
}