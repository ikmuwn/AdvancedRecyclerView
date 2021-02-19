# What is AdvancedRecyclerView

This aims to advance the use of `RecyclerView` for android.

* Simplify the use of `Multiple ViewHolder`
* It is easy to use `Data-binding` to ViewHolder
* You can easily apply `DiffUtil` as Annotation
* Various animations can be applied to the list (Inspired by `PageTransformer`)
* Infinite loop scroll (Inspired by `InfiniteViewPager`)

<br/><br/>

## It supports

The extends relationship must be replaced.

* RecyclerView -> `AdvancedRecyclerView`
* RecyclerView.Adapter -> `AdvancedRecyclerViewAdapter`
* RecyclerView.ViewHolder -> `AdvancedViewHolder`

<br/><br/>

## Demo; Scroll with tension

![Screenshot](https://github.com/ikmuwn/AdvancedRecyclerView/raw/master/Screenshot.gif)

<br/>

Alpha, Scroll interpolate, Rotation

```kotlin
class ModelHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedViewHolder<Model>(adapter, R.layout.model_holder) {

    private val modelHolderBinding by lazy { ModelHolderBinding.bind(itemView) }

    override fun onBindView(item: Model, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        modelHolderBinding.item = item
    }

    override fun onScrollChanged() {
        super.onScrollChanged()
        val alphaBias = 1f - abs(viewBias - 0.5f) / 0.5f
        modelHolderBinding.root.alpha = 0.5f + 0.5f * alphaBias
        modelHolderBinding.root.translationY = scrolled[0] * 3f * motionBias
        modelHolderBinding.root.rotationX = -min(20f, 20f * scrolled[0] / itemView.height * (1f - motionBias))
    }

}
```

<br/><br/>

## Demo; Parallax scroll

![Screenshot](https://github.com/ikmuwn/AdvancedRecyclerView/raw/master/Screenshot-1.gif)

<br/>

`onScrollChanged` Alpha, Scroll interpolate, Rotation

```kotlin
class ModelHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedViewHolder<Model>(adapter, R.layout.model_holder) {

    private val modelHolderBinding by lazy { ModelHolderBinding.bind(itemView) }
    private val parallaxRange = (itemView.iv_parallax.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin / 2

    override fun onBindView(item: Model, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        modelHolderBinding.item = item
    }

    override fun onScrollChanged() {
        super.onScrollChanged()
        modelHolderBinding.iv_parallax.translationY = parallaxRange * viewBias
    }

}
```

<br/><br/>

## Demo; Infinite loop scroll

![Screenshot](https://github.com/ikmuwn/AdvancedRecyclerView/raw/master/Screenshot-2.gif)

<br/>

`InfiniteRecyclerViewAdapter` Just change it

```kotlin
class ModelAdapter: InfiniteRecyclerViewAdapter() // AdvancedRecyclerViewAdapter
```

<br/><br/>

## How to support animator

### AdvancedViewRecyclerView

`scrollUnit : (Int) -> Unit` As you scroll, values are passed by orientation

```kotlin
recyclerView.scrollUnit = { scroll ->
    // TODO
}
```

<br/>

### AdvancedViewRecyclerView, AdvancedViewHolder

`scroll: Int` Scroll value from start <br/>
`scrolled: Array<Int>(10)` Recent the difference from the previous scroll value <br/>
`viewBias: Float` itemView bias on the screen (start 0f ~ 1f end) <br/>
`motionBias: Float` itemView bias on the screen from touch point (start 0f ~ 1f end)

<br/>

### Fling effect

`flingEnable: Boolean` Fling effect usable (default: false) <br/>
`flingOffset: Int` Start margin of first child <br/>
`flingGravity: Int` Fling gravity (start, center, right) <br/>

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
    : AdvancedViewHolder<Model>(adapter, R.layout.model_holder) {

    private val modelHolderBinding by lazy { ModelHolderBinding.bind(itemView) }

    override fun onBindView(item: Model, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        modelHolderBinding.item = item
    }

    override fun onScrollChanged() {
        super.onScrollChanged()
        val alphaBias = 1f - abs(viewBias - 0.5f) / 0.5f
        modelHolderBinding.root.alpha = 0.5f + 0.5f * alphaBias
        modelHolderBinding.root.translationY = scrolled[0] * 3f * motionBias
        modelHolderBinding.root.rotationX = -min(20f, 20f * scrolled[0] / itemView.height * (1f - motionBias))
    }

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
            name="item"
            type="kim.uno.recyclerview.ui.fragment.Model" />
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
            android:text="@{item.title}"
            android:textStyle="bold"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="5dp"
            android:text="@{item.description}"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/title" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

<br/>

### Main adapter setup

#### OPTION 1 (Make adapter, holder)

```kotlin
recyclerview.adapter = ModelAdapter()
```

```kotlin
class ModelAdapter: AdvancedRecyclerViewAdapter() {
    override fun onCreateHolder(viewType: Int): AdvancedViewHolder<*> {
        return ModelHolder(this)
    }
}
```

```kotlin
class ModelHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedViewHolder<Model>(adapter, R.layout.model_holder) {

    private val modelHolderBinding by lazy { ModelHolderBinding.bind(itemView) }

    override fun onBindView(item: Model, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        modelHolderBinding.item = item
    }
}
```

#### OPTION 2 (Use builder adapter. make holder)

```kotlin
recyclerview.adapter = adapterOf()
    .addHolder(
        viewType = 0, 
        holder = ModelHolder::class.java)
    .build()
```

```kotlin
class ModelHolder(adapter: AdvancedRecyclerViewAdapter)
    : AdvancedViewHolder<Model>(adapter, R.layout.model_holder) {

    private val modelHolderBinding by lazy { ModelHolderBinding.bind(itemView) }

    override fun onBindView(item: Model, position: Int, payloads: MutableList<Any>) {
        super.onBindView(item, position, payloads)
        modelHolderBinding.item = item
    }
}
```

#### OPTION 3 (Use builder adapter, holder with view binding)

```kotlin
val binding = ModelHolderBinding.inflate(layoutInflater)
recyclerview.adapter = adapterOf()
    .addHolder<Model, ModelHolderBinding>(
        viewType = 0,
        binding = binding,
        binder = { holder ->
            binding.item = holder.item
        })
.build()
```

#### ~OPTION 4 (Use builder adapter, holder)~

The 'kotlin-android-extensions' Gradle plugin is deprecated.

```kotlin
val binding = ModelHolderBinding.inflate(layoutInflater)
recyclerview.adapter = adapterOf()
    .addHolder<Model>(
        viewType = 0,
        resId = R.layout.model_holder,
        binder = { holder ->
            holder.itemView.title.text = holder.item.title
            holder.itemView.description.text = holder.item.description
        })
    .build()
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
DiffUtil `calculateDiff` and `dispatchUpdatesTo` work automatically

#### Inject annotation into the model

`@ItemDiff` DiffUtil.areItemsTheSame <br/>
`@ContentsDiff` DiffUtil.areContentsTheSame

```kotlin
data class Model(

    @ItemDiff
    var id: String,

    @ContentsDiff
    var title: String,

    @ContentsDiff
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