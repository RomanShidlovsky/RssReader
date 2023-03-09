package by.bsuir.rssreader

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class RssParser {
    fun parse(inputStream: InputStream):List<RssItem> {
        val rssItemList = ArrayList<RssItem>()
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var currentTag = ""
            var title = ""
            var link = ""
            var description = ""
            var pubDate = ""
            var imageUrl = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        currentTag = parser.name
                        if ((currentTag == "content") || (currentTag == "thumbnail")) {
                            val tagAttrValue = parser.getAttributeValue(null, "url")
                            if (tagAttrValue != null) {
                                imageUrl = tagAttrValue
                            }
                        }
                    }
                    XmlPullParser.TEXT -> {
                        when (currentTag) {
                            "title" -> title = parser.text
                            "link" -> link = parser.text
                            "description" -> description = parser.text
                            "pubDate" -> pubDate = parser.text
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "item") {
                            val item = RssItem(title, link, description, pubDate, imageUrl)
                            rssItemList.add(item)

                            // Reset the variables for the next item
                            title = ""
                            link = ""
                            description = ""
                            pubDate = ""
                            imageUrl = ""
                        }
                        currentTag = ""
                    }
                }
                eventType = parser.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rssItemList
    }
}