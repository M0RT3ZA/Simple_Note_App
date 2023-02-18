package ir.morteza_aghighi.simplenoteapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.morteza_aghighi.simplenoteapp.R
import ir.morteza_aghighi.simplenoteapp.databinding.ActivityMainBinding
import ir.morteza_aghighi.simplenoteapp.view.NotesRecyclerAdapter
import ir.morteza_aghighi.simplenoteapp.view.SwipeUtil
import ir.morteza_aghighi.simplenoteapp.viewModel.MainActivityNoteViewModel


class MainActivity : AppCompatActivity() {

    /*assign noteViewModel variable */
    private lateinit var mainActivityNoteViewModel: MainActivityNoteViewModel
    private lateinit var notesRecyclerViewAdapter: NotesRecyclerAdapter

    /*implementing binding*/
    private lateinit var mainActivityBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        uiInit()
    }

    private fun uiInit() {
        notesRecyclerViewAdapter = NotesRecyclerAdapter(this@MainActivity)
        /*initializing norwViewModel*/
        mainActivityNoteViewModel = ViewModelProvider(this)[MainActivityNoteViewModel::class.java]

        /*add button and its listener*/

        /*launching addNoteActivity when add button is pressed*/
        mainActivityBinding.btnAdd.setOnClickListener {
            startNoteActivity()
        }

        /*recyclerView setup*/

        val rvNotes = mainActivityBinding.rvNotes
        rvNotes.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = notesRecyclerViewAdapter
        }
        rvNotes.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvNotes.setHasFixedSize(true)

        rvNotes.adapter = notesRecyclerViewAdapter

        /*getting liveDate, observe is needed because our data is live and may
        * change in realtime*/
        mainActivityNoteViewModel.allNotesLiveData()!!.observe(this) {
            notesRecyclerViewAdapter.differ.submitList(it)
//            for (i in 0 until notesRecyclerViewAdapter.differ.currentList.size)
//                notesRecyclerViewAdapter.differ.
        }

        /*function to delete by swipe*/
        swipeToDelete()
    }

    private fun swipeToDelete() {
        val swipeHelper = SwipeUtil(
            application,
            notesRecyclerViewAdapter)

/*            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val position = viewHolder.adapterPosition
//                myAdapter = mRecyclerView!!.adapter as MyAdapter?
                return if (myAdapter!!.isPendingRemoval(position)) {
                    0
                } else super.getSwipeDirs(recyclerView, viewHolder)
            }*/
        val mItemTouchHelper = ItemTouchHelper(swipeHelper)
        mItemTouchHelper.attachToRecyclerView(mainActivityBinding.rvNotes)
    }

    /*implementing menu*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /*this tells the system to use our custom menu as the activity menu*/
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_activity_overflow_menu, menu)
        return true
    }

    /*handling delete all notes button click*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnDeleteAllNotes) {
            mainActivityNoteViewModel.deleteAllNotes()
            Toast.makeText(
                this@MainActivity,
                getString(R.string.deleted_message), Toast.LENGTH_LONG
            ).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startNoteActivity() {
        /*starting activity*/
        startActivity(Intent(this@MainActivity, AddEditNoteActivity::class.java))
    }

}