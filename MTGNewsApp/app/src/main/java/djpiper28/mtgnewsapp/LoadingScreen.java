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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.List;

import djpiper28.news.NewsGetter;
import djpiper28.news.NewsItem;

public class LoadingScreen extends AppCompatActivity {

    public static final String[] SocialShares = {"twitter.com", "facebook.com", "instagram.com", "youtube.com", "twitch.tv"};
    public static final String[] Downloadable = {".docx", ".pdf", ".txt"};
    public static List<Runnable> onRefresh = new LinkedList<>();
    public static List<NewsItem> news;
    public static List<forohfor.scryfall.api.Set> sets;
    public static boolean reloadRequested = false;  // To try to fetch data less

    private void error(String errorMessage) {
        Log.e("LoadingScreen", "error: " + errorMessage);

        ErrorFragment fragmentActivity = ErrorFragment.newInstance(errorMessage);
        addFragment(fragmentActivity);
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.errors, fragment).commit();
    }

    public static void loadNews(ProgressBar bar) {
        if (reloadRequested || news == null || sets == null) {
            reloadRequested = false;
            news = new LinkedList<>();

            NewsGetter newsGetter = null;
            boolean gotNews = false;
            int a = 0;

            while (!gotNews && a < 5) {
                try {
                    newsGetter = new NewsGetter();
                    if(bar != null) {
                        news = newsGetter.getNews((int articles, int max) -> {
                            bar.setProgress(100 * (articles / max));
                        });
                    }
                    gotNews = true;
                } catch (final Exception e) {
                    e.printStackTrace();

                    // Wait and hope the internet starts to work after the wait
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                a++;
            }

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
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        Log.i("info", "loadNews: failed to load sets");
                        ex.printStackTrace();
                    }
                }
                a++;
            }
        }

        for(Runnable runnable: onRefresh) {
            runnable.run();
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

        ProgressBar bar = findViewById(R.id.progressBar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("Error", "No permission to access internet");
            error("Error - no internet permission");
        } else {
            (new Thread(() -> {
                Log.i("Loading Screen", "Started loading");
                loadNews(bar);
                Log.i("Loading Screen", "Finished loading");

                if (news != null && sets != null) {
                    TabHost tabHost = new TabHost();
                    getSupportFragmentManager().beginTransaction().replace(R.id.tabHostContainer, tabHost).commit();

                    Log.i("Loading Screen", "Started tabhost");
                } else {
                    Log.i("Loading Screen", "Failed loading");
                    bar.setProgress(0);
                    StringBuilder sb = new StringBuilder();

                    if (news == null) {
                        sb.append("news=null ");
                    }
                    if (sets == null) {
                        sb.append("sets=null ");
                    }

                    error("Unable to load details: " + sb.toString());
                }
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

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}