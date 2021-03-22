package me.ranieripieper.android.marvel.feature.characters.view

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.ranieripieper.android.marvel.R
import me.ranieripieper.android.marvel.core.view.BaseFragment
import me.ranieripieper.android.marvel.core.viewmodel.ViewState
import me.ranieripieper.android.marvel.databinding.FragmentCharacterDetailBinding
import me.ranieripieper.android.marvel.feature.characters.view.adapter.ComicSerieAdapter
import me.ranieripieper.android.marvel.feature.characters.viewmodel.CharacterViewModel

class CharacterDetailFragment : BaseFragment<CharacterViewModel>(true) {

    private lateinit var binding: FragmentCharacterDetailBinding


    private var isFavorite: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCharacterDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CharacterDetailFragmentArgs.fromBundle(
            requireArguments()
        )
        val id = args.characterId
        val image = args.characterImage
        val name = args.characterName

        binding.tvName.text = name
        loadImage(image)

        initViewModel(id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.character_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.fav_item)
        if (isFavorite != null) {
            if (isFavorite!!) {
                item.setIcon(R.drawable.ic_star_filled)
            } else {
                item.setIcon(R.drawable.ic_star)
            }
            item.isVisible = true
        } else {
            item.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_item -> favUnfavItem()
        }
        return true
    }

    private fun favUnfavItem() {
        viewModel.favUnfavItem(
            CharacterDetailFragmentArgs.fromBundle(
                requireArguments()
            ).characterId,
            isFavorite!!
        )
    }

    private fun initViewModel(id: Long) {
        viewModel.viewStateDetail.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ViewState.Error -> {
                    binding.loadingView.root.visibility = View.GONE
                    Toast.makeText(context, viewState.error, Toast.LENGTH_SHORT).show()
                }
                is ViewState.Loading ->
                    binding.loadingView.root.visibility = View.VISIBLE
                is ViewState.Content -> {
                    binding.loadingView.root.visibility = View.GONE
                }
            }
        })

        viewModel.characterDetail.observe(viewLifecycleOwner, Observer { character ->
            binding.apply {
                tvName.text = character.name
                tvDescription.text = character.description
                tvDescription.visibility =
                    if (TextUtils.isEmpty(character.description)) View.GONE else View.VISIBLE

                loadImage(character.image)

                configComicSerie(binding.rvComics, binding.tvComics, character.comics)
                configComicSerie(binding.rvSeries, binding.tvSeries, character.series)

                isFavorite = character.favorite
                activity?.invalidateOptionsMenu()
            }
        })

        viewModel.fetchCharacter(id)

    }

    private fun configComicSerie(
        recyclerView: RecyclerView,
        tvSubtitle: TextView,
        items: List<String>
    ) {
        if (items.isNotEmpty()) {
            recyclerView.adapter = ComicSerieAdapter(items)
            recyclerView.visibility = View.VISIBLE
            tvSubtitle.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.GONE
            tvSubtitle.visibility = View.GONE
        }
    }

    private fun loadImage(image: String) {
        Glide.with(requireContext())
            .load(image)
            .into(binding.ivCharacter)
    }
}