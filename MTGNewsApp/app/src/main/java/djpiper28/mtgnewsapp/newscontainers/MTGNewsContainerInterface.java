package djpiper28.mtgnewsapp.newscontainers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import djpiper28.mtgnewsapp.R;
import djpiper28.news.NewsItem;

import static djpiper28.mtgnewsapp.LoadingScreen.onRefresh;

public abstract class MTGNewsContainerInterface extends Fragment {

    abstract List<NewsItem> getNewsFeed();

    private void loadNews(View view) {
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecycleViewerAdapterNews adapter = new RecycleViewerAdapterNews(getContext(), getNewsFeed());
        recyclerView.setAdapter(adapter);

        // add on refresh
        onRefresh.add(adapter::notifyDataSetChanged);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_container, container, false);

        loadNews(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
