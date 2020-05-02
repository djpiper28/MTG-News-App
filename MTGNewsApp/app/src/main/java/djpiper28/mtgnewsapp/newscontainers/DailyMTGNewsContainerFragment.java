package djpiper28.mtgnewsapp.newscontainers;

import java.util.List;

import djpiper28.news.NewsItem;

import static djpiper28.mtgnewsapp.LoadingScreen.dailyMTGNews;

public class DailyMTGNewsContainerFragment extends MTGNewsContainerInterface {

    @Override
    List<NewsItem> getNewsFeed() {
        return dailyMTGNews;
    }

}