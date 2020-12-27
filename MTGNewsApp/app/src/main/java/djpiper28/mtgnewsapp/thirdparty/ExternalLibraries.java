package djpiper28.mtgnewsapp.thirdparty;

import java.util.LinkedList;
import java.util.List;

public class ExternalLibraries {

    private String name, url;

    public ExternalLibraries(String name, String url) {
        setName(name);
        setUrl(url);
    }

    public static List<ExternalLibraries> getDefaultList() {
        List<ExternalLibraries> defaults = new LinkedList<>();

        //TODO: check
        defaults.add(new ExternalLibraries("MTG News App (This app)", "https://github.com/djpiper28/MTG-News-App"));
        defaults.add(new ExternalLibraries("Gson", "https://github.com/google/gson"));
        defaults.add(new ExternalLibraries("Glide", "https://github.com/bumptech/glide"));
        defaults.add(new ExternalLibraries("GlideToVectorYou", "https://github.com/corouteam/GlideToVectorYou"));
        defaults.add(new ExternalLibraries("Scryfall API Binding", "https://github.com/ForOhForError/ScryfallAPIBinding"));
        defaults.add(new ExternalLibraries("Android Material Color Picker Dialog", "https://github.com/Pes8/android-material-color-picker-dialog"));
        defaults.add(new ExternalLibraries("Manamoji", "https://github.com/scryfall/manamoji-slack"));
        defaults.add(new ExternalLibraries("JSoup", "https://jsoup.org"));

        return defaults;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
