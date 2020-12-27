package djpiper28.mtgnewsapp.settings;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import djpiper28.mtgnewsapp.BuildConfig;
import djpiper28.mtgnewsapp.R;
import djpiper28.mtgnewsapp.news.NewsItem;
import forohfor.scryfall.api.Set;

public class Settings {

    private transient static final long hour = 60 * 60 * 1000;
    private int versionCode;
    private long updateEvery = 6L * 60L * 60L * 1000L;
    private int primaryColour;
    private int accentColour;
    private boolean useCardsForNews;
    private boolean dailyMTGEnabled;
    private boolean setPreviewsEnabled;
    private boolean EDHRECEnabled;
    private boolean MTGGoldfishEnabled;
    private boolean isDarkModeEnabled;
    private boolean isBackgroundRefreshEnabled;
    private List<NewsItem> dailyMTGNews, EDHRECNews, MTGGoldfishNews;
    private List<Set> sets;
    private long lastCacheUpdate;

    public int getPrimaryColour() {
        return primaryColour;
    }

    public void setPrimaryColour(int primaryColour) {
        this.primaryColour = primaryColour;
    }

    public int getAccentColour() {
        return accentColour;
    }

    public void setAccentColour(int accentColour) {
        this.accentColour = accentColour;
    }

    public void setVersionCode() {
        versionCode = BuildConfig.VERSION_CODE;
    }

    public int getVersion() {
        return versionCode;
    }

    public boolean outdatedSettings() {
        return BuildConfig.VERSION_CODE > versionCode;
    }

    public long getUpdateEvery() {
        return updateEvery / hour;
    }

    public void setUpdateEvery(long updateEvery) {
        this.updateEvery = updateEvery * hour;
    }

    public boolean isEDHRECEnabled() {
        return EDHRECEnabled;
    }

    public void setEDHRECEnabled(boolean EDHRECEnabled) {
        this.EDHRECEnabled = EDHRECEnabled;
    }

    public boolean isDarkModeEnabled() {
        return isDarkModeEnabled;
    }

    public void setDarkModeEnabled(boolean darkModeEnabled) {
        isDarkModeEnabled = darkModeEnabled;
    }

    public long getLastCacheUpdate() {
        return lastCacheUpdate;
    }

    public void setLastCacheUpdate(long lastCacheUpdate) {
        this.lastCacheUpdate = lastCacheUpdate;
    }

    public List<NewsItem> getDailyMTGNews() {
        if (dailyMTGNews == null) {
            return new LinkedList<>();
        }
        return dailyMTGNews;
    }

    public void setDailyMTGNews(List<NewsItem> dailyMTGNews) {
        this.dailyMTGNews = dailyMTGNews;
        setLastCacheUpdate(System.currentTimeMillis());
    }

    public List<NewsItem> getEDHRECNews() {
        if (EDHRECNews == null) {
            return new LinkedList<>();
        }
        return EDHRECNews;
    }

    public void setEDHRECNews(List<NewsItem> EDHRECNews) {
        this.EDHRECNews = EDHRECNews;
        setLastCacheUpdate(System.currentTimeMillis());
    }

    public List<NewsItem> getMTGGoldfishNews() {
        if (MTGGoldfishNews == null) {
            return new LinkedList<>();
        }
        return MTGGoldfishNews;
    }

    public void setMTGGoldfishNews(List<NewsItem> MTGGoldfishNews) {
        this.MTGGoldfishNews = MTGGoldfishNews;
        setLastCacheUpdate(System.currentTimeMillis());
    }

    public List<Set> getSets() {
        if (sets == null) {
            return new LinkedList<>();
        }
        return sets;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
        setLastCacheUpdate(System.currentTimeMillis());
    }

    public boolean isEdhrecEnabled() {
        return EDHRECEnabled;
    }

    public void setEdhrecEnabled(boolean edhrecEnabled) {
        this.EDHRECEnabled = edhrecEnabled;
    }

    public boolean isMTGGoldfishEnabled() {
        return MTGGoldfishEnabled;
    }

    public void setMTGGoldfishEnabled(boolean MTGGoldfishEnabled) {
        this.MTGGoldfishEnabled = MTGGoldfishEnabled;
    }

    public boolean isUseCardsForNews() {
        return useCardsForNews;
    }

    public void setUseCardsForNews(boolean useCardsForNews) {
        this.useCardsForNews = useCardsForNews;
    }

    public boolean isDailyMTGEnabled() {
        return dailyMTGEnabled;
    }

    public void setDailyMTGEnabled(boolean dailyMTGEnabled) {
        this.dailyMTGEnabled = dailyMTGEnabled;
    }

    public boolean isSetPreviewsEnabled() {
        return setPreviewsEnabled;
    }

    public void setSetPreviewsEnabled(boolean setPreviewsEnabled) {
        this.setPreviewsEnabled = setPreviewsEnabled;
    }

    public void applyDefaultSettings(Context context) {
        setPrimaryColour(ContextCompat.getColor(context, R.color.colorPrimary));
        setAccentColour(ContextCompat.getColor(context, R.color.colorAccent));
        setUpdateEvery(6);
        setUseCardsForNews(false);
        setDailyMTGEnabled(true);
        setEdhrecEnabled(true);
        setMTGGoldfishEnabled(true);
        setSetPreviewsEnabled(true);
        setVersionCode();
        setDailyMTGNews(null);
        setEDHRECNews(null);
        setSets(null);
        setMTGGoldfishNews(null);
        setBackgroundRefreshEnabled(true);
        setLastCacheUpdate(System.currentTimeMillis());
    }

    public String getSettingsFileDataToWrite() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public boolean cacheUpdateRequired() {
        if (System.currentTimeMillis() - getLastCacheUpdate() >= updateEvery) {
            return true;
        } else {
            return (isDailyMTGEnabled() && dailyMTGNews == null) ||
                    (isEdhrecEnabled() && EDHRECNews == null) ||
                    (isMTGGoldfishEnabled() && MTGGoldfishNews == null) ||
                    (isSetPreviewsEnabled() && sets == null);
        }
    }

    public boolean isValid() {
        return versionCode > 0 && updateEvery > 0 && dailyMTGNews != null && EDHRECNews != null && MTGGoldfishNews != null && sets != null;
    }

    public void applyDarkMode() {
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    public boolean isBackgroundRefreshEnabled() {
        return isBackgroundRefreshEnabled;
    }

    public void setBackgroundRefreshEnabled(boolean backgroundRefreshEnabled) {
        isBackgroundRefreshEnabled = backgroundRefreshEnabled;
    }
}
