package djpiper28.mtgnewsapp.news.newscontainers;


import java.util.List;

import djpiper28.mtgnewsapp.LoadingScreen;
import djpiper28.mtgnewsapp.news.NewsItem;

public class HipstersNewsContainerFragment extends MTGNewsContainerInterface {

    @Override
    List<NewsItem> getNewsFeed() {
        return LoadingScreen.hipstersNews;
    }

}