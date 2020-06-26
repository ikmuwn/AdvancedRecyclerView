package kim.uno.recyclerview.widget

import androidx.recyclerview.widget.DiffUtil

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ItemDiffField

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContentsDiffField

class AdvancedRecyclerViewDiffCallback(
        private val beforeItems: ArrayList<Any>,
        private val beforeTypes: ArrayList<Int>,
        private val afterItems: ArrayList<Any>,
        private val afterTypes: ArrayList<Int>
) : DiffUtil.Callback() {

    override fun getOldListSize() = beforeItems.size
    override fun getNewListSize() = afterItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areSame(oldItemPosition, newItemPosition, ItemDiffField::class.java, false)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areSame(oldItemPosition, newItemPosition, ContentsDiffField::class.java, true)
    }

    private fun areSame(oldItemPosition: Int, newItemPosition: Int, clazz: Class<out Annotation>, default: Boolean): Boolean {
        val beforeItem = beforeItems[oldItemPosition]
        val beforeType = beforeTypes[oldItemPosition]
        val afterItem = afterItems[newItemPosition]
        val afterType = afterTypes[newItemPosition]
        if (beforeItem.javaClass.name == afterItem.javaClass.name && beforeType == afterType) {
            var isAnnotationPresent = default

            beforeItem.javaClass.declaredFields
                    .filter { it.isAnnotationPresent(clazz) }
                    .forEach {
                        isAnnotationPresent = true

                        it.isAccessible = true
                        val beforeValue = it.get(beforeItem)
                        val afterValue = it.get(afterItem)
                        it.isAccessible = false

                        if (beforeValue != afterValue) {
                            return false
                        }
                    }

            return isAnnotationPresent
        }
        return false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val beforeItem = beforeItems[oldItemPosition]
        val afterItem = afterItems[newItemPosition]
        if (beforeItem.javaClass.name == afterItem.javaClass.name) {
            val payload = ArrayList<String>()
            beforeItem.javaClass.declaredFields
                    .filter { it.isAnnotationPresent(ContentsDiffField::class.java) }
                    .forEach {
                        it.isAccessible = true
                        val beforeValue = it.get(beforeItem)
                        val afterValue = it.get(afterItem)
                        if (beforeValue != afterValue) payload.add(it.name)
                        it.isAccessible = false
                    }

            if (payload.size > 0) return payload
        }
        return null
    }

}