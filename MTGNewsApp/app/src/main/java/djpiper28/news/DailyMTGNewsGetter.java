package djpiper28.news;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DailyMTGNewsGetter implements NewsGetterInterface {

    private static final String siteRoot = "https://magic.wizards.com";
    private static final String site = "https://magic.wizards.com/en/rss/rss.xml?tags=Daily%20MTG&lang=en";
    private static final String user = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:73.0) Gecko/20100101 Firefox/73.0";

    public List<NewsItem> getNews(NewsProgressListener listener) throws IOException {
        List<NewsItem> output = new LinkedList<NewsItem>();

        try {
            Connection conn = Jsoup.connect(site/*.replace("langcode", Locale.getDefault().getISO3Language().split("-")[0].toLowerCase())*/);
            Document doc = conn.userAgent(user).get();

            Elements items = doc.getElementsByTag("item");

            Log.i("Info", "Downloaded article info, " + items.size() + " articles to check.\n");

            int i = 0;
            for (Element item : items) {
                try {
                    Log.i("Info", "Article " + (i + 1) + " out of " + items.size());
                    i++;

                    String description = "";
                    try {
                        description = Jsoup.parse(item.getElementsByTag("description").get(0).text()).getElementsByTag("p")
                                .get(0).text();
                    } catch (Exception e) {
                        description = item.getElementsByTag("description").get(0).text().split("<a")[0].split("</a")[0];
                    }
                    if (description.contains("<") || description.contains(">")) {
                        description = Jsoup.parse(description).text();
                    }

                    String imageURL = "https://magic.wizards.com/sites/all/themes/wiz_mtg/images/global/placeholder-500.jpg";
                    if (item.outerHtml().contains(";img src=\"")) {
                        imageURL = item.outerHtml().split(";img src=\"")[1].split(".jpg\"")[0] + ".jpg";
                    }

                    output.add(new NewsItem(item.getElementsByTag("title").get(0).text(), description,
                            item.getElementsByTag("dc:creator").get(0).text(),
                            item.getElementsByTag("pubDateString").get(0).text(),
                            siteRoot + item.getElementsByTag("link").text(), imageURL));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final int temp = i;
                (new Thread(() -> {
                    listener.progress(temp, items.size());
                })).start();
            }

            Log.i("Info", "Found " + output.size() + " articles");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Info", "Error downloading news.");
            throw e;
        }

        output.add(new NewsItem("Article Archive", "The archive has all of the old articles which you may be looking for.",
                "", "", "https://magic.wizards.com/en/articles/archive",
                "https://media.magic.wizards.com/images/featured/EN_AllArticles_Header_1.jpg"));

        return output;
    }

}