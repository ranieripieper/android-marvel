package me.ranieripieper.android.marvel.core.viewmodel

import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseViewModel() : ViewModel(), KoinComponent {
    val contextPool: CoroutineContextProvider by inject()
}