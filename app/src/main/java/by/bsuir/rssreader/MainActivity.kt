package by.bsuir.rssreader

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var rssFragment: RssFragment
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#333333")))

        // Retrieve URLs from SharedPreferences
        val prefs = getSharedPreferences("rss_urls", Context.MODE_PRIVATE)
        val urls = prefs.getStringSet("urls", setOf()) ?: emptySet()

        rssFragment = RssFragment.newInstance(ArrayList(urls))

        bottomNavView = findViewById(R.id.bottom_navigation)
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    true
                }
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_root, rssFragment)
            .commit()
    }


}