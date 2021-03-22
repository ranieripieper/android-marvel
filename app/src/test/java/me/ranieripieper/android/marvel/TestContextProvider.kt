package me.ranieripieper.android.marvel

import kotlinx.coroutines.Dispatchers
import me.ranieripieper.android.marvel.core.viewmodel.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext

class TestContextProvider : CoroutineContextProvider() {
    override val Main: CoroutineContext = Dispatchers.Unconfined
    override val IO: CoroutineContext = Dispatchers.Unconfined
    override val Default: CoroutineContext = Dispatchers.Unconfined
}