package kim.uno.recyclerview.ui

import kim.uno.recyclerview.R
import kim.uno.recyclerview.data.Model
import kim.uno.recyclerview.widget.AdvancedViewBindingHolder
import kim.uno.recyclerview.widget.AdvancedRecyclerViewAdapter
import kotlin.math.abs
import kotlin.math.min

class ModelBindingHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedViewBindingHolder<Model>(adapter, R.layout.model_holder) {

    override fun onScrollChanged() {
        super.onScrollChanged()
        val alphaBias = 1f - abs(viewBias - 0.5f) / 0.5f
        itemView.alpha = 0.5f + 0.5f * alphaBias
        itemView.translationY = scrolled[0] * 3f * motionBias
        itemView.rotationX = -min(20f, 20f * scrolled[0] / itemView.height * (1f - motionBias))
    }

}