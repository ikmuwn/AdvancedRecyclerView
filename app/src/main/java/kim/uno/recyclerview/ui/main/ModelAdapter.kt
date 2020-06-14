package kim.uno.recyclerview.ui.main

import kim.uno.recyclerview.widget.AdvancedRecyclerViewAdapter
import kim.uno.recyclerview.widget.AdvancedViewHolder

class ModelAdapter: AdvancedRecyclerViewAdapter() {

    override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
        return ModelHolder(this)
    }

}