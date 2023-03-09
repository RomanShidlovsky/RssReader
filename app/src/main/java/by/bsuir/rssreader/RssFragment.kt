package by.bsuir.rssreader

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import by.bsuir.rssreader.WebViewActivity

class RssFragment : Fragment() {
    private var rssItems = ArrayList<RssItem>()
    private var listView : RecyclerView? = null
    private var adapter : RssItemRecyclerViewAdapter = RssItemRecyclerViewAdapter(rssItems, activity)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rss_list,
            container,false)

        listView = view.findViewById(R.id.listView)
        return view
    }

    fun updateRV(rssItemsL: List<RssItem>) {
            rssItems.addAll(rssItemsL)
            adapter?.notifyDataSetChanged()
    }

    class RssFeedFetcher(val context: RssFragment) : AsyncTask<URL, Void, List<RssItem>>() {
        private val reference = WeakReference(context)
        private var stream: InputStream? = null;

        override fun doInBackground(vararg params: URL?): List<RssItem>? {
           try {
               val connect = params[0]?.openConnection() as HttpsURLConnection
               connect.readTimeout = 8000
               connect.connectTimeout = 8000
               connect.requestMethod = "GET"
               connect.connect();

               val responseCode: Int = connect.responseCode;
               var rssItems: List<RssItem>? = null
               if (responseCode == 200) {
                   stream = connect.inputStream;

                   try {
                       val parser = RssParser()
                       rssItems = parser.parse(stream!!)

                   } catch (e: IOException) {
                       e.printStackTrace()
                   }
               }

               return rssItems
           } catch (e: java.lang.Exception)
           {
                e.printStackTrace()
           }
            return null
        }

        override fun onPostExecute(result: List<RssItem>?) {
            super.onPostExecute(result)
            if (result != null && result.isNotEmpty()) {
                reference.get()?.updateRV(result)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = RssItemRecyclerViewAdapter(rssItems, activity)
        adapter.setOnItemClickListener(object : RssItemRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(link: String) {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("url", link)
                startActivity(intent)
            }
        })

        listView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        listView?.adapter = adapter

       // val url = URL(RSS_FEED_LINK)


//        val urlsStr = arguments?.getStringArrayList(ARG_URLS) ?: arrayListOf()
//        val feedFetcher = RssFeedFetcher(this)
//
//        val urls = ArrayList<URL>()
//
//        urlsStr.forEach{
//            try {
//                val url = URL(it)
//                urls.add(url)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//
//        feedFetcher.execute(*urls.toTypedArray())
    }

    override fun onResume() {
        super.onResume()

        val prefs = requireContext().getSharedPreferences("rss_urls", Context.MODE_PRIVATE)
        val urlsStr = prefs.getStringSet("urls", setOf()) ?: emptySet()



        urlsStr.forEach{
            try {
                val url = URL(it)
                val feedFetcher = RssFeedFetcher(this)
                feedFetcher.execute(url)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}