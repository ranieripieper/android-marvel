package me.ranieripieper.android.marvel.feature.characters.view

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CharactersFragment : BaseCharactersListFragment() {

    override fun initViewModel() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            footerAdapter.updateState(viewState)
        })

        viewModel.characterList.observe(viewLifecycleOwner, Observer { characterList ->
            characterAdapter.updateData(characterList)
            binding.rvCharacters.apply {
                addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrolled(
                        recyclerView: RecyclerView,
                        dx: Int,
                        dy: Int
                    ) {
                        super.onScrolled(recyclerView, dx, dy)
                        val totalItemCount = layoutManager?.itemCount
                        val lastVisibleItem =
                            (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                        if (lastVisibleItem == totalItemCount?.minus(1)) {
                            clearOnScrollListeners()
                            viewModel.loadNextPage()
                        }
                    }
                })
            }
        })
    }

    override fun getOnItemClicked(): OnItemClicked {
        return object : OnItemClicked {
            override fun onClicked(id: Long, name: String, image: String) {
                val actionDetail =
                    CharactersFragmentDirections.actionCharactersListToCharacterDetail(
                        id,
                        name,
                        image
                    )

                Navigation.findNavController(binding.root).navigate(actionDetail)
            }
        }
    }
}