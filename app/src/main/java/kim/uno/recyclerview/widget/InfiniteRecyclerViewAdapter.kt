package kim.uno.recyclerview.widget

abstract class InfiniteRecyclerViewAdapter : AdvancedRecyclerViewAdapter() {

    companion object {
        const val LOOP_INIT_POSITION = Int.MAX_VALUE / 2
    }

    private val loop: Boolean
        get() = items.size > 1

    private val loopOffset: Int
        get() = LOOP_INIT_POSITION % items.size

    val initPosition: Int
        get() = if (loop) LOOP_INIT_POSITION - loopOffset else 0

    override fun getItemCount() = if (loop) Int.MAX_VALUE else super.getItemCount()

    override fun positionCalibrate(position: Int): Int {
        return if (loop) position % items.size else position
    }

}