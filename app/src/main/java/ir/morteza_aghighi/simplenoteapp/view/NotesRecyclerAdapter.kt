package ir.morteza_aghighi.simplenoteapp.view

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import ir.morteza_aghighi.simplenoteapp.R
import ir.morteza_aghighi.simplenoteapp.activities.AddEditNoteActivity
import ir.morteza_aghighi.simplenoteapp.databinding.NoteItemBinding
import ir.morteza_aghighi.simplenoteapp.repository.NoteRepository
import ir.morteza_aghighi.simplenoteapp.room.entities.Note
import kotlinx.coroutines.*

class NotesRecyclerAdapter(private val context: Context) :
    RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(
        val noteBinding: NoteItemBinding
    ) :
        RecyclerView.ViewHolder(noteBinding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.noteBody == newItem.noteBody
                    && oldItem.priority == newItem.priority
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = differ.currentList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.noteBinding.apply {
            tvRowNumber.text = (position + 1).toString()
            tvNoteTitle.text = currentNote.title
            /*setting cardView background according to the priority*/
            when (currentNote.priority) {
                0 -> cvNotes.setCardBackgroundColor(
                    MaterialColors.getColor(context, R.attr.colorVeryLowPriority, Color.TRANSPARENT)
                )
                1 -> cvNotes.setCardBackgroundColor(
                    MaterialColors.getColor(context, R.attr.colorLowPriority, Color.TRANSPARENT)
                )
                2 -> cvNotes.setCardBackgroundColor(
                    MaterialColors.getColor(context, R.attr.colorMediumPriority, Color.TRANSPARENT)
                )
                3 -> cvNotes.setCardBackgroundColor(
                    MaterialColors.getColor(context, R.attr.colorHighPriority, Color.TRANSPARENT)
                )
                4 -> cvNotes.setCardBackgroundColor(
                    MaterialColors.getColor(context, R.attr.colorUrgentPriority, Color.TRANSPARENT)
                )
            }
            holder.itemView.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    val editNoteIntent = Intent(context, AddEditNoteActivity::class.java)
                    editNoteIntent.putExtra("notePosition", position)
                    context.startActivity(editNoteIntent)
                }
            }
        }
    }

    fun remove(position: Int, application: Application) {
        if (position != RecyclerView.NO_POSITION) {
            val note = differ.currentList[position]
            NoteRepository(application).deleteNote(note)
            correctOrder(position)
        }
    }

    private fun correctOrder(position: Int) {
        notifyItemRemoved(position)
        val correctSize = differ.currentList.size - 1
        for (i in position .. correctSize) {
            if (position != correctSize) {
                notifyItemMoved(position + 1, position)
            }
        }
    }
}