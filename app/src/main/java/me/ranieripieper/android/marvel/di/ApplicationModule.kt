package me.ranieripieper.android.marvel.di

import android.content.Context
import me.ranieripieper.android.marvel.core.viewmodel.CoroutineContextProvider
import me.ranieripieper.android.marvel.core.viewmodel.ResourceManager
import org.koin.dsl.module

val applicationModule = module {
    fun provideResourceManager(context: Context): ResourceManager {
        return ResourceManager(
            context
        )
    }

    single { provideResourceManager(get()) }
}

val courotinesModule = module {
    fun provideCoroutineContextProvider(): CoroutineContextProvider {
        return CoroutineContextProvider()
    }

    single { provideCoroutineContextProvider() }
}
