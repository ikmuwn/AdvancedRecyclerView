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

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areSame(oldItemPosition, newItemPosition, ItemDiffField::class.java)
    }

    override fun getOldListSize() = beforeItems.size
    override fun getNewListSize() = afterItems.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areSame(oldItemPosition, newItemPosition, ContentsDiffField::class.java)
    }

    private fun areSame(oldItemPosition: Int, newItemPosition: Int, clazz: Class<out Annotation>): Boolean {
        val beforeItem = beforeItems[oldItemPosition]
        val beforeType = beforeTypes[oldItemPosition]
        val afterItem = afterItems[newItemPosition]
        val afterType = afterTypes[newItemPosition]
        if (beforeItem.javaClass.name == afterItem.javaClass.name && beforeType == afterType) {
            var annotationPresent = false
            beforeItem.javaClass.declaredFields
                    .filter { it.isAnnotationPresent(clazz) }
                    .forEach {
                        annotationPresent = true

                        it.isAccessible = true
                        val beforeValue = it.get(beforeItem)
                        val afterValue = it.get(afterItem)
                        it.isAccessible = false

                        if (beforeValue != afterValue) {
                            return false
                        }
                    }

            return annotationPresent
        } else {
            return false
        }
    }

}