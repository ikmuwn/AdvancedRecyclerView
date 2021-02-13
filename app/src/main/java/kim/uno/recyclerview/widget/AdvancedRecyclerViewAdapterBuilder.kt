package kim.uno.recyclerview.widget

import androidx.collection.ArrayMap

class AdvancedAdapterBuilder {

    private val adapter = object : AdvancedRecyclerViewAdapter() {
        override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
            return createUnit[viewType]!!()
        }
    }

    private val createUnit: ArrayMap<Int, () -> AdvancedViewHolder<*>> by lazy {
        ArrayMap<Int, () -> AdvancedViewHolder<*>>()
    }

    fun <ITEM> addHolder(
            viewType: Int = 0,
            resId: Int,
            dataBinding: Boolean = false,
            binder: ((AdvancedViewHolder<ITEM>) -> Unit)? = null,
            scroll: ((AdvancedViewHolder<ITEM>) -> Unit)? = null): AdvancedAdapterBuilder {
        createUnit[viewType] = {
            if (dataBinding) {
                lateinit var holder: AdvancedViewHolder<ITEM>
                holder = object : AdvancedViewBindingHolder<ITEM>(adapter, resId) {
                    override fun onBindView(item: ITEM, position: Int, payloads: MutableList<Any>) {
                        super.onBindView(item, position, payloads)
                        binder?.invoke(holder)
                    }

                    override fun onScrollChanged() {
                        super.onScrollChanged()
                        scroll?.invoke(this)
                    }
                }
                holder
            } else {
                lateinit var holder: AdvancedViewHolder<ITEM>
                holder = object : AdvancedViewHolder<ITEM>(adapter, resId) {
                    override fun onBindView(item: ITEM, position: Int, payloads: MutableList<Any>) {
                        super.onBindView(item, position, payloads)
                        binder?.invoke(holder)
                    }

                    override fun onScrollChanged() {
                        super.onScrollChanged()
                        scroll?.invoke(this)
                    }
                }

                holder
            }
        }
        return this@AdvancedAdapterBuilder
    }

    fun build() = adapter.apply { }

}

fun adapterOf() = AdvancedAdapterBuilder()
