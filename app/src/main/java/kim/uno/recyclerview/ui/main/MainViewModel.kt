package kim.uno.recyclerview.ui.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val models = MutableLiveData<ArrayList<Model>>()

    fun models(lifecycleOwner: LifecycleOwner, unit: (ArrayList<Model>?) -> Unit) {
        models.observe(lifecycleOwner, Observer { unit(it) })

        models.value = ArrayList<Model>().apply {
            for (i in 1..10) {
                add(Model(title = "$i, Hello", description = "description here"))
            }
        }
    }

}