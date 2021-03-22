package me.ranieripieper.android.marvel.feature.characters.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.Deferred
import me.ranieripieper.android.marvel.feature.characters.data.model.FavoriteCharacter

@Dao
interface FavoriteCharacterDao {

    @Query("SELECT * FROM FavoriteCharacter")
    fun getAll(): List<FavoriteCharacter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favCharacter: FavoriteCharacter)

    @Query("DELETE FROM FavoriteCharacter WHERE id = :id")
    fun deleteById(id: Long)
}