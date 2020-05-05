package djpiper28.settings;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import djpiper28.mtgnewsapp.LoadingScreen;
import djpiper28.mtgnewsapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Settings settings = SettingsLoader.getSettingsLoader().getSettings();

        ((Switch) findViewById(R.id.switchSetPreviews)).setChecked(settings.isSetPreviewsEnabled());
        ((Switch) findViewById(R.id.switchEDHRec)).setChecked(settings.isEdhrecEnabled());
        ((Switch) findViewById(R.id.switchDailyMTG)).setChecked(settings.isDailyMTGEnabled());
        ((Switch) findViewById(R.id.switchMTGGoldfish)).setChecked(settings.isMTGGoldfishEnabled());
        ((RadioButton) findViewById(R.id.radioButtonBigImage)).setChecked(!settings.isUseCardsForNews());
        ((RadioButton) findViewById(R.id.radioButtonSmallImage)).setChecked(settings.isUseCardsForNews());

        // Load settings into activity
        findViewById(R.id.saveSettings).setOnClickListener(event -> {
            // Set settings

            settings.setSetPreviewsEnabled(((Switch) findViewById(R.id.switchSetPreviews)).isChecked());
            settings.setEdhrecEnabled(((Switch)  findViewById(R.id.switchEDHRec)).isChecked());
            settings.setDailyMTGEnabled(((Switch) findViewById(R.id.switchDailyMTG)).isChecked());
            settings.setMTGGoldfishEnabled(((Switch) findViewById(R.id.switchMTGGoldfish)).isChecked());

            settings.setUseCardsForNews( ((RadioButton)findViewById(R.id.radioButtonSmallImage)).isChecked());

            Log.i("info", "onCreate: saving settings");
            try {
                SettingsLoader.getSettingsLoader().saveSettings(event.getContext());
                Toast.makeText(this, "Settings have been saved, restart to see changes", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving settings", Toast.LENGTH_LONG).show();
            }
            Log.i("info", "onCreate: saved settings");
        });

        findViewById(R.id.cacheReset).setOnClickListener(event -> {
            settings.setDailyMTGNews(null);
            settings.setEDHRECNews(null);
            settings.setMTGGoldfishNews(null);
            settings.setSets(null);

            (new Thread(() -> {
                LoadingScreen.reloadRequested = true;
                LoadingScreen.loadNews(null);
                LoadingScreen.reloadRequested = false;
            })).start();

            Toast.makeText(this, "Cache invalidated and deleted", Toast.LENGTH_LONG).show();
        });

        // Reset Button
        findViewById(R.id.restoreDefaults).setOnClickListener(event -> {
            settings.applyDefaultSettings();

            Log.i("info", "onCreate: saving settings");
            try {
                SettingsLoader.getSettingsLoader().saveSettings(event.getContext());
                Toast.makeText(event.getContext(), "Settings have been reset, restart to see changes", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(event.getContext(), "Error resetting settings", Toast.LENGTH_LONG).show();
            }
            Log.i("info", "onCreate: saved settings");
        });
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