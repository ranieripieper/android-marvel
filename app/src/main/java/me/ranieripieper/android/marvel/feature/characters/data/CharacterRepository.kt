package me.ranieripieper.android.marvel.feature.characters.data

import me.ranieripieper.android.marvel.BuildConfig
import me.ranieripieper.android.marvel.core.service.RepositoryResult
import me.ranieripieper.android.marvel.feature.characters.CharacterNotFound
import me.ranieripieper.android.marvel.feature.characters.data.local.FavoriteCharacterDao
import me.ranieripieper.android.marvel.feature.characters.data.model.Character
import me.ranieripieper.android.marvel.feature.characters.data.model.FavoriteCharacter
import me.ranieripieper.android.marvel.feature.characters.data.remote.MarvelCharactersApi
import me.ranieripieper.android.marvel.feature.characters.data.remote.response.CharactersResponse

class CharacterRepository constructor(
    private val marvelCharactersApi: MarvelCharactersApi,
    private val favoriteCharacterDao: FavoriteCharacterDao
) {

    suspend fun getCharacters(page: Int): RepositoryResult<CharactersResponse> {

        return try {
            val result = marvelCharactersApi
                .getCharacters(
                    ORDER_NAME,
                    BuildConfig.PAGE_SIZE,
                    page * BuildConfig.PAGE_SIZE
                )
                .await()
            RepositoryResult.Success(result)
        } catch (ex: Exception) {
            RepositoryResult.Error(ex)
        }
    }

    suspend fun getCharacterById(id: Long): RepositoryResult<Character> {

        return try {
            val result = marvelCharactersApi
                .getCharacterDetail(id)
                .await()
            if (result.dataResponse?.results?.size == 1) {
                RepositoryResult.Success(result.dataResponse.results[0])
            } else {
                RepositoryResult.Error(CharacterNotFound("Character not found"))
            }

        } catch (ex: Exception) {
            RepositoryResult.Error(ex)
        }
    }

    suspend fun getFavorites(): RepositoryResult<List<FavoriteCharacter>> {
        return try {
            val result = favoriteCharacterDao.getAll()
            RepositoryResult.Success(result)
        } catch (ex: Exception) {
            RepositoryResult.Error(ex)
        }
    }

    suspend fun insertFavorite(favCharacter: FavoriteCharacter): RepositoryResult<FavoriteCharacter> {
        return try {
            favoriteCharacterDao.insert(favCharacter)
            RepositoryResult.Success(favCharacter)
        } catch (ex: Exception) {
            RepositoryResult.Error(ex)
        }
    }

    suspend fun removeFavorite(id: Long): RepositoryResult<Long> {
        return try {
            favoriteCharacterDao.deleteById(id)
            RepositoryResult.Success(id)
        } catch (ex: Exception) {
            RepositoryResult.Error(ex)
        }
    }

    companion object {
        private const val ORDER_NAME = "name"
    }
}