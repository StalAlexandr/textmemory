package ru.alexandrstal.textmemory.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.alexandrstal.textmemory.entity.EmbPhrase
import ru.alexandrstal.textmemory.entity.Phrase
import ru.alexandrstal.textmemory.entity.TextSlice

@Dao
abstract class PhraseDAO {

 //   @Transaction
    fun insertPhrase(phrase: Phrase){
        val phraseId =  insertPhrase(phrase.emb)
        phrase.slices.forEach{it.phraseId = phraseId
            insertSlice(it)
        }
    }


    fun updatePhrase(phrase: Phrase){


        selectSlicesForPhrase(phrase.id()).forEach {deleteSlice(it)}

        phrase.slices.forEach{it.phraseId = phrase.id()
            insertSlice(it)
        }

    }


 //   @Transaction
    fun deletePhrase(phrase: Phrase){

        phrase.slices.forEach{
            deleteSlice(it)
        }
        deletePhrase(phrase.emb)
    }




    @Transaction
    @Query("SELECT * FROM EmbPhrase")
    abstract fun loadAllPhrases(): LiveData<List<Phrase>>

    @Transaction
    @Query("SELECT * FROM EmbPhrase")
    abstract fun selectAllPhrases(): List<Phrase>


    @Transaction
    @Query("SELECT * FROM TextSlice where phraseId = :phraseId  ")
    abstract fun selectSlicesForPhrase(phraseId:Long): List<TextSlice>


    @Insert
    abstract fun insertPhrase(emb:EmbPhrase):Long

    @Insert
    abstract fun insertSlice(slice: TextSlice):Long


    @Delete
    abstract fun deletePhrase(phrase: EmbPhrase)

    @Delete
    abstract fun deleteSlice(it: TextSlice)

}