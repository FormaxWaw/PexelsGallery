package com.formax.pexelsgallery;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.formax.pexelsgallery.adapters.FavoritesListAdapter;
import com.formax.pexelsgallery.adapters.OnBookmarkListener;
import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.viewmodels.FavoritesViewModel;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements OnBookmarkListener {

    private static final String TAG = "FavoritesActivity";

    private FavoritesViewModel viewModel;
    private FavoritesListAdapter favoritesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(FavoritesViewModel.class);

        initToolbar();

        if (!checkInternetConnection()) {
            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        } else {

            initRecyclerView();

            viewModel.getAllPhotosFromDatabase().observe(this, new Observer<List<Photo>>() {
                @Override
                public void onChanged(List<Photo> photos) {
                    favoritesListAdapter.setPhotos(photos);
                }
            });
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.saved_photos);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_favorites);
        favoritesListAdapter = new FavoritesListAdapter(this, this);
        recyclerView.setAdapter(favoritesListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBookmarkClick(int position) {
        viewModel.delete(favoritesListAdapter.getPhotoPosition(position));
        favoritesListAdapter.notifyItemRemoved(position);
        Toast.makeText(this, getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoClick(int position) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("photo", favoritesListAdapter.getPhotoPosition(position));
        startActivity(intent);
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
