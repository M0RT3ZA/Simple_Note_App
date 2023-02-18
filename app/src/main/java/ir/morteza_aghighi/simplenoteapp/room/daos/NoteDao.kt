package ir.morteza_aghighi.simplenoteapp.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.morteza_aghighi.simplenoteapp.room.entities.Note

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note?)

    @Update
    fun update(note: Note?)

    @Delete
    fun delete(note: Note?)

    @Query("DELETE FROM note_table")
    fun deleteAll()

    @get:Query("SELECT * FROM note_table ORDER BY priority DESC")
    val liveGetAllNotes: LiveData<List<Note>?>?

    /*method bellow is only used to get single notes
    * because it is not possible (AFAIK) to get single note without
    * a lifecycle observer*/
    @get:Query("SELECT * FROM note_table ORDER BY priority DESC")
    val getAllNotes: List<Note>
}