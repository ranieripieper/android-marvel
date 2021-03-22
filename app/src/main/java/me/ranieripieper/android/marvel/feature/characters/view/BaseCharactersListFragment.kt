package me.ranieripieper.android.marvel.feature.characters.view

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.ranieripieper.android.marvel.R
import me.ranieripieper.android.marvel.core.view.BaseFragment
import me.ranieripieper.android.marvel.databinding.FragmentCharacterListBinding
import me.ranieripieper.android.marvel.feature.characters.view.adapter.CharacterAdapter
import me.ranieripieper.android.marvel.feature.characters.view.adapter.FooterAdapter
import me.ranieripieper.android.marvel.feature.characters.viewmodel.CharacterViewModel


abstract class BaseCharactersListFragment : BaseFragment<CharacterViewModel>(true) {

    protected lateinit var characterAdapter: CharacterAdapter
    protected lateinit var footerAdapter: FooterAdapter

    protected lateinit var binding: FragmentCharacterListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCharacterListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterAdapter =
            CharacterAdapter(getOnItemClicked(), onItemFavClick = object : OnItemFavClick {
                override fun onClicked(id: Long, isFavorite: Boolean) {
                    viewModel.favUnfavItem(id, isFavorite)
                }
            })
        footerAdapter = FooterAdapter()
        binding.rvCharacters.apply {
            layoutManager = createLayoutManager(true)
            adapter = ConcatAdapter(characterAdapter, footerAdapter)

        }
        
        initViewModel()
    }

    private fun createLayoutManager(isGridLayout: Boolean): RecyclerView.LayoutManager {
        if (isGridLayout) {
            return GridLayoutManager(context, 2).apply {
                val spanCount = 2
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (position) {
                            binding.rvCharacters.adapter?.itemCount?.minus(1) -> spanCount
                            else -> 1
                        }
                    }
                }
            }
        } else {
            return LinearLayoutManager(context)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.character_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.characters_list_grid -> changeListGrid()
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.characters_list_grid)
        item?.apply {
            if (isGridMode()) {
                setIcon(R.drawable.ic_list)
            } else {
                setIcon(R.drawable.ic_grid)
            }
        }
    }

    private fun changeListGrid() {
        binding.rvCharacters.apply {
            layoutManager = createLayoutManager(!isGridMode())
            adapter?.notifyDataSetChanged()
        }

        activity?.invalidateOptionsMenu()
    }

    private fun isGridMode(): Boolean {
        binding.rvCharacters.apply {
            return layoutManager is GridLayoutManager
        }
    }

    protected abstract fun initViewModel()
    protected abstract fun getOnItemClicked(): OnItemClicked

    interface OnItemClicked {
        fun onClicked(id: Long, name: String, image: String)
    }

    interface OnItemFavClick {
        fun onClicked(id: Long, isFavorite: Boolean)
    }

}