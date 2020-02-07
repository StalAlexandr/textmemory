package ru.alexandrstal.textmemory.dao

import androidx.room.*
import ru.alexandrstal.textmemory.entity.EmbPhrase
import ru.alexandrstal.textmemory.entity.Phrase
import ru.alexandrstal.textmemory.entity.TextSlice

@Dao
abstract class PhraseDAO {

 //   @Transaction
    fun insertPhrase(phrase: Phrase){
        insertPhrase(phrase.emb)

        val phraseId = phrase.emb.id
        phrase.slices.forEach{it.phraseId = phraseId
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
    abstract fun selectAllPhrases(): List<Phrase>


    @Insert
    abstract fun insertPhrase(emb:EmbPhrase)

    @Insert
    abstract fun insertSlice(slice: TextSlice)


    @Delete
    abstract fun deletePhrase(phrase: EmbPhrase)

    @Delete
    abstract fun deleteSlice(it: TextSlice)


}