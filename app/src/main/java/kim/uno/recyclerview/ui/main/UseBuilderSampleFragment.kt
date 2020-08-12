package kim.uno.recyclerview.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kim.uno.recyclerview.R
import kim.uno.recyclerview.widget.adapterOf
import kotlinx.android.synthetic.main.main_fragment.view.*

class UseBuilderSampleFragment : Fragment() {

    companion object {
        fun newInstance() = UseBuilderSampleFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var itemView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false).also { itemView = it }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        itemView.recycler_view.adapter = adapterOf()
                .addHolder<Model>(
                        viewType = 0,
                        resId = R.layout.model_holder,
                        dataBinding = true)
                .build()

        viewModel = ViewModelProviders.of(this@UseBuilderSampleFragment).get(MainViewModel::class.java)
        viewModel.models(this@UseBuilderSampleFragment) { models ->
            itemView.recycler_view.adapter!!.notifyDataSetChanged {
                it.addAll(viewType = 0, items = models!!)
            }
        }
    }

}