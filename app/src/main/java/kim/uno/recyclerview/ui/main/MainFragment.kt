package kim.uno.recyclerview.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kim.uno.recyclerview.R
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var itemView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false).also { itemView = it }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        itemView.recycler_view.adapter = ModelAdapter().apply {
            viewModel = ViewModelProviders.of(this@MainFragment).get(MainViewModel::class.java)
            viewModel.models(this@MainFragment) { models ->
                notifyDataSetChanged {
                    it.addAll(items = models!!)
                }
            }
        }
    }

}