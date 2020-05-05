package djpiper28.mtgnewsapp.news.tests;

import java.util.List;

import djpiper28.mtgnewsapp.news.testInterface.Test;
import djpiper28.news.DailyMTGNewsGetter;
import djpiper28.news.NewsItem;

public class NewsGetterTest extends Test {

    @Override
    public boolean executeTest() {
        try {
            DailyMTGNewsGetter getter = new DailyMTGNewsGetter();
            List<NewsItem> newsItems = getter.getNews();
            /*
             * for (NewsItem item : newsItems) { System.out.println(item.toString()); }
             */
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getTestName() {
        return "News getter test.";
    }

}
