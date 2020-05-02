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
//import java.util.regex.Pattern;

public class EDHRECNewsGetter implements NewsGetterInterface {

        private static final String site = "https://articles.edhrec.com/feed/";
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

                        String description = item.getElementsByTag("description").get(0).text().replace("&#8217;", "'");

                        String imageURL = "";//item.html().toString().split(Pattern.quote("<id>tag:www.mtggoldfish.com,2005:Article/"))[1].split(Pattern.quote("</id>"))[0];

                        output.add(new NewsItem(item.getElementsByTag("title").get(0).text().replace("&#8217;", "'"), description,
                                item.getElementsByTag("dc:creator").get(0).text().replace("&#8217;", "'"),
                                item.getElementsByTag("pubDate").get(0).text().replace("T"," at ").replace("&#8217;", "'"),
                                item.getElementsByTag("link").text(), imageURL));
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

            return output;
        }


}
