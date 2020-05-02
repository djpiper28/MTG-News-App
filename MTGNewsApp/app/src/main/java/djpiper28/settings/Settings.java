package djpiper28.settings;

import androidx.core.view.accessibility.AccessibilityViewCommand;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;
import djpiper28.news.NewsItem;
import forohfor.scryfall.api.Set;

public class Settings {

    private static long updateEvery = 6L * 36000L;
    //TODO: add theme change support
    //private Color primaryColour, primaryColourDark, accentColour;
    private boolean useCardsForNews;
    private boolean dailyMTGEnabled;
    private boolean setPreviewsEnabled;
    private boolean EDHRECEnabled;
    private boolean MTGGoldfishEnabled;
    private List<NewsItem> dailyMTGNews, EDHRECNews, MTGGoldfishNews;
    private List<Set> sets;
    private long lastCacheUpdate;

    @Override
    public String toString() {
        return "Settings{" +
                "useCardsForNews=" + useCardsForNews +
                ", dailyMTGEnabled=" + dailyMTGEnabled +
                ", setPreviewsEnabled=" + setPreviewsEnabled +
                ", EDHRECEnabled=" + EDHRECEnabled +
                ", MTGGoldfishEnabled=" + MTGGoldfishEnabled +
                ", dailyMTGNews=" + dailyMTGNews +
                ", EDHRECNews=" + EDHRECNews +
                ", MTGGoldfishNews=" + MTGGoldfishNews +
                ", sets=" + sets +
                ", lastCacheUpdate=" + lastCacheUpdate +
                '}';
    }

    public String toStringNice() {
        return "Settings{" +
                "useCardsForNews=" + useCardsForNews +
                ", dailyMTGEnabled=" + dailyMTGEnabled +
                ", setPreviewsEnabled=" + setPreviewsEnabled +
                ", EDHRECEnabled=" + EDHRECEnabled +
                ", MTGGoldfishEnabled=" + MTGGoldfishEnabled +
                ", dailyMTGNews=" + (dailyMTGNews == null) +
                ", EDHRECNews=" + (EDHRECNews == null) +
                ", MTGGoldfishNews=" + (MTGGoldfishNews == null) +
                ", sets=" + (sets == null) +
                ", lastCacheUpdate=" + lastCacheUpdate +
                '}';
    }

    public long getLastCacheUpdate() {
        return lastCacheUpdate;
    }

    public void setLastCacheUpdate(long lastCacheUpdate) {
        this.lastCacheUpdate = lastCacheUpdate;
    }

    public List<NewsItem> getDailyMTGNews() {
        if(dailyMTGNews == null) {
            return new LinkedList<>();
        }
        return dailyMTGNews;
    }

    public void setDailyMTGNews(List<NewsItem> dailyMTGNews) {
        this.dailyMTGNews = dailyMTGNews;
        setLastCacheUpdate(System.currentTimeMillis());
    }

    public List<NewsItem> getEDHRECNews() {
        if(EDHRECNews == null) {
            return new LinkedList<>();
        }
        return EDHRECNews;
    }

    public void setEDHRECNews(List<NewsItem> EDHRECNews) {
        this.EDHRECNews = EDHRECNews;
        setLastCacheUpdate(System.currentTimeMillis());
    }

    public List<NewsItem> getMTGGoldfishNews() {
        if(MTGGoldfishNews == null) {
            return new LinkedList<>();
        }
        return MTGGoldfishNews;
    }

    public void setMTGGoldfishNews(List<NewsItem> MTGGoldfishNews) {
        this.MTGGoldfishNews = MTGGoldfishNews;
        setLastCacheUpdate(System.currentTimeMillis());
    }

    public List<Set> getSets() {
        if(sets == null) {
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

    /*public Color getPrimaryColour() {
        return primaryColour;
    }

    public void setPrimaryColour(Color primaryColour) {
        this.primaryColour = primaryColour;
    }

    public Color getPrimaryColourDark() {
        return primaryColourDark;
    }

    public void setPrimaryColourDark(Color primaryColourDark) {
        this.primaryColourDark = primaryColourDark;
    }

    public Color getAccentColour() {
        return accentColour;
    }

    public void setAccentColour(Color accentColour) {
        this.accentColour = accentColour;
    }*/

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

    public void applyDefaultSettings() {
        /*primaryColour = R.color.colorPrimary;
        primaryColourDark = R.color.colorPrimaryDark;
        accentColour = R.color.colorAccent;*/
        setUseCardsForNews(false);
        setDailyMTGEnabled(true);
        setEdhrecEnabled(true);
        setMTGGoldfishEnabled(true);
        setSetPreviewsEnabled(true);

        dailyMTGNews = null;
        EDHRECNews = null;
        sets = null;
        MTGGoldfishNews = null;

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

}
