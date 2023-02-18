package ir.morteza_aghighi.simplenoteapp.view

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ir.morteza_aghighi.simplenoteapp.R

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /*creating and casting our view items*/
    @JvmField
    var tvRowNumber: TextView
    @JvmField
    var tvNoteTitle: TextView
    @JvmField
    var cvNote: CardView

    init {
        tvRowNumber = itemView.findViewById(R.id.tvRowNumber)
        tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle)
        cvNote = itemView.findViewById(R.id.cvNotes)
    }
}