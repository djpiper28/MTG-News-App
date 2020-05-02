package djpiper28.mtgnewsapp.newscontainers;

import java.util.List;

import djpiper28.mtgnewsapp.LoadingScreen;
import djpiper28.news.NewsItem;

public class EDHRECNewsContainerFragment extends MTGNewsContainerInterface {

    @Override
    List<NewsItem> getNewsFeed() {
        return LoadingScreen.EDHRECNews;
    }

}
