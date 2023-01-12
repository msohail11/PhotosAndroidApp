package com.example.photos58.utility;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.example.photos58.R;
import com.example.photos58.activities.DisplayPhoto;
import com.example.photos58.models.Album;
import com.example.photos58.models.Photo;
import com.example.photos58.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagUtil {


    private static List<Tag> tags = new ArrayList<>();

    public static void addTagDialog(Context context, View mView, ListView tagListView,  List<Album> albums, int selectedAlbumIndex, int selectedPhotoIndex, String startingPhotoName){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText typeInput = (EditText) mView.findViewById(R.id.typeInput);
        final EditText valInput = (EditText) mView.findViewById(R.id.valInput);

        Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);
        Button okBtn = (Button) mView.findViewById(R.id.okBtn);
        alert.setView(mView);
        final AlertDialog alertDialog  = alert.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album album = albums.get(selectedAlbumIndex);
                List<Photo> photos = album.getPhotos();
                Photo photo = photos.get(selectedPhotoIndex);
                String type = typeInput.getText().toString();
                String val = valInput.getText().toString();
                Tag tag = new Tag(type,val);

                if( val.isEmpty() || type.isEmpty() ){
                    Helper.showErrorAlert(context,"You did Input all Values", "A tag must contain a type and a value.");
                    return;
                }
                if(!type.equalsIgnoreCase("person") && !type.equalsIgnoreCase("location")){
                    Helper.showErrorAlert(context,"You did input the proper type: ", "A tag can only be of type \"person\" or \"location\". ");
                    return;
                }

                if(tagExists(tag,photo)){
                    Helper.showErrorAlert(context,"This photo already contains that Tag ", "Please choose different values");
                    return;
                }

                photo.addTag(tag);
                photos.set(selectedPhotoIndex, photo);
                album.setPhotos(photos);
                albums.set(selectedAlbumIndex,album);
                tags = photo.getTags();
                if(photo.toString().equals(startingPhotoName)){
                    Serializer.writeStartingTags(context,photo.getTags());
                    Log.d("hello!",String.valueOf(Serializer.readStartingTags(context)));
                }
                else{
                    DisplayPhoto.updateList(tags);

                }
                DisplayPhoto.tagAdapter.notifyDataSetChanged();
                Serializer.saveChanges(context,albums);

                Log.d("photo", photo.toString());
                Log.d("tags", String.valueOf(photo.getTags()));
                alertDialog.dismiss();
            }
        });

    }





    public static boolean tagExists(Tag tagToBeAdded, Photo photo){
        for(Tag tag : photo.getTags()){
            if(tag.equals(tagToBeAdded)){
                return true;
            }
        }

        return false;
    }

}
