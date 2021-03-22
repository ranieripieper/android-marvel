package me.ranieripieper.android.marvel.feature.characters.data.model

data class Character(
    val id: Long,
    val name: String,
    val description: String?,
    val thumbnail: Thumbnail?,
    val comics: Portfolio?,
    val series: Portfolio?
)