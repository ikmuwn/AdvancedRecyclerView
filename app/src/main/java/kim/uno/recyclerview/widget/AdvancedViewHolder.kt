package kim.uno.recyclerview.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

open class AdvancedViewHolder<ITEM>(val adapter: AdvancedRecyclerViewAdapter, itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    constructor(adapter: AdvancedRecyclerViewAdapter, resId: Int)
            : this(adapter, LayoutInflater.from(adapter.recyclerView!!.context)
            .inflate(resId, adapter.recyclerView, false))

    val inflater: LayoutInflater
        get() = LayoutInflater.from(adapter.recyclerView!!.context)

    open fun onBindView(item: ITEM, position: Int) {
        onScrollChanged()
    }

    open fun onBindView(item: ITEM, position: Int, payloads: MutableList<Any>?) {
        onScrollChanged()
    }

    open fun onScrollChanged() {}

    val viewPosition: Int
        get() = adapter.positionCalibrate(adapterPosition)

    val item: ITEM
        @Suppress("UNCHECKED_CAST")
        get() = adapter.items[viewPosition].second as ITEM

    val beforeItemViewType: Int
        get() = if (viewPosition > 0) adapter.getItemViewType(viewPosition - 1) else -1

    val afterItemViewType: Int
        get() = if (viewPosition < adapter.itemCount - 1) adapter.getItemViewType(viewPosition + 1) else -1

    val context: Context
        get() = itemView.context

    val scroll: Int
        get() = if (adapter.recyclerView is AdvancedRecyclerView) {
            (adapter.recyclerView as AdvancedRecyclerView).scroll
        } else {
            null
        } ?: 0

    val scrolled: IntArray
        get() = if (adapter.recyclerView is AdvancedRecyclerView) {
            (adapter.recyclerView as AdvancedRecyclerView).scrolled
        } else {
            null
        } ?: IntArray(AdvancedRecyclerView.CACHED_SCROLL)

    val viewBias: Float
        get() = adapter.recyclerView?.layoutManager?.let { layoutManager ->
            var viewStart = 0
            var viewSize = 0
            var container = 0

            when {
                layoutManager.canScrollHorizontally() -> {
                    viewStart = itemView.left
                    viewSize = itemView.right - itemView.left
                    container = adapter.recyclerView!!.width
                }
                layoutManager.canScrollVertically() -> {
                    viewStart = itemView.top
                    viewSize = itemView.bottom - itemView.top
                    container = adapter.recyclerView!!.height
                }
            }

            if (container > 0 && viewSize > 0) {
                viewStart.toFloat() / (container - viewSize)
            } else {
                null
            }
        } ?: 0f

    val motionBias: Float
        get() = adapter.recyclerView?.layoutManager?.let { layoutManager ->
            if (adapter.recyclerView !is AdvancedRecyclerView) return viewBias

            var motion = 0f
            var viewStart = 0
            var viewSize = 0
            var container = 0

            when {
                layoutManager.canScrollHorizontally() -> {
                    motion = (adapter.recyclerView as AdvancedRecyclerView).motion[0]
                    viewStart = itemView.left
                    viewSize = itemView.right - itemView.left
                    container = adapter.recyclerView!!.width
                }
                layoutManager.canScrollVertically() -> {
                    motion = (adapter.recyclerView as AdvancedRecyclerView).motion[1]
                    viewStart = itemView.top
                    viewSize = itemView.bottom - itemView.top
                    container = adapter.recyclerView!!.height
                }
            }

            if (container > 0 && viewSize > 0 && motion > 0f) {
                val viewBias = viewStart.toFloat() / (container - viewSize)
                val motionBias = motion / container
                if (motion.toInt() in viewStart..viewStart + viewSize) 0f
                else abs(viewBias - motionBias)
            } else {
                null
            }
        } ?: 0f

}