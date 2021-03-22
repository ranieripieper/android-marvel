package me.ranieripieper.android.marvel.core.viewmodel

sealed class ViewState {
    class Error(val error: String) : ViewState()
    object Loading : ViewState()
    object Content : ViewState()
    class Empty(val message: String) : ViewState()
}