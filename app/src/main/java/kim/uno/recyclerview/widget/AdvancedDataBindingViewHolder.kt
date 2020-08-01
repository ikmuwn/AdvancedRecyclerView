package kim.uno.recyclerview.widget

import androidx.collection.ArrayMap
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kim.uno.recyclerview.BR

abstract class AdvancedDataBindingViewHolder<ITEM>(adapter: AdvancedRecyclerViewAdapter, resId: Int)
    : AdvancedViewHolder<ITEM>(adapter, resId) {

    private var viewDataBinding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
    open fun getVariable(): ArrayMap<Int, Any> = ArrayMap<Int, Any>().apply {
        put(BR.item, item)
    }

    override fun onBindView(item: ITEM, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        viewDataBinding.apply {
            getVariable().forEach { variable -> setVariable(variable.key, variable.value) }
            executePendingBindings()
        }
    }

}