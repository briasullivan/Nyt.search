package com.codepath.nytsearch.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by briasullivan on 10/18/16.
 */
public class Article {

    private static final int SMALL_IMAGE = 0;
    private static final int LARGE_IMAGE = 1;

    public String getWebUrl() {

        return webUrl;
    }

    public String getHeadline() {

        return headline;
    }

    public String getThumbNail() {

        return thumbNail;
    }

    public String getSnippet() {
        return snippet;
    }
    public String getNewsDesk() {
        return newsDesk;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    String webUrl;
    String headline;
    String thumbNail;
    String snippet;
    String newsDesk;
    int height;
    int width;

    public Article(Doc doc) {
        this.webUrl = doc.getWeb_url();
        this.snippet = doc.getSnippet();
        if (doc.getHeadline() != null) {
            this.headline = doc.getHeadline().getMain();
        }
        if (doc.getMultimedia() != null
                && doc.getMultimedia().size() > 0
                && doc.getMultimedia().get(0).getUrl() != null) {
            int imagePosition;
            if (doc.getMultimedia().size() > 1 && doc.getMultimedia().get(LARGE_IMAGE).getSubtype().equals("xlarge")) {
                imagePosition = LARGE_IMAGE;
            } else {
                imagePosition = SMALL_IMAGE;
            }
            this.thumbNail = "https://www.nytimes.com/" + doc.getMultimedia().get(imagePosition).getUrl();
            this.height = doc.getMultimedia().get(imagePosition).getHeight();
            this.width = doc.getMultimedia().get(imagePosition).getWidth();
            this.newsDesk = doc.getNews_desk();
        }
    }

    public static ArrayList<Article> fromDocList(List<Doc> docs) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; i < docs.size(); i++) {
            results.add(new Article(docs.get(i)));
        }

        return results;
    }
}
