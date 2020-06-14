package kim.uno.recyclerview.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class AdvancedRecyclerViewAdapter : RecyclerView.Adapter<AdvancedViewHolder<Any>>() {

    var recyclerView: RecyclerView? = null
    var items = ArrayList<Any>()
    var types = ArrayList<Int>()

    val holders = ArrayList<AdvancedViewHolder<Any>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvancedViewHolder<Any> =
            onCreateHolder(viewType) as AdvancedViewHolder<Any>

    abstract fun onCreateHolder(viewType: Int): AdvancedViewHolder<*>

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: AdvancedViewHolder<Any>, position: Int) {
        holder.onBindView(items[position], position)
    }

    override fun getItemViewType(position: Int) = types[position]

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onViewAttachedToWindow(holder: AdvancedViewHolder<Any>) {
        super.onViewAttachedToWindow(holder)
        if (!holders.contains(holder)) holders.add(holder)
    }

    override fun onViewDetachedFromWindow(holder: AdvancedViewHolder<Any>) {
        super.onViewDetachedFromWindow(holder)
        holders.remove(holder)
    }

    fun notifyDataSetChanged(unit: (AdvancedRecyclerViewAdapter) -> Unit) {
        val transactionItems = ArrayList(items)
        val transactionTypes = ArrayList(types)

        unit(this)
        DiffUtil.calculateDiff(AdvancedRecyclerViewDiffCallback(
                transactionItems, transactionTypes,
                items, types
        )).dispatchUpdatesTo(this)
    }

    fun add(index: Int = items.size, item: Any = Any(), type: Int = 0) {
        items.add(index, item)
        types.add(index, type)
    }

    fun addAll(index: Int = this.items.size, items: ArrayList<*>, type: Int = 0) {
        items.forEachIndexed { i, item -> add(index = index + i, item = item, type = type) }
    }

    fun remove(item: Any): Boolean {
        items.indexOf(item).let { index ->
            return removeAt(index)
        }

        return false
    }

    fun removeAt(index: Int): Boolean {
        if (index > -1 && index < items.size) {
            items.removeAt(index)
            types.removeAt(index)
            return true
        }

        return false
    }


}