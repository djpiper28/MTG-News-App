package djpiper28.mtgnewsapp.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class HipstersNewsGetter implements NewsGetterInterface {

    private static final String url = "https://www.hipstersofthecoast.com/feed/";

    @Override
    public List<NewsItem> getNews(NewsProgressListener listener) throws IOException {
        List<NewsItem> output = new LinkedList<NewsItem>();
        try {
            Document rssFeed = Jsoup.connect(url).get();
            Elements items = rssFeed.getElementsByTag("item");
            for (Element item : items) {
                try {
                    String title = item.getElementsByTag("title").get(0).text();
                    String link = item.getElementsByTag("link").get(0).text();
                    String author = item.getElementsByTag("dc:creator").get(0).html()
                            .split("<!\\[CDATA\\[ ")[1].split(" ]]>")[0];

                    String description = item.getElementsByTag("description").html()
                            .split("<!\\[CDATA\\[ ")[1].split(" ]]>")[0]
                            .replaceAll("<.*>", "");

                    String pubDate = item.getElementsByTag("pubDate").get(0).text().split(" +")[0];

                    output.add(new NewsItem(title, description, author, pubDate, link, ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}
