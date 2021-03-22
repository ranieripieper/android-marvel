package me.ranieripieper.android.marvel.feature.characters.data.remote.response

import com.google.gson.annotations.SerializedName
import me.ranieripieper.android.marvel.feature.characters.data.model.Character

class CharactersResponse(
    val code: Int? = null,
    val status: String? = null,
    @SerializedName("data")
    val dataResponse: DataResponse<Character>? = null
)