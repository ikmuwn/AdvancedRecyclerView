package kim.uno.recyclerview.widget

import android.view.View
import androidx.collection.ArrayMap
import androidx.databinding.ViewDataBinding

class AdvancedAdapterBuilder {

    private val adapter = object : AdvancedRecyclerViewAdapter() {
        override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
            return createUnit[viewType]!!()
        }
    }

    private val createUnit: ArrayMap<Int, () -> AdvancedViewHolder<*>> by lazy {
        ArrayMap<Int, () -> AdvancedViewHolder<*>>()
    }

    fun <ITEM, HOLDER : AdvancedViewHolder<ITEM>> addHolder(
            viewType: Int = 0,
            holder: Class<HOLDER>): AdvancedAdapterBuilder {
        createUnit[viewType] = {
            holder.getConstructor(AdvancedRecyclerViewAdapter::class.java, View::class.java).newInstance()
        }
        return this
    }

    fun <ITEM, BINDING : ViewDataBinding> addHolder(
            viewType: Int = 0,
            binding: BINDING,
            init: ((AdvancedViewHolder<ITEM>) -> Unit)? = null,
            binder: ((AdvancedViewHolder<ITEM>) -> Unit)? = null,
            scroll: ((AdvancedViewHolder<ITEM>) -> Unit)? = null): AdvancedAdapterBuilder {
        createUnit[viewType] = {
            val holder = object : AdvancedViewHolder<ITEM>(adapter, binding.root) {
                init {
                    init?.invoke(this)
                }

                override fun onBindView(item: ITEM, position: Int, payloads: MutableList<Any>) {
                    super.onBindView(item, position, payloads)
                    binder?.invoke(this)
                }

                override fun onScrollChanged() {
                    super.onScrollChanged()
                    scroll?.invoke(this)
                }
            }
            holder
        }
        return this
    }

    fun <ITEM> addHolder(
            viewType: Int = 0,
            resId: Int,
            init: ((AdvancedViewHolder<ITEM>) -> Unit)? = null,
            binder: ((AdvancedViewHolder<ITEM>) -> Unit)? = null,
            scroll: ((AdvancedViewHolder<ITEM>) -> Unit)? = null): AdvancedAdapterBuilder {
        createUnit[viewType] = {
            lateinit var holder: AdvancedViewHolder<ITEM>
            holder = object : AdvancedViewHolder<ITEM>(adapter, resId) {
                init {
                    init?.invoke(this)
                }

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
        return this@AdvancedAdapterBuilder
    }

    fun build() = adapter.apply { }

}

fun adapterOf() = AdvancedAdapterBuilder()
