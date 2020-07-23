package djpiper28.mtgnewsapp;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.io.IOException;

import djpiper28.settings.SettingsLoader;

public class NewsUpdateService extends IntentService {

    public NewsUpdateService() {
        super("news updater service");
    }

    public NewsUpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        (new Thread(() -> {
            try {
                SettingsLoader loader = new SettingsLoader(this);
                LoadingScreen.loadNews(null, getBaseContext()); // Seems legit
            } catch (IOException e) {
                e.printStackTrace();
            }
        })).start();
    }

}
