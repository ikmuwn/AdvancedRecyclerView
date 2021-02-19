package kim.uno.recyclerview.ui.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import kim.uno.recyclerview.databinding.MainFragmentBinding
import kim.uno.recyclerview.ui.BaseFragment
import kim.uno.recyclerview.ui.MainViewModel
import kim.uno.recyclerview.ui.ModelAdapter

class AdapterBindingSampleFragment : BaseFragment() {

    companion object {
        fun newInstance() = AdapterBindingSampleFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateViewOnce(inflater: LayoutInflater): View {
        binding = MainFragmentBinding.inflate(inflater)
        loadModels()
        return binding.root
    }

    private fun loadModels() {
        binding.recyclerView.adapter = ModelAdapter().notifyDataSetChanged {
            it.addAll(items = viewModel.loadModels())
        }
    }

}