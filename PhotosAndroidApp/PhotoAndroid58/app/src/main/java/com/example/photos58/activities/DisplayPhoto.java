package com.example.photos58.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.example.photos58.models.Album;
import com.example.photos58.models.Photo;
import com.example.photos58.models.Tag;
import com.example.photos58.utility.Helper;
import com.example.photos58.utility.Serializer;
import com.example.photos58.utility.TagUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.photos58.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.photos58.utility.Serializer.saveChanges;

public class DisplayPhoto extends AppCompatActivity {
    Context context = DisplayPhoto.this;
    private int selectedPhotoIndex;
    private int selectedAlbumIndex;
    private Photo prevPhoto, selectedPhoto, nextPhoto;
    List<Photo> photos = new ArrayList<>();
    TextView slideNum;
    ImageView slide;
    Button prevBtn, nextBtn, addTagBtn, deleteTagBtn;
    ListView tagListView;
    List<Album> albums = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();
    List<Tag> newTags = new ArrayList<>();
    List<Tag> startingTags = new ArrayList<>();
    private Tag selectedTag;
    private int selectedTagIndex = -1;
    String startingPhotoName;
    private Album album;
    public static ArrayAdapter<Tag> tagAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photo_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupView();

        //set up back intent here...
        Intent i = new Intent(context, ShowAlbum.class);
        i.putExtra(HomePage.SELECTED_INDEX, selectedAlbumIndex); // insert your extras here
        setResult(ShowAlbum.DISPLAY_REQUEST, i);

        nextBtn.setOnClickListener(this::goNextSlide);
        prevBtn.setOnClickListener(this::goPrevSlide);
        addTagBtn.setOnClickListener(this::addTag);
        deleteTagBtn.setOnClickListener(this::deleteTag);

    }



    private void addTag(View view) {
        View mView = getLayoutInflater().inflate(R.layout.add_tag_dialog, null);
        TagUtil.addTagDialog(context,mView,tagListView,albums,selectedAlbumIndex, selectedPhotoIndex, startingPhotoName);
    }

    private void deleteTag(View view){
        if(selectedTag == null){
            Helper.showErrorAlert(context,"You did not select a tag", "Select the tag you wish to delete");
            return;
        }
        Photo currPhoto = photos.get(selectedPhotoIndex);
        currPhoto.getTags().remove(selectedTag);
        tags.remove(selectedTag);
        tagAdapter.notifyDataSetChanged();
        if(currPhoto.toString().equals(startingPhotoName)){
            Serializer.writeStartingTags(context,tags);
        }
        albums.set(selectedAlbumIndex,album);
        saveChanges(context,albums);
        selectedTag = null;
        selectedTagIndex = -1;
    }


    private void goNextSlide(View view) {
        int nextIndex = selectedPhotoIndex + 1;
        Photo nextPhoto = photos.get(nextIndex);
        List<Tag> nextTags = nextPhoto.getTags();
        if(nextPhoto.toString().equals(selectedPhoto.toString())){
            nextTags = Serializer.readStartingTags(context);
        }
        updateList(nextTags);
        setImg(nextPhoto);
        selectedPhotoIndex = nextIndex;
        slideNum.setText(getString(R.string.slide_title, selectedPhotoIndex + 1, photos.size()));
        handleButtonToggles();
    }

    private void goPrevSlide(View view) {
        int prevIndex = selectedPhotoIndex - 1;
        Photo prevPhoto = photos.get(prevIndex);
        List<Tag> prevTags = prevPhoto.getTags();
        if(prevPhoto.toString().equals(selectedPhoto.toString())){
            prevTags = Serializer.readStartingTags(context);
        }
        updateList(prevTags);
        setImg(prevPhoto);
        selectedPhotoIndex = prevIndex;
        slideNum.setText(getString(R.string.slide_title, selectedPhotoIndex + 1, photos.size()));
        handleButtonToggles();
    }

    public static void updateList(List<Tag> newList){
        tagAdapter.clear();
        tagAdapter.addAll(newList);
        tagAdapter.notifyDataSetChanged();
    }


    public void setupView(){
        albums = Serializer.readFromFile(context, albums);
        Bundle bundle = getIntent().getExtras();
        selectedPhotoIndex = bundle.getInt(ShowAlbum.INDEX_KEY);
        selectedAlbumIndex = bundle.getInt(HomePage.SELECTED_INDEX);
        album = albums.get(selectedAlbumIndex);
        photos = album.getPhotos();
        selectedPhoto = photos.get(selectedPhotoIndex);

        slide = (ImageView) findViewById(R.id.slide);
        slideNum = (TextView) findViewById(R.id.slideNum);
        prevBtn = (Button) findViewById(R.id.prevBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        addTagBtn = (Button) findViewById(R.id.addTagBtn);
        deleteTagBtn = (Button) findViewById(R.id.deleteTagBtn);

        setImg(selectedPhoto);
        slideNum.setText(getString(R.string.slide_title, selectedPhotoIndex + 1, photos.size()));
        tags = selectedPhoto.getTags();
        tagListView = (ListView) findViewById(R.id.tagListView);
        tagAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tags);
        tagListView.setAdapter(tagAdapter);
        tagListView.setOnItemClickListener((p,v,pos,id) -> {
            selectedTag = (Tag) tagAdapter.getItem(pos);
            selectedTagIndex = pos;
        });

        startingPhotoName = selectedPhoto.toString();
        startingTags = tags;
        Serializer.writeStartingTags(context, startingTags);


    }


    public void setImg(Photo photo){
        try{
            Uri uri = Uri.parse(photo.getUri());
            final InputStream imageStream = context.getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            slide.setImageBitmap(selectedImage);
            handleButtonToggles();
        }

        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }


    public void handleButtonToggles(){
        if(selectedPhotoIndex == 0){
            prevBtn.setEnabled(false);
        }
        else{
            prevBtn.setEnabled(true);
        }
        if(selectedPhotoIndex == photos.size()-1){
            nextBtn.setEnabled(false);
        }
        else{
            nextBtn.setEnabled(true);
        }
    }

}
