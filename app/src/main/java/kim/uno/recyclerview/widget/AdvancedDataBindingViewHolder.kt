package kim.uno.recyclerview.widget

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kim.uno.recyclerview.BR

open class AdvancedDataBindingViewHolder<ITEM>(adapter: AdvancedRecyclerViewAdapter, resId: Int)
    : AdvancedViewHolder<ITEM>(adapter, resId) {

    private var viewDataBinding: ViewDataBinding = DataBindingUtil.bind(itemView)!!

    open fun getVariable() = ArrayList<Pair<Int, *>>().apply {
        add(BR.item to item)
    }

    override fun onBindView(item: ITEM, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        viewDataBinding.apply {
            getVariable().forEach { variable -> setVariable(variable.first, variable.second) }
            executePendingBindings()
        }
    }

}