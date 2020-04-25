package djpiper28.mtgnewsapp.news;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import djpiper28.news.NewsItem;

public class NewsGetter {

    private static final String siteRoot = "https://magic.wizards.com";
    private static final String site = "https://magic.wizards.com/en/rss/rss.xml?tags=Daily%20MTG&lang=en";
    private static final String user = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:73.0) Gecko/20100101 Firefox/73.0";

    public List<djpiper28.news.NewsItem> getNews() throws IOException {
        List<djpiper28.news.NewsItem> output = new LinkedList<djpiper28.news.NewsItem>();
        Logger logger = Logger.getLogger("News Getter");

        try {
            Connection conn = Jsoup.connect(site);
            Document doc = conn.userAgent(user).get();

            Elements items = doc.getElementsByTag("item");

            Logger.getGlobal().log(Level.INFO, "Downloaded article info, " + items.size() + " articles to check.\n");

            int i = 0;
            for (Element item : items) {
                Logger.getGlobal().log(Level.INFO, "Article " + (i + 1) + " out of " + items.size());
                i++;

                String description = "";
                try {
                    description = Jsoup.parse(item.getElementsByTag("description").get(0).text()).getElementsByTag("p")
                            .get(0).text();
                } catch (Exception e) {
                    description = item.getElementsByTag("description").get(0).text().split("<a")[0].split("</a")[0];
                }

                String imageURL = item.outerHtml().split(";img src=\"")[1].split(".jpg\"")[0] + ".jpg";

                output.add(new NewsItem(item.getElementsByTag("title").get(0).text(), description,
                        item.getElementsByTag("dc:creator").get(0).text(),
                        item.getElementsByTag("pubDateString").get(0).text(),
                        siteRoot + item.getElementsByTag("link").text(), imageURL));
            }

            logger.log(Level.INFO, "Found " + output.size() + " articles");
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.INFO, "Error downloading news.");
            throw e;
        }
        return output;
    }
}