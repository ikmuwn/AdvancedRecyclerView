package kim.uno.recyclerview.widget

import androidx.collection.ArrayMap
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class AdvancedDataBindingViewHolder<ITEM>(adapter: AdvancedRecyclerViewAdapter, resId: Int)
    : AdvancedViewHolder<ITEM>(adapter, resId) {

    private var viewDataBinding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
    abstract fun getVariable(): ArrayMap<Int, Any>

    override fun onBindView(item: ITEM, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        viewDataBinding.apply {
            getVariable().forEach { variable ->
                setVariable(variable.key, variable.value)
            }
            executePendingBindings()
        }
    }

}