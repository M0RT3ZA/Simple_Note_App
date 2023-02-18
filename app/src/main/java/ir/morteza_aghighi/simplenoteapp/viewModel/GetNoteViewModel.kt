package ir.morteza_aghighi.simplenoteapp.viewModel

import android.app.Application
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import ir.morteza_aghighi.simplenoteapp.repository.NoteRepository
import ir.morteza_aghighi.simplenoteapp.room.entities.Note
import java.io.Serializable

class GetNoteViewModel(application: Application) : AndroidViewModel(application) {
    /*creating repository and live data to communicate with lower layer
    * (repository and list of notes from database)*/
    private var noteRepository: NoteRepository = NoteRepository(application)

    /*function for getting individual notes*/
    fun getNote(position: Int): Note {
        return noteRepository.getNote(position)
    }
    /*end of functions*/
}