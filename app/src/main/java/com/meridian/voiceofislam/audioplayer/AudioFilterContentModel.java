package com.meridian.voiceofislam.audioplayer;

/**
 * Created by user 1 on 18-08-2016.
 */
public class AudioFilterContentModel {
    private  String category_id;
    private String sub_category_id;
    private String audio;
    private String thumbnail;

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
