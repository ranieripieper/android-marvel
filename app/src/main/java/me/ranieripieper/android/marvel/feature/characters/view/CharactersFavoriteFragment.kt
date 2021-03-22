package me.ranieripieper.android.marvel.feature.characters.view

import androidx.lifecycle.Observer
import androidx.navigation.Navigation

class CharactersFavoriteFragment : BaseCharactersListFragment() {

    override fun initViewModel() {

        viewModel.viewStateFav.observe(viewLifecycleOwner, Observer { viewState ->
            footerAdapter.updateState(viewState)
        })

        viewModel.characterFavList.observe(viewLifecycleOwner, Observer { characterList ->
            characterAdapter.updateData(characterList)
        })
    }

    override fun getOnItemClicked(): OnItemClicked {
        return object : OnItemClicked {
            override fun onClicked(id: Long, name: String, image: String) {
                val actionDetail =
                    CharactersFavoriteFragmentDirections.actionCharactersFavoritesToCharacterDetail(
                        id,
                        name,
                        image
                    )

                Navigation.findNavController(binding.root).navigate(actionDetail)
            }
        }
    }
}