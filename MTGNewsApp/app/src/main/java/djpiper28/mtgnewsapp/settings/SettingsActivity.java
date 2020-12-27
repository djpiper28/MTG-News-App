package djpiper28.mtgnewsapp.settings;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.IOException;

import djpiper28.mtgnewsapp.LoadingScreen;
import djpiper28.mtgnewsapp.R;

public class SettingsActivity extends AppCompatActivity {

    private void applyDefaults() {
        Settings settings = SettingsLoader.getSettingsLoader().getSettings();

        ((Switch) findViewById(R.id.switchSetPreviews)).setChecked(settings.isSetPreviewsEnabled());
        ((Switch) findViewById(R.id.switchEDHRec)).setChecked(settings.isEdhrecEnabled());
        ((Switch) findViewById(R.id.switchDailyMTG)).setChecked(settings.isDailyMTGEnabled());
        ((Switch) findViewById(R.id.switchMTGGoldfish)).setChecked(settings.isMTGGoldfishEnabled());

        ((Switch) findViewById(R.id.EnableNewsRefresh)).setChecked(settings.isBackgroundRefreshEnabled());

        ((Switch) findViewById(R.id.switchForceNightMode)).setChecked(settings.isDarkModeEnabled());

        ((EditText) findViewById(R.id.cacheUpdateFrequency)).setText(String.valueOf(settings.getUpdateEvery()));

        ((RadioButton) findViewById(R.id.radioButtonBigImage)).setChecked(!settings.isUseCardsForNews());
        ((RadioButton) findViewById(R.id.radioButtonSmallImage)).setChecked(settings.isUseCardsForNews());

        Button primaryColourButton = findViewById(R.id.primaryColourButton);
        primaryColourButton.setOnClickListener(event -> {
            final ColorPicker cp = new ColorPicker(this, Color.red(settings.getPrimaryColour())
                    , Color.green(settings.getPrimaryColour())
                    , Color.blue(settings.getPrimaryColour()));
            cp.show();
            cp.enableAutoClose();
            cp.setCallback(colour -> {
                settings.setPrimaryColour(colour);
                applyColours();
            });
        });

        Button accentColourButton = findViewById(R.id.accentColourButton);
        accentColourButton.setOnClickListener(event -> {
            final ColorPicker cp = new ColorPicker(this, Color.red(settings.getAccentColour())
                    , Color.green(settings.getAccentColour())
                    , Color.blue(settings.getAccentColour()));
            cp.show();
            cp.enableAutoClose();
            cp.setCallback(colour -> {
                settings.setAccentColour(colour);
                applyColours();
            });
        });

        // Save settings button
        Button saveSettingsButton = findViewById(R.id.saveSettings);
        saveSettingsButton.setOnClickListener(event -> {
            // Set settings
            settings.setSetPreviewsEnabled(((Switch) findViewById(R.id.switchSetPreviews)).isChecked());
            settings.setEdhrecEnabled(((Switch) findViewById(R.id.switchEDHRec)).isChecked());
            settings.setDailyMTGEnabled(((Switch) findViewById(R.id.switchDailyMTG)).isChecked());
            settings.setMTGGoldfishEnabled(((Switch) findViewById(R.id.switchMTGGoldfish)).isChecked());
            settings.setDarkModeEnabled(((Switch) findViewById(R.id.switchForceNightMode)).isChecked());
            settings.setUseCardsForNews(((RadioButton) findViewById(R.id.radioButtonSmallImage)).isChecked());
            settings.setBackgroundRefreshEnabled(((Switch) findViewById(R.id.EnableNewsRefresh)).isChecked());

            try {
                settings.setUpdateEvery(Long.parseLong(String.valueOf(((EditText) findViewById(R.id.cacheUpdateFrequency)).getText())));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Please set cache update period to a number", Toast.LENGTH_LONG).show();
            }

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

        //Reset cache Button
        Button resetCacheButton = findViewById(R.id.cacheReset);
        resetCacheButton.setOnClickListener(event -> {
            (new Thread(() -> {
                LoadingScreen.reloadRequested = true;
                LoadingScreen.loadNews(null, this);
                LoadingScreen.reloadRequested = false;
            })).start();

            Log.i("info", "onCreate: saving settings");
            try {
                SettingsLoader.getSettingsLoader().saveSettings(event.getContext());
                Toast.makeText(event.getContext(), "Settings have been reset, restart to see changes", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(event.getContext(), "Error resetting settings", Toast.LENGTH_LONG).show();
            }
            Log.i("info", "onCreate: saved settings");

            Toast.makeText(this, "Cache invalidated and deleted", Toast.LENGTH_LONG).show();
        });

        // Reset Button
        Button resetButton = findViewById(R.id.restoreDefaults);
        resetButton.setOnClickListener(event -> {
            settings.applyDefaultSettings(this);
            applyColours();

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

        applyColours();
    }

    private void applyColours() {
        Settings settings = SettingsLoader.getSettingsLoader().getSettings();

        Button primaryColourButton = findViewById(R.id.primaryColourButton);
        primaryColourButton.setBackgroundColor(settings.getPrimaryColour());

        Button resetButton = findViewById(R.id.restoreDefaults);
        resetButton.setBackgroundColor(settings.getAccentColour());

        Button saveSettingsButton = findViewById(R.id.saveSettings);
        saveSettingsButton.setBackgroundColor(settings.getAccentColour());

        Button resetCacheButton = findViewById(R.id.cacheReset);
        resetCacheButton.setBackgroundColor(settings.getAccentColour());

        Button accentColourButton = findViewById(R.id.accentColourButton);
        accentColourButton.setBackgroundColor(settings.getAccentColour());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(settings.getPrimaryColour());

        getWindow().setStatusBarColor(Color.rgb((int) (Color.red(settings.getPrimaryColour()) * 0.8),
                (int) (Color.green(settings.getPrimaryColour()) * 0.8),
                (int) (Color.blue(settings.getPrimaryColour()) * 0.8)));
    }

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

        applyDefaults();
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