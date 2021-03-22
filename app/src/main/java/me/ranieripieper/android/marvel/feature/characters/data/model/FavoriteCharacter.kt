package me.ranieripieper.android.marvel.feature.characters.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteCharacter(
    @PrimaryKey val id: Long,
    val name: String,
    val image: String
)