package by.bsuir.rssreader

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsFragment : Fragment() {
    companion object {
        private const val TEXT_VIEW_KEY = "text_view_key"
    }

    private lateinit var prefs: SharedPreferences
    private lateinit var urls: MutableList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_settings, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_VIEW_KEY, urlEditText.text.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(TEXT_VIEW_KEY, "")
            urlEditText.setText(savedText)
        }

        prefs = requireActivity().getSharedPreferences("rss_urls", Context.MODE_PRIVATE)
        urls = prefs.getStringSet("urls", setOf())?.toMutableList() ?: mutableListOf()

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, urls)
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
    }

    override fun onPause() {
        super.onPause()
        prefs.edit().putStringSet("urls", urls.toSet()).apply()
    }


}