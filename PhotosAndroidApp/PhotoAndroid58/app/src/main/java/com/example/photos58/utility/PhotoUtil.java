package com.example.photos58.utility;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import com.example.photos58.R;
import com.example.photos58.activities.ShowAlbum;
import com.example.photos58.models.Album;
import com.example.photos58.models.Photo;

import java.util.List;



public class PhotoUtil {

    private static Album destAlbum;
    private static int destIndex;

    public static void movePhotoDialog(Context context, View mView, List<Album> albums, Album srcAlbum, int srcIndex, Photo photo){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        Button cancelMoveBtn = (Button) mView.findViewById(R.id.cancelMoveBtn);
        Button moveBtn = (Button) mView.findViewById(R.id.moveBtn);
        ListView albumListView = (ListView) mView.findViewById(R.id.albumListView);
        ArrayAdapter<Album> adapter = new ArrayAdapter<Album>(context, android.R.layout.simple_list_item_1, albums);
        albumListView.setAdapter(adapter);
        albumListView.setOnItemClickListener((p,v,pos,id) ->{
            destAlbum = adapter.getItem(pos);
            destIndex = pos;
        });

        alert.setView(mView);
        final AlertDialog alertDialog  = alert.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        cancelMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destAlbum == null){
                    Helper.showErrorAlert(context,"Invalid Selection","You must select an album!");
                    return;
                }
                if(destAlbum.getName().equals(srcAlbum.getName())){
                    Helper.showErrorAlert(context,"Invalid Operation","The photo is already contained in this album! Choose a different Album.");
                    return;
                }
                List<Photo> srcPhotos = srcAlbum.getPhotos();
                srcPhotos.remove(photo);
                albums.set(srcIndex, srcAlbum);
                List<Photo> destPhotos = destAlbum.getPhotos();
                destPhotos.add(photo);
                photo.setParentAlbum(destAlbum.getName());
                albums.set(destIndex,destAlbum);
                ShowAlbum.photoAdapter.notifyDataSetChanged();
                Serializer.saveChanges(context, albums);
                alertDialog.dismiss();
            }
        });
    }




}
