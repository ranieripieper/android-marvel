package me.ranieripieper.android.marvel.feature.characters.data

import me.ranieripieper.android.marvel.BaseUnitTest
import me.ranieripieper.android.marvel.core.service.RepositoryResult
import me.ranieripieper.android.marvel.feature.characters.data.local.FavoriteCharacterDao
import me.ranieripieper.android.marvel.feature.characters.data.model.*
import me.ranieripieper.android.marvel.feature.characters.data.remote.MarvelCharactersApi
import me.ranieripieper.android.marvel.feature.characters.data.remote.response.CharactersResponse
import me.ranieripieper.android.marvel.feature.characters.data.remote.response.DataResponse
import me.ranieripieper.android.marvel.toDeferred
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterRepositoryTest : BaseUnitTest() {

    @Mock
    lateinit var marvelCharactersApi: MarvelCharactersApi

    @Mock
    lateinit var favoriteCharacterDao: FavoriteCharacterDao

    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setup() {
        characterRepository = CharacterRepository(marvelCharactersApi, favoriteCharacterDao)
    }

    @Test
    fun `getCharacterById-error`() {
        val id = 1L

        testCoroutineRule.runBlockingTest {
            val charactersResponse = CharactersResponse()

            Mockito.`when`(marvelCharactersApi.getCharacterDetail(id))
                .thenReturn(charactersResponse.toDeferred())

            val repositoryResult = characterRepository.getCharacterById(id)
            Assert.assertThat(
                repositoryResult,
                CoreMatchers.instanceOf(RepositoryResult.Error::class.java)
            )
        }
    }

    @Test
    fun `getCharacterById-success`() {
        val id = 1L

        val character = Character(
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
        testCoroutineRule.runBlockingTest {
            val charactersResponse =
                CharactersResponse(
                    code = 1, status = "200", dataResponse = DataResponse<Character>(
                        results = listOf(
                            character
                        )
                    )
                )

            Mockito.`when`(marvelCharactersApi.getCharacterDetail(id))
                .thenReturn(charactersResponse.toDeferred())

            val repositoryResult = characterRepository.getCharacterById(id)
            Assert.assertThat(
                repositoryResult,
                CoreMatchers.instanceOf(RepositoryResult.Success::class.java)
            )
            Assert.assertEquals(
                character,
                (repositoryResult as RepositoryResult.Success).data
            )
        }
    }

    @Test
    fun `getAll Favorites`() {
        testCoroutineRule.runBlockingTest {

            val favorites = listOf(
                FavoriteCharacter(
                    id = 1,
                    name = "teste",
                    image = ""
                )
            )
            Mockito.`when`(favoriteCharacterDao.getAll())
                .thenReturn(
                    favorites
                )

            val repositoryResult = characterRepository.getFavorites()

            Assert.assertEquals(
                favorites,
                (repositoryResult as RepositoryResult.Success).data
            )
        }
    }

    @Test
    fun `getAll Favorites-error`() {
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(favoriteCharacterDao.getAll()).thenThrow(RuntimeException())

            val repositoryResult = characterRepository.getFavorites()

            Assert.assertEquals(
                repositoryResult,
                (repositoryResult as RepositoryResult.Error)
            )
        }
    }
}