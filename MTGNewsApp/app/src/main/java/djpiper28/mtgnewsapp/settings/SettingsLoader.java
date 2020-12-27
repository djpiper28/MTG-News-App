package djpiper28.mtgnewsapp.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import djpiper28.mtgnewsapp.R;

public class SettingsLoader {

    private final static String filename = "settings-mtg-news-app.json";
    private static SettingsLoader settingsLoader;
    private Settings settings;
    private boolean darkModeApplied;

    @SuppressLint("Assert")
    public SettingsLoader(Context context) throws IOException {
        darkModeApplied = false;
        settingsLoader = this;
        boolean readSettings = settingsFileExists(context);

        if (readSettings) {
            try {
                settings = readSettingsFile(context);
                Log.i("info", "SettingsLoader: read settings from file");
                if (settings == null || settings.outdatedSettings()) throw new NullPointerException();
            } catch (Exception e) {
                Log.i("info", "SettingsLoader: error reading settings from file");
                e.printStackTrace();
                readSettings = false;
            }
        }

        if (!readSettings) {
            Log.i("info", "SettingsLoader: making new settings object");
            settings = new Settings();
            settings.applyDefaultSettings(context);
            saveSettings(context);
        }

        if(!darkModeApplied) {
            settings.applyDarkMode();
            darkModeApplied = true;
        }

        if(settings.getPrimaryColour() == 0) {
            settings.setPrimaryColour(R.color.colorPrimary);
        }

        if(settings.getAccentColour() == 0) {
            settings.setAccentColour(R.color.colorAccent);
        }

        if (settings==null || settingsLoader==null) throw new NullPointerException();
    }

    public static SettingsLoader getSettingsLoader() {
        return settingsLoader;
    }

    public static void setSettingsLoader(SettingsLoader settingsLoader) {
        SettingsLoader.settingsLoader = settingsLoader;
    }

    public Settings getSettings() {
        return settings;
    }

    public void saveSettings(Context context) throws IOException {
        Log.i("info", "SettingsLoader:saving settings");

        // Update version code
        if(settings.isValid()) {
            settings.setVersionCode();
        } else {
            Log.i("warning", "saveSettings: invalid settings");
        }

        // Make new file
        File file = new File(context.getFilesDir(), filename);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }

        // Get data
        String dataToWrite = settings.getSettingsFileDataToWrite();

        // Write data
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        fileOutputStream.write(dataToWrite.getBytes());
        fileOutputStream.close();

        Log.i("info", "SettingsLoader:saved settings");
    }

    private boolean settingsFileExists(Context context) throws IOException {
        File file = new File(context.getFilesDir(), filename);
        return file.exists();
    }

    private Settings readSettingsFile(Context context) throws IOException {
        // Open file reader
        File file = new File(context.getFilesDir(), filename);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);

        // Read data
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());
        }

        // Close readers
        bufferedReader.close();
        reader.close();
        fileInputStream.close();

        // Parse data
        String jsonSettings = stringBuilder.toString();
        Gson gson = new Gson();

        return gson.fromJson(jsonSettings, Settings.class);
    }

}
