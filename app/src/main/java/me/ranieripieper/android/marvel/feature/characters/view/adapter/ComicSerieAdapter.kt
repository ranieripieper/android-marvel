package me.ranieripieper.android.marvel.feature.characters.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.ranieripieper.android.marvel.databinding.ItemCellComicSerieBinding

class ComicSerieAdapter(private val items: List<String>) :
    RecyclerView.Adapter<PortfolioItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioItemViewHolder {
        val itemBinding =
            ItemCellComicSerieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PortfolioItemViewHolder(
            itemBinding
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PortfolioItemViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item: String = items[position]
            holder.bindView(item)
        }
    }

}