package com.example.photos58.utility;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.photos58.R;
import com.example.photos58.models.Album;

import java.util.List;

import static com.example.photos58.activities.HomePage.albumAdapter;

public class AlbumUtil {
    public static boolean isAlbumInList(String name, List<Album> albums){
        for(Album album : albums){
            if(album.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public static void createAlbumDialog(Context context, View mView, List<Album> albums, TextView header, String newHeader){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText txtInput = (EditText) mView.findViewById(R.id.txt_input);
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
                String name = txtInput.getText().toString();
                Album newAlb = new Album(name);
                if(AlbumUtil.isAlbumInList(name,albums)){
                    Helper.showErrorAlert(context,"Choose a different name", "An album with this name already exists");
                    return;
                }
                albums.add(newAlb);
                albumAdapter.notifyDataSetChanged();
                Serializer.saveChanges(context, albums);
                header.setText(newHeader);
                alertDialog.dismiss();
            }
        });
    }


    public static void renameAlbumDialog(Context context, View mView, List<Album> albums, Album selectedAlbum, int pos){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText txtInput = (EditText) mView.findViewById(R.id.txt_input);
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
                String name = txtInput.getText().toString();
                if(AlbumUtil.isAlbumInList(name,albums)){
                    Helper.showErrorAlert(context,"Choose a different name", "An album with this name already exists");
                    return;
                }
                selectedAlbum.setName(name);
                albums.set(pos, selectedAlbum);
                albumAdapter.notifyDataSetChanged();
                Serializer.saveChanges(context, albums);
                alertDialog.dismiss();
            }
        });
    }



}
