package djpiper28.mtgnewsapp.news;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
//import java.util.regex.Pattern;

public class EDHRECNewsGetter implements NewsGetterInterface {

        private static final String site = "https://articles.edhrec.com/feed/";

        public List<NewsItem> getNews(NewsProgressListener listener) throws IOException {
            List<NewsItem> output = new LinkedList<NewsItem>();

            try {
                Connection conn = Jsoup.connect(site);
                Document doc = conn.get();

                Elements items = doc.getElementsByTag("item");

                Log.i("Info", "Downloaded article info, " + items.size() + " articles to check.\n");

                int i = 0;
                for (Element item : items) {
                    try {
                        Log.i("Info", "Article " + (i + 1) + " out of " + items.size());
                        i++;

                        String description = item.getElementsByTag("description").get(0).text().replace("&#8217;", "'");

                        String imageURL = "";

                        output.add(new NewsItem(item.getElementsByTag("title").get(0).text().replace("&#8217;", "'"), description,
                                item.getElementsByTag("dc:creator").get(0).text().replace("&#8217;", "'"),
                                item.getElementsByTag("pubDate").get(0).text()
                                        .split(Pattern.quote(", "))[1]
                                        .replaceAll(Pattern.quote("+") + "([0123456789])*", ""),
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
