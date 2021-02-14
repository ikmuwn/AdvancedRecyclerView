package kim.uno.recyclerview.ui

import kim.uno.recyclerview.widget.AdvancedViewHolder
import kim.uno.recyclerview.widget.InfiniteRecyclerViewAdapter

class ModelAdapter: InfiniteRecyclerViewAdapter() {

    override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
        return ModelHolder(this)
    }

}