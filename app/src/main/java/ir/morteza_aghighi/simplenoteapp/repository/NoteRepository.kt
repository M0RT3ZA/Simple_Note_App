package ir.morteza_aghighi.simplenoteapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import ir.morteza_aghighi.simplenoteapp.room.daos.NoteDao
import ir.morteza_aghighi.simplenoteapp.room.database.NoteDatabase.Companion.getInstance
import ir.morteza_aghighi.simplenoteapp.room.entities.Note
import kotlinx.coroutines.*

class NoteRepository(application: Application) {
    private val noteDao: NoteDao
    private val allNotes: LiveData<List<Note>?>?
    private val tAG = "NoteRepository"
    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(tAG, "$tAG Exception Error: $throwable")
    }

    init {
        /*
         reading database to fetch notes
         */
        val noteDatabase = getInstance(application)
        noteDao = noteDatabase!!.noteDao()
        allNotes = noteDao.liveGetAllNotes
    }

    /*using coroutine to get note object from allNotes List
    * we need to get it from outside of ui thread otherwise app will crash*/
    fun getNote(position: Int): Note = runBlocking(
        Dispatchers.IO + errorHandler
    ) {
        noteDao.getAllNotes[position]
    }

    /*apis that repository exposes to outside*/
    fun insertNote(note: Note?) {
        /*uses noteDao to insert the note object*/
        CoroutineScope(Dispatchers.IO + errorHandler).launch {
            noteDao.insert(note)
        }
    }

    fun updateNote(note: Note?) {
        /*uses noteDao to update the note object*/
        CoroutineScope(Dispatchers.IO + errorHandler).launch {
            noteDao.update(note)
        }
    }

    fun deleteNote(note: Note?) {
        /*uses noteDao to delete the note object*/
        CoroutineScope(Dispatchers.IO + errorHandler).launch {
            noteDao.delete(note)
        }
    }



    fun deleteAllNotes() {
        /*uses noteDao to delete all note objects*/
        CoroutineScope(Dispatchers.IO + errorHandler).launch {
            noteDao.deleteAll()
        }
    }

    /*named fetch to differ from deleteAllNotes()*/
    fun fetchAllNotes(): LiveData<List<Note>?>? {
        /*
        return allNotes which obtained from allNotes = noteDao.getAllNotes()
        in constructor block
        */
        return allNotes
    }
}