package djpiper28.settings;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SettingsLoader {

    private final static String filename = "settings-mtg-news-app.json";
    private static SettingsLoader settingsLoader;
    private Settings settings;

    public SettingsLoader(Context context) throws IOException {
        settingsLoader = this;
        boolean readSettings = settingsFileExists(context);
        if (readSettings) {
            try {
                settings = readSettingsFile(context);
            } catch (Exception e) {
                e.printStackTrace();
                readSettings = false;
            }
        }

        if (!readSettings) {
            Settings temp = new Settings();
            temp.applyDefaultSettings();
            saveSettings(context);

            settings = temp;
        }
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
