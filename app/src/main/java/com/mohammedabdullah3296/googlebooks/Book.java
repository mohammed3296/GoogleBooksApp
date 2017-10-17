package com.mohammedabdullah3296.googlebooks;

/**
 * Created by Mohammed Abdullah on 9/10/2017.
 */

public class Book {
    private String id;
    private String selfLink;
    private String title;
    private String authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private String smallThumbnailImage;
    private String language;
    private String previewLink;
    private String infoLinkGooglePlay;

    public Book(String id,
                String selfLink,
                String title,
                String authors,
                String publisher,
                String publishedDate,
                String description,
                int pageCount,
                String smallThumbnailImage,
                String language,
                String previewLink,
                String infoLinkGooglePlay) {
        this.id = id;
        this.selfLink = selfLink;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.smallThumbnailImage = smallThumbnailImage;
        this.language = language;
        this.previewLink = previewLink;
        this.infoLinkGooglePlay = infoLinkGooglePlay;
    }

    public String getId() {
        return id;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getSmallThumbnailImage() {
        return smallThumbnailImage;
    }

    public String getLanguage() {
        return language;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public String getInfoLinkGooglePlay() {
        return infoLinkGooglePlay;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", selfLink='" + selfLink + '\'' +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", description='" + description + '\'' +
                ", pageCount=" + pageCount +
                ", smallThumbnailImage='" + smallThumbnailImage + '\'' +
                ", language='" + language + '\'' +
                ", previewLink='" + previewLink + '\'' +
                ", infoLinkGooglePlay='" + infoLinkGooglePlay + '\'' +
                '}';
    }
}
