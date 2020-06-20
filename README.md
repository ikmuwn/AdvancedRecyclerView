# What is AdvancedRecyclerView

This aims to advance the use of `RecyclerView` for android.

* Simplify the use of `Multiple ViewHolder`
* It is easy to use `Data-binding` to ViewHolder
* You can easily apply `DiffUtil` as Annotation
* Various animations can be applied to the list (Inspired by `PageTransformer`)

<br/><br/>

## Demo

![Screenshot](https://github.com/ikmuwn/AdvancedRecyclerView/raw/master/Screenshot.gif)

<br/>

`onScrollChanged` Alpha, Scroll interpolate, Rotation

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

<br/><br/>

## How to supported for animator

### AdvancedViewRecyclerView

`scrollUnit : (Int) -> Unit` As you scroll, values are passed by orientation

```kotlin
recyclerView.scrollUnit = { scroll ->
    // TODO
}
```

<br/>

### AdvancedViewRecyclerView, AdvancedViewHolder

`viewBias: Float` itemView bias on the screen (start 0f ~ 1f end) <br/>
`motionBias: Float` itemView bias on the screen from touch point (start 0f ~ 1f end) <br/>
`scrolled: Array<Int>(10)` recent scrolled value

<br/><br/>

## How to use

### Layout

```xml
<kim.uno.recyclerview.widget.AdvancedRecyclerView
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

<br/>

### ModelAdapter

To use multiple holders, return depending on the view type

```kotlin
class ModelAdapter: AdvancedRecyclerViewAdapter() {

    override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
        return ModelHolder(this)

        /* multiple holders
        return when (viewType) {
            TYPE_1 -> ModelHolder1(this)
            TYPE_2 -> ModelHolder2(this)
            TYPE_3 -> ModelHolder3(this)
            else -> ModelHolder(this)
        }
        */
    }

}
```

<br/>

### ModelHolder

Instead of `RecyclerView.ViewHolder` <br/>
`AdvancedViewHolder` basic <br/>
`AdvancedDataBindingViewHolder : AdvancedViewHolder` with data-binding

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

> `onScrollChanged()` Responds to scroll changes <br/>
> `getVariable()` You should return variable-set for data-binding <br/>
> `viewBias` return itemView bias on the screen (start 0f ~ 1f end) <br/>
> `motionBias` return itemView bias on the screen from touch point (start 0f ~ 1f end) <br/>

<br/>

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

<br/>

### Main

```kotlin
recyclerview.adapter = ModelAdapter().apply {
    notifyDataSetChanged {
        it.addAll(items = models)
    }
}
```

<br/>

### Adapter data change

#### Add it to the adapter

`add(index: Int, item: Any, type: Int)` <br/>
`addAll(index: Int, items: ArrayList<*>, type: Int)`

> `index` Add to the index <br/>
> `item` bind data <br/>
> `type` Create a holder of view type

#### Remove it from the adapter

`remove(item: Any)` <br/>
`removeAt(index: Int)`

#### Clear

`clear()`

<br/>

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

#### Instead of NotifyDataSetChanged

`adapter.notifyDataSetChanged(unit)` Detect changes in this block

```kotlin
adapter.notifyDataSetChanged { adapter ->
    // TODO adapter data change
    // ex) adapter.add(0), adapter.removeAt(1) ..
}
```