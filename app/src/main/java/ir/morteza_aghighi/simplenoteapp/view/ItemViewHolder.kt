package ir.morteza_aghighi.simplenoteapp.view

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ir.morteza_aghighi.simplenoteapp.R

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    @JvmField
    var cvNotes: CardView
    @JvmField
    var tvRowNumber: TextView
    @JvmField
    var tvNoteTitle: TextView

    init {
        cvNotes = view.findViewById(R.id.cvNotes)
        tvRowNumber = view.findViewById(R.id.tvRowNumber)
        tvNoteTitle = view.findViewById(R.id.tvNoteTitle)
    }
}