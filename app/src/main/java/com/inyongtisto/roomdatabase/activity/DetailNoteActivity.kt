package com.inyongtisto.roomdatabase.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.inyongtisto.roomdatabase.R
import com.inyongtisto.roomdatabase.model.NoteModel
import com.inyongtisto.roomdatabase.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.activity_detail_note.btn_back
import kotlinx.android.synthetic.main.activity_detail_note.btn_delete
import kotlinx.android.synthetic.main.activity_detail_note.edt_desc
import kotlinx.android.synthetic.main.activity_detail_note.edt_title

class DetailNoteActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    lateinit var note: NoteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)
        myDb = MyDatabase.getInstance(this) // call database

        setupData()
        mainButton()
    }

    private fun setupData(){
        val noteId = intent.getStringExtra("extra").toString()
        note = myDb.daoNote().getNote(noteId) // get note by id selected note from mainActivity
        edt_title.setText(note.title)
        edt_desc.setText(note.description)
    }

    private fun mainButton(){
        btn_delete.setOnClickListener {
            delete()
        }

        btn_back.setOnClickListener {
            update()
        }
    }

    private fun update() {
        if (edt_title.text.isEmpty()) {
            edt_title.error = "field is required"
            edt_title.requestFocus()
            return
        }

        note.title = edt_title.text.toString()
        note.description = edt_title.text.toString()

        // insert data
        CompositeDisposable().add(
            Observable.fromCallable {
            myDb.daoNote().update(note) // Update note
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons", "data update")
                finish()
            })
    }

    private fun delete() {
        // insert data
        CompositeDisposable().add(
            Observable.fromCallable {
                myDb.daoNote().delete(note) // Delete note
            }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("respons", "data deleted")
                    Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show()
                    finish()
                })
    }

    override fun onBackPressed() {
        update()
    }
}