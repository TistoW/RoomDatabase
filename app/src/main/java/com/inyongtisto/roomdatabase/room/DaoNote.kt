package com.inyongtisto.roomdatabase.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.inyongtisto.roomdatabase.model.NoteModel

@Dao
interface DaoNote {

    @Insert(onConflict = REPLACE)
    fun insert(data: NoteModel)

    @Delete
    fun delete(data: NoteModel)

    @Update
    fun update(data: NoteModel): Int

    @Query("SELECT * from note ORDER BY id ASC")
    fun getAll(): List<NoteModel>

    @Query("SELECT * FROM note WHERE id = :id LIMIT 1")
    fun getNote(id: String): NoteModel

    @Query("DELETE FROM note")
    fun deleteAll(): Int
}