package com.example.photos58.models;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.photos58.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;


public class PhotoAdapter extends ArrayAdapter<Photo> implements Serializable {
    public PhotoAdapter(Context context, List<Photo> photos) {
        super(context, 0, photos);
    }
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Photo photo = getItem(position);
       Context context = getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row,  parent, false);
        }

        TextView Caption = (TextView) convertView.findViewById(R.id.caption);
        TextView subCaption = (TextView) convertView.findViewById(R.id.subCaption);
        ImageView imgContainer = (ImageView) convertView.findViewById(R.id.imgContainer);


        try{
            Uri uri = Uri.parse(photo.getUri());
            String name = getFileName(context,uri);
            Caption.setText(name);
            subCaption.setText("Album: " + photo.getParentAlbum());
            if(photo.getUri()!= null){
                final InputStream imageStream = context.getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgContainer.setImageBitmap(selectedImage);
            }

        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }


        return convertView;
    }

    public String getFileName(Context context, Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {

            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }

        }
        if (fileName == null) {
            fileName = uri.getPath();
            int offset = fileName.lastIndexOf('/');
            if (offset != -1) {
                fileName = fileName.substring(offset + 1);
            }
        }
        return fileName;
    }


}
