package by.bsuir.rssreader

class RssItem(
    var title : String,
    var link : String,
    var description : String,
    var pubDate: String,
    var imgLink: String
) {
    override fun toString(): String {
        return "RssItem(title='$title', link='$link', description='$description', pubDate='$pubDate', imgLink='$imgLink')"
    }
}