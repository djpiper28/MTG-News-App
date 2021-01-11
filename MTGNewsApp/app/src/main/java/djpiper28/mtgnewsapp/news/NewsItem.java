package djpiper28.mtgnewsapp.news;

import java.io.IOException;

public class NewsItem {
    private String title;
    private String description;
    private String author;
    private String publishedDate;
    private String link;
    private String imageurl;

    public NewsItem(final String title, final String description, final String author,
                    final String publishedDate, final String link, final String image) {
        this.setTitle(title);
        this.setDescription(description);
        this.setAuthor(author);
        this.setPublishedDate(publishedDate);
        this.setLink(link);
        this.setImage(image);
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(final String author) {
        if (author == null) {
            throw new NullPointerException();
        }
        this.author = author;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        if (description == null) {
            throw new NullPointerException();
        }
        this.description = description;
    }

    public String getImageURL() throws IOException {
        return this.imageurl;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getPublishedDate() {
        return this.publishedDate;
    }

    public void setPublishedDate(final String publishedDate) {
        if (publishedDate == null) {
            throw new NullPointerException();
        }
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        if (title == null) {
            throw new NullPointerException();
        }
        this.title = title;
    }

    public void setImage(final String image) {
        this.imageurl = image;
    }

    @Override
    public String toString() {
        return "NewsItem [title=" + this.title + ", description=" + this.description + ", author=" + this.author
                + ", publishedDate=" + this.publishedDate + ", link=" + this.link + "]";
    }
}
