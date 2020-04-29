package com.formax.pexelsgallery;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.formax.pexelsgallery.adapters.OnPhotoListener;
import com.formax.pexelsgallery.adapters.PhotoListAdapter;
import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.util.Constans;
import com.formax.pexelsgallery.viewmodels.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPhotoListener {

    private static final String TAG = "MainActivity";

    private MainViewModel mainViewModel;
    private PhotoListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_photos);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (!checkInternetConnection()) {
            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        } else {
            initRecyclerView();
            subscribeObservers();

            if (mainViewModel.isShowingFirstTime()) {
                showCuratedPhotos();
                mainViewModel.setShowingFirstTime(false);
            }
        }
    }

    private void subscribeObservers() {
        mainViewModel.getPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                if (photos != null) {
                    adapter.setPhotos(photos);
                    mainViewModel.setPerformingQuery(true);
                    if (photos.isEmpty()) {
                        adapter.displayNoMorePhotosImage();
                    }
                }
            }
        });

        mainViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Log.d(TAG, "onChanged: exhausted");
                    adapter.setExhausted();
                }
            }
        });
    }

    private void showCuratedPhotos() {
        mainViewModel.getCuratedPhotos(Constans.PER_PAGE, 1);
    }

    private void initRecyclerView() {
        adapter = new PhotoListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    if (mainViewModel.isShowingCuratedPhotos()) {
                        mainViewModel.getCuratedPhotosNextPage();
                    } else {
                        mainViewModel.searchPhotosNextPage();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!checkInternetConnection()) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                } else {
                    adapter.displayLoading();
                    mainViewModel.searchPhotos(query, Constans.PER_PAGE, 1);
                    if (mainViewModel.isShowingCuratedPhotos()) {
                        mainViewModel.setShowingCuratedPhotos(false);
                    }
                }
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_random:
                Intent intent = new Intent(this, PhotoActivity.class);
                startActivity(intent);

                return true;
            case R.id.menu_curated:
                if (!mainViewModel.isShowingCuratedPhotos()) {
                    mainViewModel.setShowingCuratedPhotos(true);
                    showCuratedPhotos();
                    adapter.notifyDataSetChanged();
                }

                Toast.makeText(this, getResources().getString(R.string.curated_photos), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_favorites:
                Intent intent2 = new Intent(this, FavoritesActivity.class);
                startActivity(intent2);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPhotoClick(int position) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("photo", adapter.getSelectedPhoto(position));
        startActivity(intent);
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        if (!mainViewModel.onBackPressed()) {
            super.onBackPressed();
        } else {
            showCuratedPhotos();
        }
    }
}
