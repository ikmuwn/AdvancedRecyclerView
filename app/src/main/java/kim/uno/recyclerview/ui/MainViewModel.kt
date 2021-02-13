package kim.uno.recyclerview.ui

import androidx.lifecycle.ViewModel
import kim.uno.recyclerview.data.Model

class MainViewModel : ViewModel() {

    private val _models by lazy {
        ArrayList<Model>().apply {
            for (i in 1..10) {
                val model = Model(title = "${size + 1}, Hello", description = "description here")
                add(model)
            }
        }
    }

    fun loadModels(): ArrayList<Model> {
        return _models
    }

}