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
                (value?.layoutManager as? LinearLayoutManager)?.let {
                    it.scrollToPositionWithOffset(initPosition, 0)
                }
            }
        }
    var items = ArrayList<Pair<Int, Any>>()
    val holders = ArrayList<AdvancedViewHolder<*>>()

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvancedViewHolder<Any> = onCreateHolder(viewType) as AdvancedViewHolder<Any>

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

    fun notifyDataSetChanged(detectMoves: Boolean = true, unit: (AdvancedRecyclerViewAdapter) -> Unit) {
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
    }

    open fun add(index: Int = items.size, item: Any? = null, type: Int = 0) {
        items.add(index, type to (item?: Any()))
    }

    fun addAll(index: Int = this.items.size, items: ArrayList<*>?, type: Int = 0) {
        items?.forEachIndexed { i, item -> add(index = index + i, item = item, type = type) }
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

    fun get(index: Int): Any {
        return try {
            items[index]
        } catch (e: Throwable) {
            Any()
        }
    }

    fun contains(item: Any): Boolean {
        return items.firstOrNull { it.second == item } != null
    }

    open fun clear() {
        items.clear()
    }

}