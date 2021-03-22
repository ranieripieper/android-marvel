package me.ranieripieper.android.marvel.feature.characters.viewmodel

import me.ranieripieper.android.marvel.BaseUnitTest
import me.ranieripieper.android.marvel.R
import me.ranieripieper.android.marvel.core.viewmodel.ResourceManager
import me.ranieripieper.android.marvel.feature.characters.data.model.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterConverterTest : BaseUnitTest() {

    @Mock
    lateinit var resourceManager: ResourceManager

    lateinit var characterConverter: CharacterConverter

    @Before
    fun setup() {
        characterConverter = CharacterConverter(resourceManager)
        Mockito.`when`(resourceManager.getString(R.string.no_description))
            .thenReturn("no_description")
    }

    @Test
    fun convertCharacter() {
        val characters = listOf(
            Character(
                id = 1,
                name = "teste",
                description = "",
                comics = null,
                series = null,
                thumbnail = null
            ),
            Character(
                id = 2,
                name = "teste 2",
                description = "description",
                comics = Portfolio(
                    available = 1,
                    items = listOf(PortfolioItem(name = "comic"))
                ),
                series = Portfolio(
                    available = 1,
                    items = listOf(PortfolioItem(name = "serie"))
                ),
                thumbnail = Thumbnail(path = "path", extension = "png")
            )
        )

        val favorites = mutableMapOf<Long, FavoriteCharacter>()
        favorites[1] =
            FavoriteCharacter(
                id = 1,
                name = "teste",
                image = ""
            )
        val characterItems = characterConverter.convertCharacter(characters, favorites)

        Assert.assertEquals(2, characterItems.size)
        Assert.assertTrue(characterItems[0].favorite)
        Assert.assertFalse(characterItems[1].favorite)
        Assert.assertEquals("teste", characterItems[0].name)
        Assert.assertEquals("no_description", characterItems[0].description)
        Assert.assertEquals("", characterItems[0].image)
        Assert.assertEquals(0, characterItems[0].series.size)
        Assert.assertEquals(0, characterItems[0].comics.size)

        Assert.assertEquals("teste 2", characterItems[1].name)
        Assert.assertEquals("description", characterItems[1].description)
        Assert.assertEquals("path/standard_xlarge.png", characterItems[1].image)
        Assert.assertEquals(1, characterItems[1].series.size)
        Assert.assertEquals(1, characterItems[1].comics.size)
        Assert.assertEquals("serie", characterItems[1].series[0])
        Assert.assertEquals("comic", characterItems[1].comics[0])
    }

    @Test
    fun convertFavorite() {
        val favorites = mutableMapOf<Long, FavoriteCharacter>()
        favorites[1] =
            FavoriteCharacter(
                id = 1,
                name = "teste",
                image = ""
            )
        val characterItems = characterConverter.convertFavorite(favorites)

        Assert.assertEquals(1, characterItems.size)
        Assert.assertTrue(characterItems[0].favorite)
        Assert.assertEquals("teste", characterItems[0].name)
        Assert.assertEquals("", characterItems[0].description)
        Assert.assertEquals("", characterItems[0].image)
        Assert.assertEquals(0, characterItems[0].series.size)
        Assert.assertEquals(0, characterItems[0].comics.size)
    }
}