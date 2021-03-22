package me.ranieripieper.android.marvel.feature.characters.data.remote

import kotlinx.coroutines.Deferred
import me.ranieripieper.android.marvel.feature.characters.data.remote.response.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelCharactersApi {

    @GET("v1/public/characters")
    fun getCharacters(
        @Query("orderBy") orderBy: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Deferred<CharactersResponse>

    @GET("v1/public/characters/{characterId}")
    fun getCharacterDetail(
        @Path("characterId") characterId: Long
    ): Deferred<CharactersResponse>
}