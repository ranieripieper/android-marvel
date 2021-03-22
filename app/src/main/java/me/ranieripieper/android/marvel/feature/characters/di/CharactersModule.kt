package me.ranieripieper.android.marvel.feature.characters.di

import me.ranieripieper.android.marvel.feature.characters.data.CharacterRepository
import me.ranieripieper.android.marvel.feature.characters.data.remote.MarvelCharactersApi
import me.ranieripieper.android.marvel.feature.characters.viewmodel.CharacterConverter
import me.ranieripieper.android.marvel.feature.characters.viewmodel.CharacterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val charactersModule = module {

    fun provideMarvelCharactersApi(retrofit: Retrofit): MarvelCharactersApi {
        return retrofit.create(MarvelCharactersApi::class.java)
    }

    single { provideMarvelCharactersApi(get()) }
    single { CharacterRepository(get(), get()) }
    single { CharacterConverter(get()) }

    viewModel {
        CharacterViewModel(get(), get(), get())
    }

}
