package by.bsuir.rssreader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class RssItemRecyclerViewAdapter(
    var mValues: List<RssItem>,
    private val context: FragmentActivity?
) : RecyclerView.Adapter<RssItemRecyclerViewAdapter.RssImagelessItemViewHolder>() {

    companion object {
        val RSS_ITEM = 1
        val RSS_IMAGELESS_ITEM = 2
    }
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(link : String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return if (mValues[position].imgLink == "") {
            RSS_IMAGELESS_ITEM
        } else {
            RSS_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssImagelessItemViewHolder {
        return when (viewType) {
            RSS_IMAGELESS_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_rss_imageless, parent, false)
                    RssImagelessItemViewHolder(view)
            } else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_rss, parent, false)
                    RssItemViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RssImagelessItemViewHolder, position: Int) {
        val item = mValues[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int = mValues.size

    inner class RssItemViewHolder(itemView: View) : RssImagelessItemViewHolder(itemView) {
        private val featuredImg: ImageView? = itemView.findViewById(R.id.featuredImg)

        override fun bind(item: RssItem, listener: OnItemClickListener?) {
            super.bind(item, listener)

            val link = item.imgLink
            context?.let {
                featuredImg?.let { it1 ->
                    Glide.with(it)
                        .load(link)
                        .centerCrop()
                        .into(it1)
                }
            }
        }
    }

    open inner class RssImagelessItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: TextView? = itemView.findViewById(R.id.txtTitle)
        val linkTV: TextView? = itemView.findViewById(R.id.txtLink)
        val contentTV: TextView? = itemView.findViewById(R.id.txtContent)
        val pubDateTV: TextView? = itemView.findViewById(R.id.txtPubdate)

        open fun bind(item: RssItem, listener: OnItemClickListener?) {
            titleTV?.text = item.title
            linkTV?.text = item.link
            contentTV?.text  = item.description
            pubDateTV?.text = item.pubDate
            itemView.setOnClickListener { listener?.onItemClick(item.link)}
        }
    }
}