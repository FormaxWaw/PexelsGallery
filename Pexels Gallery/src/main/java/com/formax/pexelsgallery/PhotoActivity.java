package com.formax.pexelsgallery;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.viewmodels.PhotoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Random;

public class PhotoActivity extends AppCompatActivity {
    private static final String TAG = "PhotoActivity";

    private Photo mPhoto;
    private PhotoViewModel viewModel;
    private ImageView image;
    private ImageView imageBookmark;
    private TextView textPhotographer;
    private RequestOptions requestOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        image = findViewById(R.id.img_image);
        imageBookmark = findViewById(R.id.img_bookmark);
        textPhotographer = findViewById(R.id.tv_photographer);
        FloatingActionButton fab = findViewById(R.id.fab_download);

        initToolbar();

        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(PhotoViewModel.class);

        requestOptions = new RequestOptions().placeholder(R.color.lightGrey);

        if (!checkInternetConnection()) {
            Toast.makeText(this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = getIntent();
            if (intent.hasExtra("photo")) {
                mPhoto = getIntent().getParcelableExtra("photo");
                assert mPhoto != null;
                initWidgets(mPhoto);
                checkIsExistInDatabase(mPhoto);
            } else {
                subscribeObservers();

                if (!viewModel.isShowingPhoto()) {
                    Random random = new Random();
                    int rndPage = random.nextInt(1000 - 1) + 1;
                    viewModel.getRandomPhoto(1, rndPage);
                    viewModel.setShowingPhoto(true);
                }
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhotoActivity.this, R.style.AlertDialog);
                    alertDialog.setCancelable(true).setTitle(getResources().getString(R.string.downloading)).setPositiveButton(getResources().getString(R.string.original), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadImage(mPhoto.getSrc().getOriginal());
                        }
                    }).setNegativeButton(getResources().getString(R.string.small), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadImage(mPhoto.getSrc().getLarge2x());
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();

                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageInFullScreen(mPhoto);
                }
            });

            imageBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewModel.isSavedInDatabase()) {
                        viewModel.delete(mPhoto);

                        imageBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_border, null));
                        Toast.makeText(PhotoActivity.this, getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    } else {
                        viewModel.insert(mPhoto);

                        imageBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_filled, null));
                        viewModel.setSavedInDatabase(true);
                        Toast.makeText(PhotoActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void checkIsExistInDatabase(Photo photo) {
        viewModel.getAllPhotosFromDatabase().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                for (int i = 0; i < photos.size(); i++) {
                    if (photos.get(i).getUrl().contains(photo.getUrl())) {
                        imageBookmark.setImageResource(R.drawable.ic_bookmark_filled);
                        viewModel.setSavedInDatabase(true);
                    }
                }
            }
        });
    }

    void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void subscribeObservers() {
        viewModel.getPhoto().observe(this, new Observer<Photo>() {
            @Override
            public void onChanged(Photo photo) {
                mPhoto = photo;
                initWidgets(mPhoto);
            }
        });
    }

    public void initWidgets(Photo photo) {
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(photo.getSrc().getLarge())
                .into(image);

        textPhotographer.setText(getResources().getString(R.string.photo_by, photo.getPhotographer()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");

                i.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.photo_by, mPhoto.getPhotographer()) + "\n" + mPhoto.getUrl());
                startActivity(Intent.createChooser(i, "Share via"));
                return true;
            case R.id.menu_web:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPhoto.getUrl()));
                startActivity(browserIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //check permission and start downloading photo
    public void downloadImage(String url) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, uri.getLastPathSegment());
            downloadManager.enqueue(request);
        }
    }

    //show popup with Photo
    public void showImageInFullScreen(Photo photo) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ProgressBar progressBar = new ProgressBar(this);
        LinearLayout.LayoutParams layoutParamss = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamss.gravity = Gravity.CENTER;
        builder.addContentView(progressBar, layoutParamss);

        requestOptions = new RequestOptions().placeholder(R.color.lightGrey);
        ImageView imageView = new ImageView(this);
        Glide.with(PhotoActivity.this)
                .setDefaultRequestOptions(requestOptions)
                .load(photo.getSrc().getLarge2x())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.show();
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}