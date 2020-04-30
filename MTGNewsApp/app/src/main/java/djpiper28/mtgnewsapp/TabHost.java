package djpiper28.mtgnewsapp;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class TabHost extends Fragment {

    public TabHost() {

    }

    private void refreshNews() {
        /*Intent intent = new Intent(getContext(), LoadingScreen.class);
        LoadingScreen.reloadRequested = true;
        startActivity(intent);*/

        // Reloads data.
        try {
            LoadingScreen.loadNews(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tab_host, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        TabAdapter adapter = new TabAdapter(getChildFragmentManager());

        Log.i("TabHost", "Loading news viewer");
        adapter.addFragment(new NewsContainerFragment(), "News");
        Log.i("TabHost", "Loading set viewer");
        adapter.addFragment(new SetPreviewContainer(), "Set Previews");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.i("TabHost", "Tabs done");

        FloatingActionButton refresh = view.findViewById(R.id.fab);
        refresh.setOnClickListener(event -> {
            refresh.setOnClickListener(click -> {
            });    //Stops spamming of the FAB.
            refreshNews();
        });

        LoadingScreen.onRefresh.add(() -> {
            refresh.setOnClickListener(event -> {
                refresh.setOnClickListener(click -> {
                });    //Stops spamming of the FAB.
                refreshNews();
            });
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        if (width > height) {
            viewPager.setPadding(120, 0, 120, 0);
        }

        Log.i("Tab Host", "Finished loading");

        return view;
    }

}
