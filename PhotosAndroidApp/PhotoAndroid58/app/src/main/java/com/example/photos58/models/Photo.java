package com.example.photos58.models;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {

    private static final long serialVersionUID = -6563594106876436735L;
    private String caption;
    private String path;
    private String uri;
    private String parentAlbum;
    private ImageView img;
    private List<Tag> tags = new ArrayList<>();

    //--constructor--
    public Photo(String uri, String caption, String parentAlbum){
        this.uri = uri;
        this.caption = caption;
        this.parentAlbum = parentAlbum;
    }



    //-------caption----------
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    //-------img----------

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    //-------Parent Album----------

    public String getParentAlbum() {
        return parentAlbum;
    }

    public void setParentAlbum(String parentAlbum) {
        this.parentAlbum = parentAlbum;
    }
    //-------uri----------

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    //-------Tags----------

    public List<Tag> getTags(){
        return this.tags;
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
    }



    @Override
    public String toString() {
        return "Photo{" +
                "uri='" + uri + '\'' +
                '}';
    }
}
