package djpiper28.mtgnewsapp;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ThirdPartyLibraries extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_libraries);

        // ReleasedSets
        RecyclerView viewer = findViewById(R.id.recyclerViewThirdPartyLibraries);
        viewer.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewAdapterLibraries adapter = new RecycleViewAdapterLibraries(this, Library.getDefaultList());
        viewer.setAdapter(adapter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
