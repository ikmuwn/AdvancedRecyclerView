package kim.uno.recyclerview.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdvancedRecyclerView : RecyclerView {

    companion object {
        const val CACHED_SCROLL = 10
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var scroll = 0
    var scrolled = IntArray(CACHED_SCROLL)
    var scrollUnit: ((Int) -> Unit)? = null

    var motion = FloatArray(2)
    private var motionIdleAnimator: Animator? = null

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

}