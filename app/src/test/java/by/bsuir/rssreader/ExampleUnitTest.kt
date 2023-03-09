package by.bsuir.rssreader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.io.InputStream
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


class RssParserTest {
    @Test
    fun parse_withValidXml_returnsRssItems() {
        // Arrange
        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <rss version="2.0">
                <channel>
                    <title>Example RSS Feed</title>
                    <link>https://example.com</link>
                    <description>An example RSS feed</description>
                    <item>
                        <title>Item 1</title>
                        <link>https://example.com/item1</link>
                        <description>Item 1 description</description>
                        <pubDate>Mon, 07 Mar 2023 12:00:00 +0000</pubDate>
                        <thumbnail url="https://example.com/item1.jpg"/>
                    </item>
                    <item>
                        <title>Item 2</title>
                        <link>https://example.com/item2</link>
                        <description>Item 2 description</description>
                        <pubDate>Tue, 08 Mar 2023 12:00:00 +0000</pubDate>
                        <thumbnail url="https://example.com/item2.jpg"/>
                    </item>
                </channel>
            </rss>
        """.trimIndent()
        val inputStream = ByteArrayInputStream(xml.toByteArray())
        val expectedRssItems = listOf(
            RssItem("Item 1", "https://example.com/item1", "Item 1 description",
                "Mon, 07 Mar 2023 12:00:00 +0000", "https://example.com/item1.jpg"),
            RssItem("Item 2", "https://example.com/item2", "Item 2 description",
                "Tue, 08 Mar 2023 12:00:00 +0000", "https://example.com/item2.jpg")
        )
        val rssParser = RssParser()

        // Act
        val actualRssItems = rssParser.parse(inputStream)

        // Assert
        assertEquals(expectedRssItems[0].title, "Item1")
    }
}

