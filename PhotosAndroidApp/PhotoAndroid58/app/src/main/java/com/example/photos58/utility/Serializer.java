package com.example.photos58.utility;

import android.content.Context;

import com.example.photos58.models.Album;
import com.example.photos58.models.Tag;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Serializer {


    public static void saveToFile(Context context, List<Album> albums) {
        try {
            FileOutputStream fos = context.openFileOutput("albums.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            for(Album album : albums) {
                os.writeObject(album);
            }
            os.close();
            fos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void emptyFile(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("albums.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.close();
            fos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Album> readFromFile(Context context, List<Album> albList) {
        List<Album> albums = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput("albums.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            while(fileInputStream.available() > 0 ){
                Album album = (Album) objectInputStream.readObject();
                if(albList!= null){
                    if(!AlbumUtil.isAlbumInList(album.getName(), albList)){
                        albums.add(album);
                    }
                }
                else{
                    albums.add(album);
                }

            }
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public static void saveChanges(Context context , List<Album> albums){
        emptyFile(context);
        saveToFile(context, albums);
    }




    public static void writeStartingTags(Context context, List<Tag> tags){
        try {
            FileOutputStream fos = context.openFileOutput("tags.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            for(Tag tag : tags) {
                os.writeObject(tag);
            }
            os.close();
            fos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }





    public static List<Tag> readStartingTags(Context context) {
        List<Tag> tags = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput("tags.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            while(fileInputStream.available() > 0 ){
                Tag tag = (Tag) objectInputStream.readObject();
                tags.add(tag);
            }
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tags;
    }



    //        if(selectedPhotoIndex != 0){
//            prevPhoto = photos.get(selectedPhotoIndex - 1);
//        }
//        if(selectedPhotoIndex != photos.size()-1){
//            nextPhoto = photos.get(selectedPhotoIndex + 1);
//            Serializer.writeNextTags(context,nextPhoto.getTags());
//        }
//        Serializer.writeNextTags(context,tags);






}



