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
//import java.util.regex.Pattern;

public class MTGGoldfishNewsGetter implements NewsGetterInterface {

        private static final String site = "https://www.mtggoldfish.com/feed";

        public List<NewsItem> getNews(NewsProgressListener listener) throws IOException {
            List<NewsItem> output = new LinkedList<NewsItem>();

            try {
                Connection conn = Jsoup.connect(site/*.replace("langcode", Locale.getDefault().getISO3Language().split("-")[0].toLowerCase())*/);
                Document doc = conn/*.userAgent(user)*/.get();

                Elements items = doc.getElementsByTag("entry");

                Log.i("Info", "Downloaded article info, " + items.size() + " articles to check.\n");

                int i = 0;
                for (Element item : items) {
                    try {
                        Log.i("Info", "Article " + (i + 1) + " out of " + items.size());
                        i++;

                        String description = item.getElementsByTag("summary").get(0).text();

                        output.add(new NewsItem(item.getElementsByTag("title").get(0).text(), description,
                                item.getElementsByTag("name").get(0).text(),
                                item.getElementsByTag("published").get(0).text().replace("T"," at ").replace("Z", ""),
                                item.getElementsByTag("url").text(), ""));
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
