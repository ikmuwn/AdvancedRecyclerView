package kim.uno.recyclerview.ui.main

import android.view.ViewGroup
import kim.uno.recyclerview.widget.AdvancedRecyclerViewAdapter
import kim.uno.recyclerview.widget.AdvancedViewHolder

class ModelAdapter: AdvancedRecyclerViewAdapter() {

    override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
        return when (viewType) {
            0 -> ModelHolder(this)
            else -> null!!
        }
    }

}