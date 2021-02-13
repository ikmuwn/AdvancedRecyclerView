package kim.uno.recyclerview.ui.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import kim.uno.recyclerview.R
import kim.uno.recyclerview.data.Model
import kim.uno.recyclerview.databinding.MainFragmentBinding
import kim.uno.recyclerview.ui.BaseFragment
import kim.uno.recyclerview.ui.MainViewModel
import kim.uno.recyclerview.widget.adapterOf

class WithoutAdapterSampleFragment : BaseFragment() {

    companion object {
        fun newInstance() = WithoutAdapterSampleFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateViewOnce(inflater: LayoutInflater): View? {
        binding = MainFragmentBinding.inflate(inflater)
        loadModels()
        return binding.root
    }

    private fun loadModels() {
        val adapter = adapterOf()
                .addHolder<Model>(
                        viewType = 0,
                        resId = R.layout.model_holder,
                        dataBinding = true)
                .build()
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged {
            it.addAll(items = viewModel.loadModels())
        }
    }

}