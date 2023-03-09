package by.bsuir.rssreader

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.bottom_navigation

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var urls: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#333333")))

        prefs = getSharedPreferences("rss_urls", Context.MODE_PRIVATE)
        urls = prefs.getStringSet("urls", setOf())?.toMutableList() ?: mutableListOf()

        val adapter = ArrayAdapter(this, R.layout.list_item, urls)
        urlsListView.adapter = adapter

        addUrlButton.setOnClickListener {
            val url = urlEditText.text.toString()
            if (url.isNotEmpty()) {
                urls.add(url)
                adapter.notifyDataSetChanged()
                urlEditText.text.clear()
            }
        }

        urlsListView.setOnItemLongClickListener { parent, view, position, id ->
            urls.removeAt(position)
            adapter.notifyDataSetChanged()
            true
        }

        bottom_navigation.menu.findItem(R.id.action_settings).isChecked = true
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.action_settings -> {
                    // Do nothing, we're already on the settings screen
                    true
                }
                else -> false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        prefs.edit().putStringSet("urls", urls.toSet()).apply()
    }
}
