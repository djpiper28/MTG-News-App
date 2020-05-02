package djpiper28.mtgnewsapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Stack;

import static djpiper28.mtgnewsapp.LoadingScreen.Downloadable;
import static djpiper28.mtgnewsapp.LoadingScreen.SocialShares;

public class CardPreviewActivity extends AppCompatActivity {

    public static String url;
    private WebView webView;
    private Stack<String> urlStack;

    private void openLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_preview);

        urlStack = new Stack<>();

        webView = findViewById(R.id.webView);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.getSettings().setOffscreenPreRaster(true);
        }
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                boolean openedExternally = false;

                for (String socialShare : SocialShares) {
                    if (request.getUrl().toString().contains(socialShare)) {
                        openLink(request.getUrl().toString());
                        openedExternally = true;
                    }
                }

                for (String downloadable : Downloadable) {
                    if (request.getUrl().toString().contains(downloadable)) {
                        openLink(request.getUrl().toString());
                        openedExternally = true;
                    }
                }

                if(!openedExternally) {
                    urlStack.push(request.getUrl().toString());
                }

                view.loadUrl(request.getUrl().toString());
                view.setScrollX(0);
                view.setScrollY(0);
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.articleFab);
        fab.setOnClickListener(event -> {
            openLink(webView.getUrl());
        });

        webView.loadUrl(url);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if(urlStack.isEmpty()) {
            super.onBackPressed();
        } else{
            webView.loadUrl(urlStack.pop());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_browser_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                this.webView.loadUrl(webView.getUrl());
                return true;

            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out these new set previews!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check out these new set previews!" + webView.getUrl());
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                return true;

            case R.id.action_open:
                openLink(webView.getUrl());
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation != getResources().getConfiguration().orientation) {
            LoadingScreen.reloadRequested = false;
        }
    }

}
