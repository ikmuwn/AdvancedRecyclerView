# What is AdvancedRecyclerView

This aims to advance the use of `RecyclerView`.

* Simplify the use of multiple `ViewHolder`
* It is easy to use data-binding to `ViewHolder`
* You can easily apply `DiffUtil` as `Annotation`
* Various animations can be applied to the list (Inspired by `PageTransformer`)

<br/><br/>

## Demo

![Screenshot](https://github.com/ikmuwn/AdvancedRecyclerView/raw/master/Screenshot.gif)
![Screenshot](https://github.com/ikmuwn/AdvancedRecyclerView/raw/master/Screenshot-scroll-changed.png)

Alpha, Scroll interpolate, Rotation

<br/><br/>

## How to use

### Main
```kotlin
val adapter = ModelAdapter()
recyclerView.adapter = adapter
adapter.transaction {
    it.addAll(items = models)
}
```

### Xml
```xml
<kim.uno.recyclerview.widget.AdvancedRecyclerView
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

### ModelAdapter
```kotlin
class ModelAdapter: AdvancedRecyclerViewAdapter() {

    override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
        return when (viewType) {
            0 -> ModelHolder(this)
            else -> null!!
        }
    }

}
```

### MainHolder with data-binding

use `AdvancedViewHolder` or `AdvancedDataBindingViewHolder` with data-binding <br/>
`viewBias` return itemView bias on the screen (start 0f ~ 1f end) <br/>
`motionBias` return itemView bias on the screen from touch point (start 0f ~ 1f end) <br/>
`AdvancedDataBindingViewHolder.getVariable()` If you use data-binding, you should return variable set

```kotlin
class ModelHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedDataBindingViewHolder<Model>(adapter, R.layout.model_holder) {

    // Scroll animator
    override fun onScrollChanged() {
        super.onScrollChanged()
        val alphaBias = 1f - abs(viewBias - 0.5f) / 0.5f
        itemView.alpha = 0.5f + 0.5f * alphaBias
        itemView.translationY = scrolled[0] * 3f * motionBias
        itemView.rotationX = -min(20f, 20f * scrolled[0] / itemView.height * (1f - motionBias))
    }

    // Data-binding
    override fun getVariable(): ArrayMap<Int, Any> {
        return ArrayMap<Int, Any>().apply {
            put(BR.model, item)
        }
    }

}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>

        <variable
            name="model"
            type="kim.uno.recyclerview.ui.main.Model" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:background="#fff"
        android:padding="20dp">

        <TextView
            android:id="@+id/title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{model.title}"
            android:textStyle="bold"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="5dp"
            android:text="@{model.description}"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/title" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

### Set data with DifUtill

Without adapter `notifyDataSetChanged` like something <br/>
DiffUtill `calculateDiff` and `dispatchUpdatesTo` work automatically

```kotlin
adapter.transaction { adapter ->
    // TODO adapter data change
    // ex) adapter.add(0), adapter.removeAt(1) ..
}
```

#### Adapter data change

```kotlin
// index: Add to the index
// item: bind data
// type: Create a holder of view type
adapter.add(index: Int, item: Any, type: Int)

// Add all to the index
adapter.addAll(index: Int, items: ArrayList<*>, type: Int)

// Remove a model
adapter.remove(item: Any)

// Remove at index
adatper.remoteAt(index: Int)
```

#### Model

`@ItemDiffField` DiffUtil.areItemsTheSame <br/>
`@ContentsDiffField` DiffUtil.areContentsTheSame

```kotlin
data class Model(

    @ItemDiffField
    var id: String,

    @ContentsDiffField
    var title: String,

    @ContentsDiffField
    var description: String,

    var createdDate: String

)
```