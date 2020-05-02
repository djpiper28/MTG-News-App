package djpiper28.mtgnewsapp;

import java.util.LinkedList;
import java.util.List;

class Library {

    private String name, url;

    public Library(String name, String url) {
        setName(name);
        setUrl(url);
    }

    public static List<Library> getDefaultList() {
        List<Library> defaults = new LinkedList<>();

        //TODO: check
        defaults.add(new Library("MTG News App (This app)", "https://github.com/djpiper28/MTG-News-App"));
        defaults.add(new Library("Gson", "https://github.com/google/gson"));
        defaults.add(new Library("JSoup", "https://jsoup.org"));
        defaults.add(new Library("Glide", "https://github.com/bumptech/glide"));
        defaults.add(new Library("GlideToVectorYou", "https://github.com/corouteam/GlideToVectorYou"));
        defaults.add(new Library("ScryfallAPIBinding", "https://github.com/ForOhForError/ScryfallAPIBinding"));
        //defaults.add(new Library("Android Material Color Picker Dialog", "https://github.com/Pes8/android-material-color-picker-dialog"));
        // ^^ not impplemented yet

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
