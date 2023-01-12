package com.example.photos58.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.photos58.R;
import com.example.photos58.models.Album;
import com.example.photos58.models.Photo;
import com.example.photos58.models.PhotoAdapter;
import com.example.photos58.models.Tag;
import com.example.photos58.utility.Serializer;

import java.util.ArrayList;
import java.util.List;


public class SearchResults extends AppCompatActivity {
    private String type,val;
    private Tag targetTag;
    private int selectedAlbumIndex;
    ListView searchListView;
    Button backToHomeBtn, backToAlbumBtn;
    TextView tagTextView;
    PhotoAdapter adapter;
    List<Photo> photos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        setupView();
        backToHomeBtn.setOnClickListener(this::handleBackToHome);
        backToAlbumBtn.setOnClickListener(this::handleBackToAlbum);
    }



    private void handleBackToHome(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    private void handleBackToAlbum(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(HomePage.SELECTED_INDEX, selectedAlbumIndex);
        Intent intent = new Intent(this, ShowAlbum.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setupView(){
        Intent intent = getIntent();
        type = intent.getStringExtra(ShowAlbum.TAG_TYPE);
        val = intent.getStringExtra(ShowAlbum.TAG_VALUE);
        selectedAlbumIndex = intent.getIntExtra(ShowAlbum.SELECTED_ALBUM_INDEX, 0);

        backToHomeBtn = (Button) findViewById(R.id.backToHomeBtn);
        backToAlbumBtn = (Button) findViewById(R.id.backToAlbumBtn);
        tagTextView = (TextView) findViewById(R.id.tagTextView);
        searchListView = (ListView) findViewById(R.id.searchListView);

        tagTextView.setText( getString(R.string.search_title, type,val));
        photos = getPhotoResults();
        adapter = new PhotoAdapter(this, photos);
        searchListView.setAdapter(adapter);

    }



    private List<Photo> getPhotoResults(){
        List<Album>albums = null;
        List<Photo> result = new ArrayList<>();
        Tag target = new Tag(type,val);
        albums = Serializer.readFromFile(this, albums);
        for(Album album : albums){
            for(Photo photo: album.getPhotos()){
                for(Tag tag: photo.getTags()){
                    if(tag.getType().equalsIgnoreCase(target.getType()) && tag.getVal().startsWith(target.getVal())){
                        if(!result.contains(photo)){
                            result.add(photo);
                        }
                    }
                }
            }
        }
        return result;
    }


}
