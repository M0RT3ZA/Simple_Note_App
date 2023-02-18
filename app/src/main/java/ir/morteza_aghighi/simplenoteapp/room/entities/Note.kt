package ir.morteza_aghighi.simplenoteapp.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
/*this class is for creating new notes.*/
class Note(val title: String, val noteBody: String, val priority: Int = 0) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}