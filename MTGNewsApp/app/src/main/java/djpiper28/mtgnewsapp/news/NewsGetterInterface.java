package djpiper28.mtgnewsapp.news;

import java.io.IOException;
import java.util.List;

public interface NewsGetterInterface {

    static final String user = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:73.0) Gecko/20100101 Firefox/73.0";

    public abstract List<NewsItem> getNews(NewsProgressListener listener) throws IOException;

}
