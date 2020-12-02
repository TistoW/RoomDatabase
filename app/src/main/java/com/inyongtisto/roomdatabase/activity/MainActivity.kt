package com.inyongtisto.roomdatabase.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.inyongtisto.roomdatabase.R
import com.inyongtisto.roomdatabase.adapter.AdapterNote
import com.inyongtisto.roomdatabase.model.NoteModel
import com.inyongtisto.roomdatabase.room.MyDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myDb = MyDatabase.getInstance(this) // call database

        displayNote()
        mainButton()
    }

    private fun mainButton() {
        btn_delete.setOnClickListener {
            startActivity(Intent(this, CreateNoteActivity::class.java))
        }
    }

    private fun displayNote() {
        val listNote = myDb.daoNote().getAll()
        rv_note.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        rv_note.adapter = AdapterNote(listNote, object : AdapterNote.Listeners {
            override fun onClicked(note: NoteModel) {
                val intent = Intent(this@MainActivity, DetailNoteActivity::class.java)
                intent.putExtra("extra", "" + note.id)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        displayNote()
        super.onResume()
    }
}