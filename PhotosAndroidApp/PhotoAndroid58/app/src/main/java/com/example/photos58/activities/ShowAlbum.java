package com.example.photos58.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.photos58.R;
import com.example.photos58.models.Album;
import com.example.photos58.models.Photo;
import com.example.photos58.models.PhotoAdapter;
import com.example.photos58.utility.Helper;
import com.example.photos58.utility.PhotoUtil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.photos58.activities.HomePage.albumAdapter;
import static com.example.photos58.utility.Serializer.*;

public class ShowAlbum extends AppCompatActivity implements Serializable {
    private Context context = ShowAlbum.this;
    private TextView header;
    List<Album> albums = new ArrayList<>();
    private Album selectedAlbum;
    private Photo selectedPhoto;
    private int selectedPhotoIndex;
    private ListView photoListView;
    private List<Photo> photos = new ArrayList<>();
    private Button addBtn, displayBtn, removeBtn, moveBtn, searchBtn;
    private ImageView imgView;
    public static PhotoAdapter photoAdapter;
    int selectedIndex;
    public static final int PICK_IMAGE = 1;
    public static final int DISPLAY_REQUEST = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public static final String INDEX_KEY = "index key";
    public static final String PHOTOS_KEY = "photo key";
    public static final String SELECTED_ALBUM_INDEX = "album key";
    public static final String TAG_TYPE = "tagz";
    public static final String TAG_VALUE = "valz";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_album_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albums = readFromFile(context, albums);
        setupView();
        addBtn.setOnClickListener(this::addPhoto);
        removeBtn.setOnClickListener(this::removePhoto);
        moveBtn.setOnClickListener(this::movePhoto);
        displayBtn.setOnClickListener(this::displayPhoto);
        searchBtn.setOnClickListener(this::searchByTag);
    }




    private void searchByTag(View view){
        View mView = getLayoutInflater().inflate(R.layout.add_tag_dialog, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText typeInput = (EditText) mView.findViewById(R.id.typeInput);
        final EditText valInput = (EditText) mView.findViewById(R.id.valInput);
        Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);
        Button okBtn = (Button) mView.findViewById(R.id.okBtn);
        okBtn.setText("Search");
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

                String type = typeInput.getText().toString();
                String val = valInput.getText().toString();

                if( val.isEmpty() || type.isEmpty() ){
                    Helper.showErrorAlert(context,"You did Input all Values", "Enter both type and value.");
                    return;
                }
                if(!type.equalsIgnoreCase("person") && !type.equalsIgnoreCase("location")){
                    Helper.showErrorAlert(context,"You did input the proper type: ", "A tag can only be of type \"person\" or \"location\". ");
                    return;
                }
                //need to send type string and key string through intent
                Intent i = new Intent(context, SearchResults.class);
                i.putExtra(SELECTED_ALBUM_INDEX, selectedIndex);
                i.putExtra(TAG_TYPE,type);
                i.putExtra(TAG_VALUE,val);
                startActivity(i);
                alertDialog.dismiss();
            }
        });


    }


    private void addPhoto(View view){
        Intent getIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select a Photo From your Device");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {

            final Uri imageUri = data.getData();
            Photo photo = new Photo(imageUri.toString(), imageUri.toString(), selectedAlbum.getName());
            photos.add(photo);
            photoAdapter.notifyDataSetChanged();
            albums.set(selectedIndex,selectedAlbum);
            saveChanges(context, albums);
        }
        //&& resultCode == RESULT_OK && null != data
        if(requestCode == DISPLAY_REQUEST ){
                selectedIndex = data.getIntExtra(HomePage.SELECTED_INDEX, -1);;
                selectedAlbum = albumAdapter.getItem(selectedIndex);
                photos = selectedAlbum.getPhotos();
        }
    }


    public void removePhoto(View view){
        if(selectedPhoto == null){
            Helper.showErrorAlert(context,"Invalid Deletion", "Select the Photo you wish to delete");
            return;
        }
        photos.remove(selectedPhoto);
        photoAdapter.notifyDataSetChanged();
        albums.set(selectedIndex,selectedAlbum);
        saveChanges(context,albums);
        selectedPhoto = null;
        selectedPhotoIndex = -1;
    }

    public void movePhoto(View view){
        if(selectedPhoto == null){
            Helper.showErrorAlert(context,"Invalid Action", "Select the Photo you wish to move");
            return;
        }
        View mView = getLayoutInflater().inflate(R.layout.move_photo_dialog, null);
        PhotoUtil.movePhotoDialog(context,mView,albums,selectedAlbum, selectedIndex, selectedPhoto);

    }

    public void displayPhoto(View view){
        if(selectedPhoto == null){
            Helper.showErrorAlert(context,"Invalid Action", "Select the Photo you wish to display");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX_KEY, selectedPhotoIndex);
        bundle.putInt(HomePage.SELECTED_INDEX, selectedIndex);
        Intent intent = new Intent(context, DisplayPhoto.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,DISPLAY_REQUEST);
    }


    public void setupView(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            selectedIndex = bundle.getInt(HomePage.SELECTED_INDEX);
            selectedAlbum = albumAdapter.getItem(selectedIndex);
            photos = selectedAlbum.getPhotos();
        }


        addBtn = (Button) findViewById(R.id.addBtn);
        displayBtn = (Button) findViewById(R.id.displayBtn);
        removeBtn = (Button) findViewById(R.id.removeBtn);
        moveBtn = (Button) findViewById(R.id.moveBtn);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        photoListView = (ListView) findViewById(R.id.photoListView);
        photoAdapter = new PhotoAdapter(context, photos);
        photoListView.setAdapter(photoAdapter);
        photoListView.setOnItemClickListener((p,v,pos,id) ->{
            selectedPhoto = (Photo) photoAdapter.getItem(pos);
            selectedPhotoIndex = pos;
        });

        header = (TextView) findViewById(R.id.albumHeader);
        header.setText(getString(R.string.album_title, albumAdapter.getItem(selectedIndex)));
    }





}
