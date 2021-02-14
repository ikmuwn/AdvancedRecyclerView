package kim.uno.recyclerview.ui

import kim.uno.recyclerview.R
import kim.uno.recyclerview.data.Model
import kim.uno.recyclerview.databinding.ModelHolderBinding
import kim.uno.recyclerview.widget.AdvancedRecyclerViewAdapter
import kim.uno.recyclerview.widget.AdvancedViewHolder
import kotlin.math.abs
import kotlin.math.min

class ModelHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedViewHolder<Model>(adapter, R.layout.model_holder) {

    private val modelHolderBinding by lazy { ModelHolderBinding.bind(itemView) }

    override fun onBindView(item: Model, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        modelHolderBinding.item = item
    }

    override fun onScrollChanged() {
        super.onScrollChanged()
        val alphaBias = 1f - abs(viewBias - 0.5f) / 0.5f
        modelHolderBinding.root.alpha = 0.5f + 0.5f * alphaBias
        modelHolderBinding.root.translationY = scrolled[0] * 3f * motionBias
        modelHolderBinding.root.rotationX = -min(20f, 20f * scrolled[0] / itemView.height * (1f - motionBias))
    }

}