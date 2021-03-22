package me.ranieripieper.android.marvel.feature.characters.view.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import me.ranieripieper.android.marvel.R
import me.ranieripieper.android.marvel.databinding.ItemCellCharacterBinding
import me.ranieripieper.android.marvel.feature.characters.view.BaseCharactersListFragment
import me.ranieripieper.android.marvel.feature.characters.viewmodel.CharacterItem

class CharacterViewHolder(private val itemBinding: ItemCellCharacterBinding) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(itemBinding.root) {

    fun bindView(
        character: CharacterItem,
        onItemClick: BaseCharactersListFragment.OnItemClicked,
        onItemFavClick: BaseCharactersListFragment.OnItemFavClick
    ) {
        itemBinding.tvCharacterName.text = character.name

        if (character.favorite) {
            itemBinding.ivFavorite.setImageResource(R.drawable.ic_star_filled)
        } else {
            itemBinding.ivFavorite.setImageResource(R.drawable.ic_star)
        }

        Glide.with(itemBinding.root.context)
            .load(character.image)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(itemBinding.ivCharacter)

        itemBinding.root.setOnClickListener {
            onItemClick.onClicked(
                character.id,
                character.name,
                character.image
            )
        }
        itemBinding.ivFavorite.setOnClickListener {
            onItemFavClick.onClicked(
                character.id,
                character.favorite
            )
        }
    }
}