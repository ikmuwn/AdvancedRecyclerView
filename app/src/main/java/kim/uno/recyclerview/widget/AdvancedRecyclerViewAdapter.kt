package kim.uno.recyclerview.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class AdvancedRecyclerViewAdapter : RecyclerView.Adapter<AdvancedViewHolder<Any>>() {

    open var recyclerView: RecyclerView? = null
        set(value) {
            field = value
            if (this is InfiniteRecyclerViewAdapter) {
                (value?.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(initPosition, 0)
            }
        }
    var items = ArrayList<Pair<Int, Any>>()
    val holders = ArrayList<AdvancedViewHolder<*>>()

    @Suppress("UNCHECKED_CAST")
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvancedViewHolder<Any> =
            onCreateHolder(viewType) as AdvancedViewHolder<Any>

    abstract fun onCreateHolder(viewType: Int): AdvancedViewHolder<*>

    override fun getItemCount() = items.size

    fun get(position: Int) = items.getOrNull(positionCalibrate(position))

    fun getItem(position: Int) = items[positionCalibrate(position)].second

    final override fun onBindViewHolder(holder: AdvancedViewHolder<Any>, position: Int) {
        holder.onBindView(getItem(position), positionCalibrate(position))
    }

    override fun onBindViewHolder(holder: AdvancedViewHolder<Any>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else
            holder.onBindView(getItem(position), positionCalibrate(position), payloads)
    }

    override fun getItemViewType(position: Int): Int = items[positionCalibrate(position)].first

    open fun positionCalibrate(position: Int) = position

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
        holders.remove(holder)
        super.onViewDetachedFromWindow(holder)
    }

    fun notifyDataSetChanged(detectMoves: Boolean = true, unit: (AdvancedRecyclerViewAdapter) -> Unit): AdvancedRecyclerViewAdapter {
        val transactionPairs = ArrayList(items)
        unit(this)

        if (transactionPairs.size == 0) {
            notifyDataSetChanged()
        } else {
            DiffUtil.calculateDiff(
                    AdvancedRecyclerViewDiffCallback(transactionPairs, items),
                    detectMoves
            ).dispatchUpdatesTo(this)
        }

        if (this is InfiniteRecyclerViewAdapter) {
            when (val layoutManager = recyclerView?.layoutManager) {
                is LinearLayoutManager -> layoutManager.scrollToPositionWithOffset(initPosition, 0)
                is StaggeredGridLayoutManager -> layoutManager.scrollToPositionWithOffset(initPosition, 0)
            }
        }

        return this@AdvancedRecyclerViewAdapter
    }

    open fun add(index: Int = items.size, item: Any? = null, viewType: Int = 0) {
        items.add(index, viewType to (item ?: Any()))
    }

    open fun add(index: Int = items.size, pair: Pair<Int, Any>) {
        items.add(index, pair)
    }

    fun addAll(index: Int = this.items.size, items: ArrayList<*>?, viewType: Int = 0) {
        items?.forEachIndexed { i, item -> add(index = index + i, item = item, viewType = viewType) }
    }

    fun remove(item: Any): Boolean {
        return items.firstOrNull {
            it.second == item
        }?.let {
            items.remove(it)
        } ?: false
    }

    fun removeAt(index: Int): Boolean {
        val hasIndex = index > -1 && index < items.size
        if (hasIndex) items.removeAt(index)
        return hasIndex
    }

    fun contains(item: Any): Boolean {
        return items.firstOrNull { it.second == item } != null
    }

    open fun clear() {
        items.clear()
    }

}