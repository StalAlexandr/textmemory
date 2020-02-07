package ru.alexandrstal.textmemory.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


data class Phrase(

    @Relation(
        parentColumn = "id",
        entityColumn = "phraseId"
    )
    val slices:List<TextSlice>
) {

    @Embedded
    var emb: EmbPhrase = EmbPhrase()

}

