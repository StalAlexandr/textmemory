package ru.alexandrstal.textmemory.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class EmbPhrase(@PrimaryKey(autoGenerate = true)
                     var id:Long = 0)
