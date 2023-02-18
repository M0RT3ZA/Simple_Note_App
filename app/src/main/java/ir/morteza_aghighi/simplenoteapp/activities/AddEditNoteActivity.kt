package ir.morteza_aghighi.simplenoteapp.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ir.morteza_aghighi.simplenoteapp.R
import ir.morteza_aghighi.simplenoteapp.room.entities.Note
import ir.morteza_aghighi.simplenoteapp.viewModel.AddEditNoteViewModel
import ir.morteza_aghighi.simplenoteapp.viewModel.GetNoteViewModel

class AddEditNoteActivity : AppCompatActivity() {
    /*Initialize spinner*/
    private lateinit var spnPriority: Spinner

    /*assign noteViewModel variable */
    private lateinit var addEditNoteViewModel: AddEditNoteViewModel

    /*edit text elements*/
    private lateinit var etTitle: EditText
    private lateinit var etNote: EditText
    /*creating noteID for the times we want to update a note*/
    private var noteID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        uiInit()
    }

    private fun uiInit() {
        /*title of activity*/
        title = "Add Note"

        /*casting editTexts*/
        etTitle = findViewById(R.id.etTitle)
        etNote = findViewById(R.id.etNote)

        /*setting up spinner*/
        spnPriority = findViewById(R.id.spnPriority)
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.priorities,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spnPriority.adapter = spinnerAdapter

        /*initializing norwViewModel*/
        addEditNoteViewModel = ViewModelProvider(this)[AddEditNoteViewModel::class.java]

        /*if this was an edit calls update() function
        * otherwise will call insert() function*/
        /*expression bellow means if we have a value different than
        * the default value it means this activity launched from recyclerView Adapter
        * thus we have position value for note and should use update() function*/
        val notePosition = intent.getIntExtra("notePosition", -1)
        if (notePosition > -1) {
            /*changing title of activity*/
            val getNoteViewModel = GetNoteViewModel(application).getNote(notePosition)
            title = "Edit Note"
            noteID = getNoteViewModel.id
            etTitle.setText(getNoteViewModel.title)
            etNote.setText(getNoteViewModel.noteBody)
            spnPriority.setSelection(getNoteViewModel.priority)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /*this tells the system to use our custom menu as the activity menu*/
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnSave) {
            saveNote()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        /*getting values of editTexts*/
        if (etTitle.text.toString().trim { it <= ' ' }.isEmpty() || etNote.text.toString()
                .trim { it <= ' ' }
                .isEmpty()
        ) {
            Toast.makeText(
                this@AddEditNoteActivity,
                getString(R.string.empty_warning),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val note = Note(
            etTitle.text.toString(),
            etNote.text.toString(),
            spnPriority.selectedItemPosition
        )
        /*if this was an edit calls update() function
        * otherwise will call insert() function*/
        /*expression bellow means if we have a value different than
        * the default value it means this activity launched from recyclerView Adapter
        * thus we have position value for note and should use update() function*/
        if (intent.getIntExtra("notePosition", -1) > -1) {
            note.id = noteID
            addEditNoteViewModel.update(note)
            Toast.makeText(
                this@AddEditNoteActivity,
                getString(R.string.note_updated),
                Toast.LENGTH_LONG
            ).show()
        } else {
            /*we have a new note so we are using insert*/
            addEditNoteViewModel.insert(note)
            Toast.makeText(
                this@AddEditNoteActivity,
                getString(R.string.note_added),
                Toast.LENGTH_LONG
            ).show()
        }
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(
            this@AddEditNoteActivity,
            getString(R.string.note_not_added),
            Toast.LENGTH_LONG
        ).show()
    }
}