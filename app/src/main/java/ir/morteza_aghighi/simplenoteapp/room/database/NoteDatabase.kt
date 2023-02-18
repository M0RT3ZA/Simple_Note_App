package ir.morteza_aghighi.simplenoteapp.room.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ir.morteza_aghighi.simplenoteapp.room.daos.NoteDao
import ir.morteza_aghighi.simplenoteapp.room.entities.Note
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * use [] for passing multiple classes as an array
 * comments are from java file before migration to kotlin
 */
@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    /*Room does do all the behind screen magic!*/
    abstract fun noteDao(): NoteDao

    companion object {
        private var noteDatabaseInstance: NoteDatabase? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): NoteDatabase? {
            if (noteDatabaseInstance == null) {
                noteDatabaseInstance = databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java, "note_database"
                )
                    .fallbackToDestructiveMigration() /*adding callback to create default note first time
                    * the db is created*/
                    .addCallback(roomCallback)
                    .build()
            }
            return noteDatabaseInstance
        }

        /*create some notes in the first time we create database
    to have some notes in the Recyclerview for the first time app launches*/
        /*
    It is static because we want to access to static NoteDatabase() method*/
        private val roomCallback: Callback = object : Callback() {
            /*calls only when database is created*/
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val tAG = "NoteDatabase"
                val errorHandler = CoroutineExceptionHandler { _, throwable ->
                    Log.e(tAG, "$tAG Exception Error: $throwable")
                }
                /*creating default note after db is created
                * with help of coroutines*/
                CoroutineScope(Dispatchers.IO + errorHandler).launch {
                    val noteDao: NoteDao = noteDatabaseInstance!!.noteDao()
                    /*insert one default note*/
                    noteDao.insert(
                        Note(
                            "Default Note",
                            "This is the default note.\nYou can delete it!",
                            3
                        )
                    )
                }
            }
        }
    }
}