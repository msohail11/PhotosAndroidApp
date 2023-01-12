package com.example.photos58.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable{

    private static final long serialVersionUID = 5939558774788445264L;
    private String name;
    private List<Photo> photos = new ArrayList<>();

    //-------constructor----------

    public Album(String name){
        this.name = name;
    }

    //-------photo----------

    public void addPhoto(Photo photo){
        photos.add(photo);
    }

    public void deletePhoto(Photo photo){
        photos.remove(photo);
    }

    //-------name----------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //-------photo lists----------

    public int getNumOfPhotos() {
        return photos.size();
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    //-------toString----------
    @Override
    public String toString() {
        return name;
    }

}
