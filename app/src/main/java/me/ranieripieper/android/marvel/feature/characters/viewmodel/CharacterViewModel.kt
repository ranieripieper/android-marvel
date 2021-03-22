package me.ranieripieper.android.marvel.feature.characters.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ranieripieper.android.marvel.R
import me.ranieripieper.android.marvel.core.service.RepositoryResult
import me.ranieripieper.android.marvel.core.viewmodel.BaseViewModel
import me.ranieripieper.android.marvel.core.viewmodel.ResourceManager
import me.ranieripieper.android.marvel.core.viewmodel.ViewState
import me.ranieripieper.android.marvel.feature.characters.data.CharacterRepository
import me.ranieripieper.android.marvel.feature.characters.data.model.FavoriteCharacter

class CharacterViewModel constructor(
    private val characterRepository: CharacterRepository,
    private val characterConverter: CharacterConverter,
    private val resourceManager: ResourceManager

) : BaseViewModel() {

    //Detail
    private val _characterDetail = MutableLiveData<CharacterItem>()
    private val _viewStateDetail = MutableLiveData<ViewState>()

    //favorite
    private val _characterFavList = MutableLiveData<List<CharacterItem>>()
    private val _viewStateFav = MutableLiveData<ViewState>()

    //remote
    private var lastRequestedPage = 1
    private val _viewState = MutableLiveData<ViewState>()
    private val characters: MutableList<CharacterItem> = mutableListOf()
    private val favorites: MutableMap<Long, FavoriteCharacter> = mutableMapOf()
    private val _characterList: MutableLiveData<List<CharacterItem>> by lazy {
        val liveData = MutableLiveData<List<CharacterItem>>()

        _viewState.value = ViewState.Loading

        fetchCharacters(lastRequestedPage, liveData)

        return@lazy liveData
    }

    fun loadNextPage() {
        if ((_viewState.value is ViewState.Loading)) {
            fetchCharacters(lastRequestedPage + 1, _characterList)
        }
    }

    private fun fetchCharacters(page: Int, liveData: MutableLiveData<List<CharacterItem>>) {
        viewModelScope.launch {
            if (page == 1 && favorites.isEmpty()) {
                when (val favResult =
                    withContext(contextPool.IO) { characterRepository.getFavorites() }) {
                    is RepositoryResult.Success -> {
                        favResult.data.forEach {
                            favorites[it.id] = it
                        }
                    }
                }

                _characterFavList.value = characterConverter.convertFavorite(favorites.toMap())
                updateViewStateFav()
                _viewStateFav.value = ViewState.Content
            }
            val result = withContext(contextPool.IO) {
                characterRepository.getCharacters(page)
            }

            _viewState.value = ViewState.Loading

            when (result) {
                is RepositoryResult.Success -> {
                    lastRequestedPage = page
                    result.data.dataResponse?.results?.toMutableList()
                        ?.let {
                            characters.addAll(
                                characterConverter.convertCharacter(
                                    it,
                                    favorites
                                )
                            )
                        }
                    liveData.value = characters
                }
                is RepositoryResult.Error -> {
                    _viewState.value = ViewState.Error(result.exception.message!!)
                }
            }
        }
    }

    fun fetchCharacter(id: Long) {
        _viewStateDetail.value = ViewState.Loading

        viewModelScope.launch {
            val result = withContext(contextPool.IO) {
                val character = findCharacter(id)
                if (character?.comics == null) {
                    val remoteResult = characterRepository.getCharacterById(id)
                    if (remoteResult is RepositoryResult.Success) {
                        return@withContext RepositoryResult.Success(
                            characterConverter.convertCharacter(
                                remoteResult.data,
                                favorites
                            )
                        )
                    } else {
                        return@withContext remoteResult
                    }
                } else {
                    return@withContext RepositoryResult.Success(character)
                }
            }

            when (result) {
                is RepositoryResult.Success -> {
                    _characterDetail.value = result.data as CharacterItem
                    _viewStateDetail.value = ViewState.Content
                }
                is RepositoryResult.Error -> {
                    _viewStateDetail.value = ViewState.Error(result.exception.message!!)
                }
            }
        }
    }

    private fun findCharacter(id: Long): CharacterItem? {
        return characters.firstOrNull() { it.id == id }
    }

    //favorite / unfavorite item
    fun favUnfavItem(id: Long, isFavorite: Boolean) {
        val character = findCharacter(id)
        viewModelScope.launch {

            if (isFavorite) {
                withContext(contextPool.IO) {
                    characterRepository.removeFavorite(id)
                    favorites.remove(id)
                }
            } else {
                withContext(contextPool.IO) {
                    character?.let {
                        val favoriteCharacter = FavoriteCharacter(
                            id = id,
                            image = character.image,
                            name = character.name
                        )
                        characterRepository.insertFavorite(
                            FavoriteCharacter(
                                id = character.id,
                                image = character.image,
                                name = character.name
                            )
                        )
                        favorites[character.id] = favoriteCharacter
                    }
                    return@withContext id
                }
            }

            character?.favorite = !isFavorite
            _characterFavList.value = characterConverter.convertFavorite(favorites)
            updateViewStateFav()
            _viewStateFav.value = ViewState.Content
            character?.run {
                _characterDetail.value = this
            }
            _characterList.value = characters
        }
    }


    private fun updateViewStateFav() {
        if (favorites.isEmpty()) {
            _viewStateFav.value = ViewState.Empty(resourceManager.getString(R.string.empty_result))
        } else {
            _viewStateFav.value = ViewState.Content
        }
    }

    val characterList: LiveData<List<CharacterItem>> = _characterList
    val viewState: LiveData<ViewState> = _viewState
    val characterDetail: LiveData<CharacterItem> = _characterDetail
    val viewStateDetail: LiveData<ViewState> = _viewStateDetail
    val characterFavList: LiveData<List<CharacterItem>> = _characterFavList
    val viewStateFav: LiveData<ViewState> = _viewStateFav
}