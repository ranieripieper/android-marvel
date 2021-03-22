package me.ranieripieper.android.marvel.di

import android.content.Context
import androidx.room.Room
import me.ranieripieper.android.marvel.core.database.AppDatabase
import me.ranieripieper.android.marvel.feature.characters.data.local.FavoriteCharacterDao
import org.koin.dsl.module

val databaseModule = module {

    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "marvel"
        ).build()
    }

    fun provideFavoriteCharacterDao(appDatabase: AppDatabase): FavoriteCharacterDao {
        return appDatabase.favoriteCharacterDao()
    }

    single { provideAppDatabase(get()) }
    single { provideFavoriteCharacterDao(get()) }

}
