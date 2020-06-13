package kim.uno.recyclerview.ui.main

import androidx.collection.ArrayMap
import kim.uno.recyclerview.BR
import kim.uno.recyclerview.R
import kim.uno.recyclerview.widget.AdvancedDataBindingViewHolder
import kim.uno.recyclerview.widget.AdvancedRecyclerViewAdapter
import kotlin.math.abs
import kotlin.math.min

class ModelHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedDataBindingViewHolder<Model>(adapter, R.layout.model_holder) {

    override fun onScrollChanged() {
        super.onScrollChanged()
        val alphaBias = 1f - abs(viewBias - 0.5f) / 0.5f
        itemView.alpha = 0.5f + 0.5f * alphaBias
        itemView.translationY = scrolled[0] * 3f * motionBias
        itemView.rotationX = -min(20f, 20f * scrolled[0] / itemView.height * (1f - motionBias))
    }

    override fun getVariable(): ArrayMap<Int, Any> {
        return ArrayMap<Int, Any>().apply {
            put(BR.model, item)
        }
    }

}