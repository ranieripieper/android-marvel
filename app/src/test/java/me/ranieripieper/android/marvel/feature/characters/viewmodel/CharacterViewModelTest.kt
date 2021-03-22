package me.ranieripieper.android.marvel.feature.characters.viewmodel

import androidx.lifecycle.Observer
import me.ranieripieper.android.marvel.BaseUnitTest
import me.ranieripieper.android.marvel.MockitoHelper
import me.ranieripieper.android.marvel.R
import me.ranieripieper.android.marvel.core.service.RepositoryResult
import me.ranieripieper.android.marvel.core.viewmodel.ResourceManager
import me.ranieripieper.android.marvel.core.viewmodel.ViewState
import me.ranieripieper.android.marvel.feature.characters.data.CharacterRepository
import me.ranieripieper.android.marvel.feature.characters.data.model.Character
import me.ranieripieper.android.marvel.feature.characters.data.model.Portfolio
import me.ranieripieper.android.marvel.feature.characters.data.model.PortfolioItem
import me.ranieripieper.android.marvel.feature.characters.data.model.Thumbnail
import me.ranieripieper.android.marvel.feature.characters.data.remote.response.CharactersResponse
import me.ranieripieper.android.marvel.feature.characters.data.remote.response.DataResponse
import me.ranieripieper.android.marvel.testObserver
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterViewModelTest : BaseUnitTest() {

    @Mock
    lateinit var repository: CharacterRepository

    @Mock
    lateinit var resourceManager: ResourceManager

    @Mock
    lateinit var converter: CharacterConverter

    @Mock
    lateinit var viewStateObserver: Observer<ViewState>

    @Mock
    lateinit var characterListObserver: Observer<List<CharacterItem>>

    lateinit var viewModel: CharacterViewModel

    private val errorMsg = "errorMsg"

    @Before
    fun setup() {
        Mockito.`when`(resourceManager.getString(R.string.empty_result)).thenReturn("empty_result")
    }

    @Test
    fun `error test when loading characters`() {
        createViewModel(false)

        Assert.assertEquals(1, viewModel.viewState.testObserver().observedValues.size)

        Assert.assertThat(
            viewModel.viewState.testObserver().observedValues[0],
            CoreMatchers.instanceOf(ViewState.Error::class.java)
        )
    }

    @Test
    fun `loading characters`() {
        createViewModel(true)

        testCoroutineRule.runBlockingTest {

            val argResult = MockitoHelper.argumentCaptor<List<CharacterItem>>()

            Mockito.verify(characterListObserver, Mockito.times(1))
                .onChanged(argResult.capture())

            Assert.assertEquals(1, argResult.allValues.size)
        }
    }


    private fun getCharactersResponse(): CharactersResponse {
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
        return CharactersResponse(
            code = 1, status = "200",
            dataResponse = DataResponse(
                results = characters
            )
        )
    }

    private fun createViewModel(successTest: Boolean) {
        val charactersResponse = getCharactersResponse()
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.getCharacters(1)).thenReturn(
                if (successTest) RepositoryResult.Success(charactersResponse)
                else RepositoryResult.Error(Exception(errorMsg))
            )

            val lstCharacter: List<Character> = charactersResponse.dataResponse?.results!!
            
            Mockito.`when`(
                converter.convertCharacter(
                    MockitoHelper.safeEq(lstCharacter),
                    ArgumentMatchers.anyMap()
                )
            ).thenReturn(
                listOf(
                    CharacterItem(
                        id = lstCharacter[0].id,
                        name = lstCharacter[0].name,
                        image = "",
                        series = emptyList(),
                        comics = emptyList(),
                        description = "",
                        favorite = false
                    ),
                    CharacterItem(
                        id = charactersResponse.dataResponse!!.results?.get(1)!!.id,
                        name = charactersResponse.dataResponse!!.results?.get(1)!!.name,
                        image = "",
                        series = emptyList(),
                        comics = emptyList(),
                        description = "",
                        favorite = false
                    )

                )
            )
        }

        viewModel = CharacterViewModel(repository, converter, resourceManager).apply {
            viewState.observeForever(viewStateObserver)
            characterList.observeForever(characterListObserver)
        }
    }
}