package kim.uno.recyclerview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private lateinit var itemView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::itemView.isInitialized) {
            val view = onCreateViewOnce(inflater)
            if (view != null) itemView = view
            else return null!!
        }
        return itemView
    }

    open fun onCreateViewOnce(inflater: LayoutInflater): View? = null

}