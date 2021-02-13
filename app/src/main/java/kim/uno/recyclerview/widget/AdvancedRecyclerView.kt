package kim.uno.recyclerview.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kim.uno.recyclerview.R

class AdvancedRecyclerView : RecyclerView {

    companion object {
        const val CACHED_SCROLL = 10
        const val FLING_GRAVITY_START = -1
        const val FLING_GRAVITY_CENTER = 0
        const val FLING_GRAVITY_END = 1
    }

    var scroll = 0
    var scrolled = IntArray(CACHED_SCROLL)
    var scrollUnit: ((Int) -> Unit)? = null

    var motion = FloatArray(2)
    var motionIdleAnimator: Animator? = null

    var currentItem: Int
        get() = when (val layoutManager = layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val positions = IntArray(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(positions)
                positions[0]
            }
            else -> 0
        }
        set(value) {
            smoothScrollToPosition(value)
        }

    val completelyStartItem: Int
        get() = when (val layoutManager = layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstCompletelyVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val positions = IntArray(layoutManager.spanCount)
                layoutManager.findFirstCompletelyVisibleItemPositions(positions)
                positions[0]
            }
            else -> 0
        }

    val completelyEndItem: Int
        get() = when (val layoutManager = layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastCompletelyVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val positions = IntArray(layoutManager.spanCount)
                layoutManager.findLastCompletelyVisibleItemPositions(positions)
                positions[0]
            }
            else -> 0
        }

    var flingEnable: Boolean = false
    var flingGravity = FLING_GRAVITY_START
    var flingOffset: Int = 0

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                layoutManager?.let { layoutManager ->

                    var isHorizontally = when {
                        layoutManager.canScrollHorizontally() -> true
                        layoutManager.canScrollVertically() -> false
                        else -> null
                    } ?: return

                    val canScrollToStart = if (isHorizontally) canScrollHorizontally(-1) else canScrollVertically(-1)
                    val canScrollToEnd = if (isHorizontally) canScrollHorizontally(1) else canScrollVertically(1)
                    motionIdle(!(canScrollToStart && canScrollToEnd))

                    val changed = if (isHorizontally) dx else dy
                    scroll = if (!canScrollToStart) 0 else scroll + changed
                    scrolled.indices.reversed().forEach { index ->
                        scrolled[index] = normalize(scrolled[index], if (index == 0) changed else scrolled[index - 1])
                    }

                    scrollUnit?.invoke(scroll)
                    if (adapter is AdvancedRecyclerViewAdapter) {
                        (adapter as AdvancedRecyclerViewAdapter).holders.forEach { holder -> holder.onScrollChanged() }
                    }
                }
            }
        })
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdvancedRecyclerView)
        flingOffset = typedArray.getDimensionPixelSize(R.styleable.AdvancedRecyclerView_flingOffset, 0)
        flingEnable = typedArray.getBoolean(R.styleable.AdvancedRecyclerView_flingEnable, false)
        flingGravity = typedArray.getInt(R.styleable.AdvancedRecyclerView_flingGravity, FLING_GRAVITY_START)
        typedArray.recycle()
    }

    fun setOrientation(orientation: Int) {
        when (layoutManager) {
            is LinearLayoutManager -> (layoutManager as LinearLayoutManager).orientation = orientation
            is StaggeredGridLayoutManager -> (layoutManager as StaggeredGridLayoutManager).orientation = orientation
        }
    }

    fun isHorizontally(): Boolean {
        return layoutManager?.let {
            it.canScrollHorizontally()
        } ?: false
    }

    fun isVertically(): Boolean {
        return layoutManager?.let {
            it.canScrollVertically()
        } ?: false
    }

    override fun getAdapter(): AdvancedRecyclerViewAdapter? {
        return super.getAdapter() as? AdvancedRecyclerViewAdapter
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            motion[0] = it.x
            motion[1] = it.y
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        motionIdle(state == SCROLL_STATE_IDLE)
    }

    fun motionIdle(idle: Boolean, delay: Long = 0L) {
        if (!idle) {
            motionIdleAnimator?.let { animator ->
                animator.removeAllListeners()
                animator.cancel()
                motionIdleAnimator = null
            }
        } else if (motionIdleAnimator == null) {
            motionIdleAnimator = ValueAnimator.ofInt(scrolled[0], 0).apply {
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val changed = it.animatedValue as Int

                    scrolled.indices.reversed().forEach { index ->
                        scrolled[index] = normalize(scrolled[index], if (index == 0) changed else scrolled[index - 1])
                    }

                    if (adapter is AdvancedRecyclerViewAdapter) {
                        (adapter as AdvancedRecyclerViewAdapter).holders.forEach { holder -> holder.onScrollChanged() }
                    }
                }

                startDelay = delay
                duration = 150L
                start()
            }
        }
    }

    private fun normalize(before: Int, after: Int): Int {
        return when {
            after > 0 && before + after < after -> before + after
            after < 0 && before + after > after -> before + after
            else -> after
        }
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        if (flingEnable && layoutManager is LinearLayoutManager) {
            val layoutManager = layoutManager as LinearLayoutManager
            val firstItem = layoutManager.findFirstVisibleItemPosition()
            val lastItem = layoutManager.findLastVisibleItemPosition()

            when {
                isHorizontally() -> {
                    when (flingGravity) {
                        FLING_GRAVITY_START -> {
                            val first = layoutManager.findViewByPosition(firstItem)!!
                            val after = layoutManager.findViewByPosition(firstItem + 1)!!
                            val offset = paddingLeft + flingOffset
                            val scroll = if (velocityX < 0) first.right - offset - first.width else after.left - offset
                            smoothScrollBy(scroll, 0)
                        }
                        FLING_GRAVITY_CENTER -> {
                            val last = layoutManager.findViewByPosition(lastItem)!!
                            val offset = (measuredWidth - last.width) / 2 + flingOffset

                            var center: View? = null
                            for (i in 0 until childCount) {
                                val child = getChildAt(i)
                                if (child.left > offset) {
                                    if (velocityX >= 0) center = child
                                    break
                                }
                                center = child
                            }

                            val scroll = center!!.left - offset
                            smoothScrollBy(scroll, 0)
                        }
                        FLING_GRAVITY_END -> {
                            val before = layoutManager.findViewByPosition(lastItem - 1)!!
                            val last = layoutManager.findViewByPosition(lastItem)!!
                            val offset = measuredWidth - last.width - flingOffset - paddingRight
                            val scroll = if (velocityX < 0) before.right - offset - before.width else last.left - offset
                            smoothScrollBy(scroll, 0)
                        }
                    }
                }

                isVertically() -> {
                    when (flingGravity) {
                        FLING_GRAVITY_START -> {
                            val first = layoutManager.findViewByPosition(firstItem)!!
                            val after = layoutManager.findViewByPosition(firstItem + 1)!!
                            val offset = paddingTop + flingOffset
                            val scroll = if (velocityY < 0) first.bottom - offset - first.height else after.top - offset
                            smoothScrollBy(0, scroll)
                        }
                        FLING_GRAVITY_CENTER -> {
                            val last = layoutManager.findViewByPosition(lastItem)!!
                            val offset = (measuredHeight - last.height) / 2 + flingOffset

                            var center: View? = null
                            for (i in 0 until childCount) {
                                val child = getChildAt(i)
                                if (child.top > offset) {
                                    if (velocityY >= 0) center = child
                                    break
                                }
                                center = child
                            }

                            scroll = center!!.top - offset
                            smoothScrollBy(0, scroll)
                        }
                        FLING_GRAVITY_END -> {
                            val before = layoutManager.findViewByPosition(lastItem - 1)!!
                            val last = layoutManager.findViewByPosition(lastItem)!!
                            val offset = measuredHeight - last.height - flingOffset - paddingBottom
                            val scroll = if (velocityY < 0) before.bottom - offset - before.height else last.top - offset
                            smoothScrollBy(0, scroll)
                        }
                    }
                }
            }

            return true
        } else {
            return super.fling(velocityX, velocityY)
        }
    }

}