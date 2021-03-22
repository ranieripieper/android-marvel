package me.ranieripieper.android.marvel.feature.characters.data.remote.response

data class DataResponse<T>(
    val offset: Int? = null,
    val limit: Int? = null,
    val total: Int? = null,
    val count: Int? = null,
    val results: List<T>? = null
)