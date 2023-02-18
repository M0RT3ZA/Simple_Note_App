package ir.morteza_aghighi.simplenoteapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ir.morteza_aghighi.simplenoteapp.repository.NoteRepository
import ir.morteza_aghighi.simplenoteapp.room.entities.Note

class AddEditNoteViewModel(application: Application) : AndroidViewModel(application) {
    /*creating repository and live data to communicate with lower layer
    * (repository and list of notes from database)*/
    private var noteRepository: NoteRepository = NoteRepository(application)

    /*functions for inserting, updating notes*/
    fun insert(note: Note?) {
        noteRepository.insertNote(note)
    }

    fun update(note: Note?) {
        noteRepository.updateNote(note)
    }
    /*end of functions*/
}