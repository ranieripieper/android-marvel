package me.ranieripieper.android.marvel.feature.characters.viewmodel

data class CharacterItem(
    val id: Long,
    val name: String,
    val description: String,
    val image: String,
    val comics: List<String>,
    val series: List<String>,
    var favorite: Boolean
)