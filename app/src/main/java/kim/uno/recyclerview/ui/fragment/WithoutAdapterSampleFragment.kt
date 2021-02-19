package kim.uno.recyclerview.ui.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import kim.uno.recyclerview.data.Model
import kim.uno.recyclerview.databinding.MainFragmentBinding
import kim.uno.recyclerview.databinding.ModelHolderBinding
import kim.uno.recyclerview.ui.BaseFragment
import kim.uno.recyclerview.ui.MainViewModel
import kim.uno.recyclerview.ui.ModelHolder
import kim.uno.recyclerview.widget.AdvancedRecyclerViewAdapter
import kim.uno.recyclerview.widget.adapterOf

class WithoutAdapterSampleFragment : BaseFragment() {

    companion object {
        fun newInstance() = WithoutAdapterSampleFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateViewOnce(inflater: LayoutInflater): View {
        binding = MainFragmentBinding.inflate(inflater)
        loadModels()
        return binding.root
    }

    private fun loadModels() {
        binding.recyclerView.adapter = getAdapterViewBinding()
                .notifyDataSetChanged {
                    it.addAll(items = viewModel.loadModels())
                }
    }

    private fun getAdapterUserHolder(): AdvancedRecyclerViewAdapter {
        return adapterOf()
                .addHolder(
                        viewType = 0,
                        holder = ModelHolder::class.java)
                .build()
    }

    private fun getAdapterViewBinding(): AdvancedRecyclerViewAdapter {
        val binding = ModelHolderBinding.inflate(layoutInflater)
        return adapterOf()
                .addHolder<Model, ModelHolderBinding>(
                        viewType = 0,
                        binding = binding,
                        binder = { holder ->
                            binding.item = holder.item
                        })
                .build()
    }

}