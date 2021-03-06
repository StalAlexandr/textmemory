package ru.alexandrstal.textmemory.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.alexandrstal.textmemory.entity.EmbPhrase
import ru.alexandrstal.textmemory.entity.Phrase
import ru.alexandrstal.textmemory.entity.TextSlice

@Database(
    entities = [EmbPhrase::class, TextSlice::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun PhraseDAO():PhraseDAO


    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "phrases2.db")
            .build()
    }

}