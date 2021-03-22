package me.ranieripieper.android.marvel.feature.characters.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.ranieripieper.android.marvel.databinding.ItemCellCharacterBinding
import me.ranieripieper.android.marvel.feature.characters.view.BaseCharactersListFragment
import me.ranieripieper.android.marvel.feature.characters.viewmodel.CharacterItem
import kotlin.properties.Delegates

class CharacterAdapter(
    private val onItemClick: BaseCharactersListFragment.OnItemClicked,
    private val onItemFavClick: BaseCharactersListFragment.OnItemFavClick
) :
    RecyclerView.Adapter<CharacterViewHolder>() {


    private var characterList: List<CharacterItem> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemBinding =
            ItemCellCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CharacterViewHolder(
            itemBinding
        )
    }

    override fun getItemCount(): Int = characterList.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val character: CharacterItem = characterList[position]
            holder.bindView(character, onItemClick, onItemFavClick)
        }
    }

    // Update data
    fun updateData(characterItemList: List<CharacterItem>) {
        characterList = characterItemList
    }


}