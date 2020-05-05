package djpiper28.mtgnewsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import djpiper28.news.DailyMTGNewsGetter;
import djpiper28.news.EDHRECNewsGetter;
import djpiper28.news.MTGGoldfishNewsGetter;
import djpiper28.news.NewsItem;
import djpiper28.settings.Settings;
import djpiper28.settings.SettingsActivity;
import djpiper28.settings.SettingsLoader;

public class LoadingScreen extends AppCompatActivity {

    public static final String[] SocialShares = {"twitter.com", "facebook.com", "instagram.com", "youtube.com", "twitch.tv"};
    public static final String[] Downloadable = {".docx", ".pdf", ".txt"};
    public static List<Runnable> onRefresh;
    public static List<NewsItem> dailyMTGNews, EDHRECNews, MTGGoldfishNews;
    public static List<forohfor.scryfall.api.Set> sets;
    public static boolean reloadRequested = false;  // To try to fetch data less

    public static void loadNews(ProgressBar bar) {
        Settings settings = SettingsLoader.getSettingsLoader().getSettings();

        if (reloadRequested || settings.cacheUpdateRequired()) {
            reloadRequested = false;

            int a = 0;

            if (settings.isDailyMTGEnabled()) {
                dailyMTGNews = new LinkedList<>();

                DailyMTGNewsGetter newsGetter = null;
                boolean gotNews = false;

                while (!gotNews && a < 5) {
                    try {
                        newsGetter = new DailyMTGNewsGetter();
                        if (bar != null) {
                            dailyMTGNews = newsGetter.getNews((int articles, int max) -> {
                                bar.setProgress(100 * (articles / max));
                            });
                        }
                        gotNews = true;
                    } catch (final Exception e) {
                        e.printStackTrace();

                        // Wait and hope the internet starts to work after the wait
                        try {
                            Thread.sleep(120);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    a++;
                }

                if(dailyMTGNews == null || !gotNews) {
                    dailyMTGNews = settings.getDailyMTGNews();
                } else{
                    settings.setDailyMTGNews(dailyMTGNews);
                }
            }

            if (settings.isMTGGoldfishEnabled()) {
                MTGGoldfishNews = new LinkedList<>();

                MTGGoldfishNewsGetter newsGetter = null;
                boolean gotNews = false;

                while (!gotNews && a < 5) {
                    try {
                        newsGetter = new MTGGoldfishNewsGetter();
                        if (bar != null) {
                            MTGGoldfishNews = newsGetter.getNews((int articles, int max) -> {
                                bar.setProgress(100 * (articles / max));
                            });
                        }
                        gotNews = true;
                    } catch (final Exception e) {
                        e.printStackTrace();

                        // Wait and hope the internet starts to work after the wait
                        try {
                            Thread.sleep(120);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    a++;
                }

                if(MTGGoldfishNews == null || !gotNews) {
                    MTGGoldfishNews = settings.getMTGGoldfishNews();
                } else{
                    settings.setMTGGoldfishNews(MTGGoldfishNews);
                }
            }

            if (settings.isEdhrecEnabled()) {
                EDHRECNews = new LinkedList<>();

                EDHRECNewsGetter newsGetter = null;
                boolean gotNews = false;

                while (!gotNews && a < 5) {
                    try {
                        newsGetter = new EDHRECNewsGetter();
                        if (bar != null) {
                            EDHRECNews = newsGetter.getNews((int articles, int max) -> {
                                bar.setProgress(100 * (articles / max));
                            });
                        }
                        gotNews = true;
                    } catch (final Exception e) {
                        e.printStackTrace();

                        // Wait and hope the internet starts to work after the wait
                        try {
                            Thread.sleep(120);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    a++;
                }

                if(EDHRECNews == null || !gotNews) {
                    EDHRECNews = settings.getEDHRECNews();
                } else{
                    settings.setEDHRECNews(EDHRECNews);
                }
            }

            if (settings.isSetPreviewsEnabled()) {
                a = 0;
                boolean gotSets = false;
                while (!gotSets && a < 5) {
                    try {
                        Log.i("info", "loadNews: trying to load sets");
                        sets = forohfor.scryfall.api.MTGCardQuery.getSets();
                        gotSets = true;
                    } catch (final Exception e) {
                        e.printStackTrace();

                        // Wait and hope the internet starts to work after the wait
                        try {
                            Thread.sleep(120);
                        } catch (InterruptedException ex) {
                            Log.i("info", "loadNews: failed to load sets");
                            ex.printStackTrace();
                        }
                    }
                    a++;
                }

                if (sets == null || !gotSets) {
                    sets = settings.getSets();
                } else{
                    settings.setSets(sets);
                }
            }
        } else {
            sets = settings.getSets();
            dailyMTGNews = settings.getDailyMTGNews();
            EDHRECNews = settings.getEDHRECNews();
            MTGGoldfishNews = settings.getMTGGoldfishNews();
        }

        try {
            SettingsLoader.getSettingsLoader().saveSettings(bar.getContext());
            Log.i("info", "loadNews: "+settings.toStringNice());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Runnable runnable : onRefresh) {
            try {
                runnable.run();
            } catch (Exception e){
                e.printStackTrace();
            }
            //(new Thread(runnable)).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.generic_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        reloadRequested = false;
        onRefresh = new LinkedList<>();

        try {
            SettingsLoader settingsLoader = new SettingsLoader(this); // Loads settings

            Log.i("info", "onCreate: "+settingsLoader.getSettings().toStringNice());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProgressBar bar = findViewById(R.id.progressBar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("Error", "No permission to access internet");
            Toast.makeText(this, "Error - no internet permission", Toast.LENGTH_LONG);
        } else {
            (new Thread(() -> {
                Log.i("Loading Screen", "Started loading");
                loadNews(bar);
                Log.i("Loading Screen", "Finished loading");

                TabHost tabHost = new TabHost();
                getSupportFragmentManager().beginTransaction().replace(R.id.tabHostContainer, tabHost).commit();

                Log.i("Loading Screen", "Started tabhost");
            })).start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_feedback:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=djpiper28.mtgnewsapp"));
                startActivity(browserIntent);
                return true;

            case R.id.action_settings:
                Intent openSettings = new Intent(this, SettingsActivity.class);
                startActivity(openSettings);
                return true;

            case R.id.action_view_libs:
                Intent viewLibs = new Intent(this, ThirdPartyLibraries.class);
                startActivity(viewLibs);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}