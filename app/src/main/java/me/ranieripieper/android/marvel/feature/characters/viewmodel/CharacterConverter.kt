package me.ranieripieper.android.marvel.feature.characters.viewmodel

import me.ranieripieper.android.marvel.R
import me.ranieripieper.android.marvel.core.viewmodel.ResourceManager
import me.ranieripieper.android.marvel.feature.characters.data.model.Character
import me.ranieripieper.android.marvel.feature.characters.data.model.FavoriteCharacter
import me.ranieripieper.android.marvel.feature.characters.data.model.Portfolio

class CharacterConverter(private val resourceManager: ResourceManager) {

    fun convertCharacter(
        character: List<Character>,
        favorites: Map<Long, FavoriteCharacter>
    ): List<CharacterItem> {
        val result = mutableListOf<CharacterItem>()
        character.forEach {
            result.add(convertCharacter(it, favorites))
        }
        return result
    }

    fun convertCharacter(
        character: Character,
        favorites: Map<Long, FavoriteCharacter>
    ): CharacterItem {
        return CharacterItem(
            character.id,
            character.name,
            convertDescription(character.description),
            getImageUrl(character),
            convertComicSerie(character.comics),
            convertComicSerie(character.series),
            favorites.containsKey(character.id)
        )
    }

    fun convertFavorite(
        favoriteCharacter: Map<Long, FavoriteCharacter>
    ): List<CharacterItem> {
        val result = mutableListOf<CharacterItem>()
        favoriteCharacter.values.forEach {
            result.add(convertFavorite(it))
        }
        return result
    }

    private fun convertFavorite(
        favoriteCharacter: FavoriteCharacter
    ): CharacterItem {
        return CharacterItem(
            favoriteCharacter.id,
            favoriteCharacter.name,
            "",
            favoriteCharacter.image,
            emptyList(),
            emptyList(),
            true
        )
    }

    private fun convertDescription(description: String?): String {
        return if (description == null || description.isEmpty()) {
            resourceManager.getString(R.string.no_description)
        } else {
            description
        }
    }

    private fun convertComicSerie(portfolio: Portfolio?): List<String> {
        val result = mutableListOf<String>()
        portfolio?.items?.forEach {
            result.add(it.name)
        }
        return result
    }

    private fun getImageUrl(character: Character): String {
        if (character.thumbnail?.path != null) {
            return String.format(
                "%s/standard_xlarge.%s",
                character.thumbnail.path,
                character.thumbnail.extension
            )
        }
        return ""
    }
}