package djpiper28.news;

import java.io.IOException;
import java.util.List;

public interface NewsGetterInterface {

    public abstract List<NewsItem> getNews(NewsProgressListener listener) throws IOException;

}
