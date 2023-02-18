package ir.morteza_aghighi.simplenoteapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ir.morteza_aghighi.simplenoteapp.repository.NoteRepository
import ir.morteza_aghighi.simplenoteapp.room.entities.Note

class MainActivityNoteViewModel(application: Application) : AndroidViewModel(application) {
    /*creating repository and live data to communicate with lower layer
    * (repository and list of notes from database)*/
    private var noteRepository: NoteRepository = NoteRepository(application)
    private var allNotesLiveData: LiveData<List<Note>?>? = noteRepository.fetchAllNotes()

    /*functions for showing, deleting one or all notes*/

    fun delete(note: Note?) {
        noteRepository.deleteNote(note)
    }

    fun deleteAllNotes() {
        noteRepository.deleteAllNotes()
    }

    fun allNotesLiveData(): LiveData<List<Note>?>? {
        return allNotesLiveData
    }
    /*end of functions*/
}