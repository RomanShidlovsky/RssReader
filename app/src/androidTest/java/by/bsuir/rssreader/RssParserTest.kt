package by.bsuir.rssreader

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.ByteArrayInputStream

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
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
        assertEquals(expectedRssItems.size, actualRssItems.size)

        for (i in actualRssItems.indices) {
            assertEquals(expectedRssItems[i].title, actualRssItems[i].title)
            assertEquals(expectedRssItems[i].link, actualRssItems[i].link)
            assertEquals(expectedRssItems[i].description, actualRssItems[i].description)
            assertEquals(expectedRssItems[i].imgLink, actualRssItems[i].imgLink)
            assertEquals(expectedRssItems[i].pubDate, actualRssItems[i].pubDate)
        }
    }
}

