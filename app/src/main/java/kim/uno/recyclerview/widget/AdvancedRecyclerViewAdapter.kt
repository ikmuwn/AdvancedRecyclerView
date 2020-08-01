package kim.uno.recyclerview.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class AdvancedRecyclerViewAdapter : RecyclerView.Adapter<AdvancedViewHolder<Any>>() {

    var recyclerView: RecyclerView? = null
    var items = ArrayList<Pair<Int, Any>>()
    val holders = ArrayList<AdvancedViewHolder<*>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvancedViewHolder<Any> = onCreateHolder(viewType) as AdvancedViewHolder<Any>

    abstract fun onCreateHolder(viewType: Int): AdvancedViewHolder<*>

    override fun getItemCount() = items.size

    final override fun onBindViewHolder(holder: AdvancedViewHolder<Any>, position: Int) {
        holder.onBindView(items[positionCalibrate(position)].second, positionCalibrate(position))
    }

    override fun onBindViewHolder(holder: AdvancedViewHolder<Any>, position: Int, payloads: MutableList<Any>) {
        holder.onBindView(items[positionCalibrate(position)].second, positionCalibrate(position), payloads)
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

    fun notifyDataSetChanged(detectMoves: Boolean = true, scrollInit: Boolean = false, unit: (AdvancedRecyclerViewAdapter) -> Unit) {
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

        if (scrollInit) {
            recyclerView?.layoutManager?.let {
                val layoutManager = (it as LinearLayoutManager)
                val position = if (this is InfiniteRecyclerViewAdapter) initPosition else 0
                layoutManager.scrollToPositionWithOffset(position, 0)
            }
        }
    }

    fun add(index: Int = items.size, item: Any = Any(), type: Int = 0) {
        items.add(index, type to item)
    }

    fun addAll(index: Int = this.items.size, items: ArrayList<*>, type: Int = 0) {
        items.forEachIndexed { i, item -> add(index = index + i, item = item, type = type) }
    }

    fun remove(item: Any): Boolean {
        return items.firstOrNull {
            it.first == item
        }?.let {
            items.remove(it)
        } ?: false
    }

    fun removeAt(index: Int): Boolean {
        val hasIndex = index > -1 && index < items.size
        if (hasIndex) items.removeAt(index)
        return hasIndex
    }

    fun clear() {
        items.clear()
    }

}