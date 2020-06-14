# What is AdvancedRecyclerView

This aims to advance the use of `RecyclerView`.

* Simplify the use of multiple `ViewHolder`
* It is easy to use data-binding to `ViewHolder`
* You can easily apply `DiffUtil` as `Annotation`
* Various animations can be applied to the list (Inspired by `PageTransformer`)

<br/><br/>

## Demo

![Screenshot](https://github.com/ikmuwn/AdvancedRecyclerView/raw/master/Screenshot.gif)

### Sample animator

Alpha, Scroll interpolate, Rotation

```kotlin
override fun onScrollChanged() {
    super.onScrollChanged()
    val alphaBias = 1f - abs(viewBias - 0.5f) / 0.5f
    itemView.alpha = 0.5f + 0.5f * alphaBias
    itemView.translationY = scrolled[0] * 3f * motionBias
    itemView.rotationX = -min(20f, 20f * scrolled[0] / itemView.height * (1f - motionBias))
}
```

> `viewBias` return itemView bias on the screen (start 0f ~ 1f end) <br/>
> `motionBias` return itemView bias on the screen from touch point (start 0f ~ 1f end) <br/>

<br/><br/>

## How to use

### Layout

```xml
<kim.uno.recyclerview.widget.AdvancedRecyclerView
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

### ModelAdapter

To use multiple holders, return depending on the view type

```kotlin
class ModelAdapter: AdvancedRecyclerViewAdapter() {

    override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
        return ModelHolder(this)
    }

}
```

### ModelHolder

Instead of `RecyclerView.ViewHolder` <br/>
`AdvancedViewHolder` basic <br/>
`AdvancedDataBindingViewHolder` with data-binding

```kotlin
class ModelHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedDataBindingViewHolder<Model>(adapter, R.layout.model_holder) {

    override fun onScrollChanged() {
        super.onScrollChanged()
        val alphaBias = 1f - abs(viewBias - 0.5f) / 0.5f
        itemView.alpha = 0.5f + 0.5f * alphaBias
        itemView.translationY = scrolled[0] * 3f * motionBias
        itemView.rotationX = -min(20f, 20f * scrolled[0] / itemView.height * (1f - motionBias))
    }

    override fun getVariable() = ArrayMap<Int, Any>().apply { put(BR.model, item) }

}
```

> `onScrollChanged()` Responds to scroll changes
> `getVariable()` You should return variable-set for data-binding
> `viewBias` return itemView bias on the screen (start 0f ~ 1f end) <br/>
> `motionBias` return itemView bias on the screen from touch point (start 0f ~ 1f end) <br/>


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

### Main

```kotlin
recyclerview.adapter = ModelAdapter().apply {
    notifyDataSetChanged {
        it.addAll(items = models)
    }
}
```

### Adapter data change

#### Add

`adapter.add(index: Int, item: Any, type: Int)` <br/>
`adapter.addAll(index: Int, items: ArrayList<*>, type: Int)`

> `index` Add to the index
> `item` bind data
> `type` Create a holder of view type

#### Remove

`adapter.remove(item: Any)`
`adatper.remoteAt(index: Int)`

### Set data with DiffUtil

Without `adapter.notifyDataSetChanged()` <br/>
DiffUtill `calculateDiff` and `dispatchUpdatesTo` work automatically

#### Inject annotation into the model

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

#### NotifyDataSetChanged

`adapter.notifyDataSetChanged(unit)` Detect changes in this block

```kotlin
adapter.notifyDataSetChanged { adapter ->
    // TODO adapter data change
    // ex) adapter.add(0), adapter.removeAt(1) ..
}
```