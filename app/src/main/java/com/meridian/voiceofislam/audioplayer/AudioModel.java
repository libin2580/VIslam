package com.meridian.voiceofislam.audioplayer;

import java.util.ArrayList;

/**
 * Created by user 1 on 18-08-2016.
 */
public class AudioModel {
    private  String category_id;
    private String sub_category_id;
    private String audio;
    private String thumbnail;
    private String Orator;

    public ArrayList<String> getARRAY_NEW() {
        return ARRAY_NEW;
    }

    public void setARRAY_NEW(ArrayList<String> ARRAY_NEW) {
        this.ARRAY_NEW = ARRAY_NEW;
    }

    private String filter_content;
    private ArrayList<String> ARRAY_NEW;
    public String getNEWW() {
        return NEWW;
    }

    public void setNEWW(String NEWW) {
        this.NEWW = NEWW;
    }

    private String NEWW;

    public String getFilter_content() {
        return filter_content;
    }

    public void setFilter_content(String filter_content) {
        this.filter_content = filter_content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOrator() {
        return Orator;
    }

    public void setOrator(String orator) {
        Orator = orator;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getProgramme() {
        return Programme;
    }

    public void setProgramme(String programme) {
        Programme = programme;
    }

    private String Subject;
    private String Programme;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }


    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }
}
