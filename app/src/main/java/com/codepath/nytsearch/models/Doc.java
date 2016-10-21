package com.codepath.nytsearch.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Doc {

    private String web_url;
    private String snippet;
    private String lead_paragraph;
    private Object _abstract;
    private Object print_page;
    private List<Object> blog = new ArrayList<Object>();
    private String source;
    private List<Multimedium> multimedia = new ArrayList<Multimedium>();
    private Headline headline;
    private List<Keyword> keywords = new ArrayList<Keyword>();
    private String pub_date;
    private String document_type;
    private String news_desk;
    private String section_name;
    private Object subsection_name;
    private Object byline = new Object();
    private String type_of_material;
    private String _id;
    private String word_count;
    private String slideshow_credits;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The web_url
     */
    public String getWeb_url() {
        return web_url;
    }

    /**
     *
     * @param web_url
     * The web_url
     */
    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    /**
     *
     * @return
     * The snippet
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     *
     * @param snippet
     * The snippet
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    /**
     *
     * @return
     * The lead_paragraph
     */
    public String getLead_paragraph() {
        return lead_paragraph;
    }

    /**
     *
     * @param lead_paragraph
     * The lead_paragraph
     */
    public void setLead_paragraph(String lead_paragraph) {
        this.lead_paragraph = lead_paragraph;
    }

    /**
     *
     * @return
     * The _abstract
     */
    public Object getAbstract() {
        return _abstract;
    }

    /**
     *
     * @param _abstract
     * The abstract
     */
    public void setAbstract(Object _abstract) {
        this._abstract = _abstract;
    }

    /**
     *
     * @return
     * The print_page
     */
    public Object getPrint_page() {
        return print_page;
    }

    /**
     *
     * @param print_page
     * The print_page
     */
    public void setPrint_page(Object print_page) {
        this.print_page = print_page;
    }

    /**
     *
     * @return
     * The blog
     */
    public List<Object> getBlog() {
        return blog;
    }

    /**
     *
     * @param blog
     * The blog
     */
    public void setBlog(List<Object> blog) {
        this.blog = blog;
    }

    /**
     *
     * @return
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     * The multimedia
     */
    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    /**
     *
     * @param multimedia
     * The multimedia
     */
    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }

    /**
     *
     * @return
     * The headline
     */
    public Headline getHeadline() {
        return headline;
    }

    /**
     *
     * @param headline
     * The headline
     */
    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    /**
     *
     * @return
     * The keywords
     */
    public List<Keyword> getKeywords() {
        return keywords;
    }

    /**
     *
     * @param keywords
     * The keywords
     */
    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    /**
     *
     * @return
     * The pub_date
     */
    public String getPub_date() {
        return pub_date;
    }

    /**
     *
     * @param pub_date
     * The pub_date
     */
    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    /**
     *
     * @return
     * The document_type
     */
    public String getDocument_type() {
        return document_type;
    }

    /**
     *
     * @param document_type
     * The document_type
     */
    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    /**
     *
     * @return
     * The news_desk
     */
    public String getNews_desk() {
        return news_desk;
    }

    /**
     *
     * @param news_desk
     * The news_desk
     */
    public void setNews_desk(String news_desk) {
        this.news_desk = news_desk;
    }

    /**
     *
     * @return
     * The section_name
     */
    public String getSection_name() {
        return section_name;
    }

    /**
     *
     * @param section_name
     * The section_name
     */
    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    /**
     *
     * @return
     * The subsection_name
     */
    public Object getSubsection_name() {
        return subsection_name;
    }

    /**
     *
     * @param subsection_name
     * The subsection_name
     */
    public void setSubsection_name(Object subsection_name) {
        this.subsection_name = subsection_name;
    }

    /**
     *
     * @return
     * The byline
     */
    public Object getByline() {
        return byline;
    }

    /**
     *
     * @param byline
     * The byline
     */
    public void setByline(List<Object> byline) {
        this.byline = byline;
    }

    /**
     *
     * @return
     * The type_of_material
     */
    public String getType_of_material() {
        return type_of_material;
    }

    /**
     *
     * @param type_of_material
     * The type_of_material
     */
    public void setType_of_material(String type_of_material) {
        this.type_of_material = type_of_material;
    }

    /**
     *
     * @return
     * The _id
     */
    public String get_id() {
        return _id;
    }

    /**
     *
     * @param _id
     * The _id
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     *
     * @return
     * The word_count
     */
    public String getWord_count() {
        return word_count;
    }

    /**
     *
     * @param word_count
     * The word_count
     */
    public void setWord_count(String word_count) {
        this.word_count = word_count;
    }

    /**
     *
     * @return
     * The slideshow_credits
     */
    public String getSlideshow_credits() {
        return slideshow_credits;
    }

    /**
     *
     * @param slideshow_credits
     * The slideshow_credits
     */
    public void setSlideshow_credits(String slideshow_credits) {
        this.slideshow_credits = slideshow_credits;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}