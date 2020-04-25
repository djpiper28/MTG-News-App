package djpiper28.mtgnewsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static djpiper28.mtgnewsapp.LoadingScreen.news;
import static djpiper28.mtgnewsapp.LoadingScreen.onRefresh;

public class NewsContainerFragment extends Fragment {

    private void loadNews(View view) {
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecycleViewerAdapterNews adapter = new RecycleViewerAdapterNews(getContext(), news);
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