package com.codepath.nytsearch.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by briasullivan on 10/18/16.
 */
public class Article {

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    String webUrl;
    String headline;
    String thumbNail;

    public Article(Doc doc) {
        this.webUrl = doc.getWeb_url();
        if (doc.getHeadline() != null) {
            this.headline = doc.getHeadline().getMain();
        }
        if (doc.getMultimedia() != null && doc.getMultimedia().size() > 0) {
            this.thumbNail = doc.getMultimedia().get(0).getUrl();
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
