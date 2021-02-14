package kim.uno.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kim.uno.recyclerview.ui.fragment.AdapterBindingSampleFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, AdapterBindingSampleFragment.newInstance())
//                .replace(R.id.container, WithoutAdapterSampleFragment.newInstance())
                .commitNow()
    }
}