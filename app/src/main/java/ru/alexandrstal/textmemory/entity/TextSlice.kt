package ru.alexandrstal.textmemory.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TextSlice(

    val order:Int,
    val text:String,
    val hidden: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
    var phraseId:Int = 0
}