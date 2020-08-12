package kim.uno.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kim.uno.recyclerview.ui.main.UseBuilderSampleFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, UseBasicFragment.newInstance())
//                    .commitNow()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, UseBuilderSampleFragment.newInstance())
                    .commitNow()
        }
    }
}