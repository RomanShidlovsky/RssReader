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
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#333333")))

        bottomNavView = findViewById(R.id.bottom_navigation)
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    if (supportFragmentManager.findFragmentByTag("rssFragment") == null) {
                        val prefs = getSharedPreferences("rss_urls", Context.MODE_PRIVATE)
                        val urls = prefs.getStringSet("urls", setOf()) ?: emptySet()
                        val rssFragment = RssFragment()

                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_root, rssFragment, "rssFragment")
                            .commit()
                    }
                    true
                }
                R.id.action_settings -> {
                    if (supportFragmentManager.findFragmentByTag("settingsFragment") == null) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_root, SettingsFragment(), "settingsFragment")
                            .commit()
                    }
                    true
                }
                else -> false
            }
        }

        // Restore the last shown fragment if it exists
        if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(savedInstanceState.getString("lastFragmentTag"))?.let { fragment ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_root, fragment, savedInstanceState.getString("lastFragmentTag"))
                    .commit()
            }
        } else {
            // Show the initial fragment
            bottomNavView.selectedItemId = R.id.action_home
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the last shown fragment's tag
        val lastFragment = supportFragmentManager.findFragmentById(R.id.fragment_root)
        if (lastFragment != null) {
            outState.putString("lastFragmentTag", lastFragment.tag)
        }
    }
}