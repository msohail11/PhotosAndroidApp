package com.example.photos58.activities;

/*
    @author: Waleed Rizwan war56
    @author: Mohammad Sohail mms458
 */

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.photos58.R;
import com.example.photos58.models.Album;
import com.example.photos58.utility.AlbumUtil;
import com.example.photos58.utility.Helper;
import com.example.photos58.utility.Serializer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements Serializable {

    private ListView listView;
    private Button createBtn, deleteBtn, openBtn, renameBtn;
    private TextView header;
    private List<Album> albums = new ArrayList<>();
    public static ArrayAdapter<Album> albumAdapter;
    private EditText albumInput;
    Album selectedAlbum;
    int selectedIndex;
    public static final String SELECTED_INDEX = "selected_index";
    public static final String SELECTED_ALBUM = "selected_album";
    Context context = HomePage.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setTitle("Photo Library");
        setContentView(R.layout.home_page);
        albums = Serializer.readFromFile(context,albums);
        setUpViewElements();
        createBtn.setOnClickListener(this::createAlbum);
        renameBtn.setOnClickListener(this::renameAlbum);
        deleteBtn.setOnClickListener(this::deleteAlbum);
        openBtn.setOnClickListener(this::openAlbum);
    }

    private void setUpViewElements(){
        header = (TextView) findViewById(R.id.albumHeader);
        header.setText(getString(R.string.albums_title, albums.size()));
        createBtn = (Button) findViewById(R.id.createBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        openBtn = (Button) findViewById(R.id.openBtn);
        renameBtn = (Button)findViewById(R.id.renameBtn);
        albumAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albums);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(albumAdapter);
        listView.setOnItemClickListener((p,v,pos,id) -> {
            selectedAlbum = (Album) albumAdapter.getItem(pos);
            selectedIndex = pos;
        });
    }

    private void createAlbum(View view) {
        View mView = getLayoutInflater().inflate(R.layout.custom_album_dialog, null);
        String newHeader = getString(R.string.albums_title, albums.size() + 1 );
        AlbumUtil.createAlbumDialog(context, mView, albums, header,newHeader);
    }

    private void renameAlbum(View view){
        if(selectedAlbum == null){
            Helper.showErrorAlert(context,"You did not select an album", "Select the album you wish to rename");
            return;
        }
        View mView = getLayoutInflater().inflate(R.layout.custom_album_dialog, null);
        AlbumUtil.renameAlbumDialog(context, mView, albums, selectedAlbum, selectedIndex);

    }

    private void deleteAlbum(View view){
        if(selectedAlbum == null){
            Helper.showErrorAlert(context,"You did not select an album", "Select the album you wish to delete");
            return;
        }
        albums.remove(selectedAlbum);
        albumAdapter.notifyDataSetChanged();
        header.setText(getString(R.string.albums_title, albums.size()));
        selectedAlbum = null;
        selectedIndex = -1;
        Serializer.saveChanges(context,albums);
    }

    private void openAlbum(View view){
        if(selectedAlbum == null){
            Helper.showErrorAlert(context,"You did not select an album", "Select the album you wish to open");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_INDEX, selectedIndex);
        Intent intent = new Intent(this, ShowAlbum.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }



}


