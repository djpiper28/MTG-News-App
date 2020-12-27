package djpiper28.mtgnewsapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import djpiper28.mtgnewsapp.news.newscontainers.DailyMTGNewsContainerFragment;
import djpiper28.mtgnewsapp.news.newscontainers.EDHRECNewsContainerFragment;
import djpiper28.mtgnewsapp.news.newscontainers.MTGGoldfishNewsContainerFragment;
import djpiper28.mtgnewsapp.setpreview.SetPreviewContainer;
import djpiper28.mtgnewsapp.settings.Settings;
import djpiper28.mtgnewsapp.settings.SettingsLoader;

public class TabHost extends Fragment {

    public TabHost() {

    }

    private void refreshNews(FloatingActionButton fab) {
        /*Intent intent = new Intent(getContext(), LoadingScreen.class);
        LoadingScreen.reloadRequested = true;
        startActivity(intent);*/

        // Reloads data.
        try {
            LoadingScreen.loadNews(null, getContext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fab.setEnabled(true);
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

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setBackgroundColor(SettingsLoader.getSettingsLoader().getSettings().getPrimaryColour());

        TabAdapter adapter = new TabAdapter(getChildFragmentManager());

        Settings settings = SettingsLoader.getSettingsLoader().getSettings();

        if (settings.isDailyMTGEnabled()) {
            Log.i("TabHost", "Loading dailymtg news viewer");
            adapter.addFragment(new DailyMTGNewsContainerFragment(), "Daily MTG");

        }

        if (settings.isEdhrecEnabled()) {
            Log.i("TabHost", "Loading edhrec news viewer");
            adapter.addFragment(new EDHRECNewsContainerFragment(), "EDHREC");
        }

        if (settings.isMTGGoldfishEnabled()) {
            Log.i("TabHost", "Loading mtg goldfish news viewer");
            adapter.addFragment(new MTGGoldfishNewsContainerFragment(), "MTGGOLDFISH");
        }

        if (settings.isSetPreviewsEnabled()) {
            Log.i("TabHost", "Loading set viewer");
            adapter.addFragment(new SetPreviewContainer(), "Set Previews");
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.i("TabHost", "Tabs done");

        FloatingActionButton refresh = view.findViewById(R.id.fab);
        refresh.setOnClickListener(event -> {
            refresh.setEnabled(false);
            refreshNews(refresh);
        });
        refresh.setBackgroundTintList(ColorStateList.valueOf(SettingsLoader.getSettingsLoader().getSettings().getAccentColour()));

        LoadingScreen.onRefresh.add(() -> {
            refresh.setOnClickListener(event -> {
                refresh.setEnabled(false);
                refreshNews(refresh);
            });
        });

        Log.i("Tab Host", "Finished loading");

        return view;
    }

}
