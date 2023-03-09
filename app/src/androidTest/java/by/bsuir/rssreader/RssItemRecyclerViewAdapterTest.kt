package by.bsuir.rssreader

import androidx.fragment.app.FragmentActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RssItemRecyclerViewAdapterTest {

    private lateinit var adapter: RssItemRecyclerViewAdapter

    @Mock
    private lateinit var mockContext: FragmentActivity

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val items = listOf(
            RssItem("title", "link", "description", "pubDate", "imgLink"),
            RssItem("title2", "link2", "description2", "pubDate2", "")
        )
        adapter = RssItemRecyclerViewAdapter(items, mockContext)
    }

    @Test
    fun testGetItemViewType_withImageLink_shouldReturnRssItem() {
        val viewType = adapter.getItemViewType(0)
        assertEquals(RssItemRecyclerViewAdapter.RSS_ITEM, viewType)
    }

    @Test
    fun testGetItemViewType_withoutImageLink_shouldReturnRssImagelessItem() {
        val viewType = adapter.getItemViewType(1)
        assertEquals(RssItemRecyclerViewAdapter.RSS_IMAGELESS_ITEM, viewType)
    }
}