package me.ranieripieper.android.marvel.feature.characters.view.adapter

import me.ranieripieper.android.marvel.databinding.ItemCellComicSerieBinding

class PortfolioItemViewHolder(private val itemBinding: ItemCellComicSerieBinding) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(itemBinding.root) {

    fun bindView(item: String) {
        itemBinding.tvComicSerie.text = item
    }
}