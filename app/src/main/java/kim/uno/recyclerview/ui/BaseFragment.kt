package kim.uno.recyclerview.ui

import android.os.Bundle
import android.util.Log
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
    ): View? {
        Log.i("uno", "onCreateView ")
        if (!::itemView.isInitialized) {
            val view = onCreateViewOnce(inflater)
            if (view != null) itemView = view
            else return super.onCreateView(inflater, container, savedInstanceState)
        }
        return itemView
    }

    open fun onCreateViewOnce(inflater: LayoutInflater): View? = null

}