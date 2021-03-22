package me.ranieripieper.android.marvel.feature.characters.view.adapter

import android.view.View
import me.ranieripieper.android.marvel.core.viewmodel.ViewState
import me.ranieripieper.android.marvel.databinding.LayoutFooterRecyclerViewBinding

class FooterViewHolder(private val itemBinding: LayoutFooterRecyclerViewBinding) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(itemBinding.root) {

    fun bindView(viewState: ViewState) {
        when (viewState) {
            is ViewState.Error -> {
                itemBinding.tvError.text = viewState.error
                itemBinding.tvError.visibility = View.VISIBLE
                itemBinding.pbLoading.visibility = View.GONE
            }
            is ViewState.Loading -> {
                itemBinding.tvError.visibility = View.GONE
                itemBinding.pbLoading.visibility = View.VISIBLE
            }
            is ViewState.Content -> {
                itemBinding.tvError.visibility = View.GONE
                itemBinding.pbLoading.visibility = View.GONE
            }
            is ViewState.Empty -> {
                itemBinding.tvError.visibility = View.VISIBLE
                itemBinding.tvError.text = viewState.message
                itemBinding.pbLoading.visibility = View.GONE
            }
        }
    }
}