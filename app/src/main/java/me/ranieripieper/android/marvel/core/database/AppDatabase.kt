package me.ranieripieper.android.marvel.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.ranieripieper.android.marvel.BuildConfig
import me.ranieripieper.android.marvel.feature.characters.data.local.FavoriteCharacterDao
import me.ranieripieper.android.marvel.feature.characters.data.model.FavoriteCharacter

@Database(entities = [FavoriteCharacter::class], version = BuildConfig.VERSION_CODE)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}