package com.formax.pexelsgallery.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.formax.pexelsgallery.AppExecutors;
import com.formax.pexelsgallery.models.PexelsResponse;
import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.util.Constans;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class PexelsAPIClient {

    private static PexelsAPIClient instance;
    private MutableLiveData<List<Photo>> mPhotos;
    private MutableLiveData<Photo> mRandomPhoto;

    private RetrievePhotosRunnable retrievePhotosRunnable;
    private SearchPhotosRunnable searchPhotosRunnable;
    private RetrieveRandomPhoto retrieveRandomPhoto;

    private PexelsAPIClient() {
        mPhotos = new MutableLiveData<>();
        mRandomPhoto = new MutableLiveData<>();
    }

    public static PexelsAPIClient getInstance() {
        if (instance == null) {
            instance = new PexelsAPIClient();
        }
        return instance;
    }

    public LiveData<List<Photo>> getPhotos() {
        return mPhotos;
    }

    public LiveData<Photo> getRandomPhoto() {
        return mRandomPhoto;
    }

    public void getCuratedPhotos(int perPage, int page) {
        if (retrievePhotosRunnable != null) {
            retrievePhotosRunnable = null;
        }
        retrievePhotosRunnable = new RetrievePhotosRunnable(perPage, page);

        final Future handler = AppExecutors.get().getNetworkIO().submit(retrievePhotosRunnable);

        AppExecutors.get().getNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, Constans.TIME_OUT, TimeUnit.MILLISECONDS);
    }

    public void searchPhotos(String query, int perPage, int page) {
        if (searchPhotosRunnable != null) {
            searchPhotosRunnable = null;
        }
        searchPhotosRunnable = new SearchPhotosRunnable(query, perPage, page);

        final Future handler = AppExecutors.get().getNetworkIO().submit(searchPhotosRunnable);

        AppExecutors.get().getNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, Constans.TIME_OUT, TimeUnit.MILLISECONDS);
    }

    public void getRandomPhoto(int perPage, int page) {
        if (retrieveRandomPhoto != null) {
            retrieveRandomPhoto = null;
        }
        retrieveRandomPhoto = new RetrieveRandomPhoto(perPage, page);

        final Future handler = AppExecutors.get().getNetworkIO().submit(retrieveRandomPhoto);

        AppExecutors.get().getNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, Constans.TIME_OUT, TimeUnit.MILLISECONDS);
    }

    public void cancelRequest() {
        if (retrievePhotosRunnable != null) {
            retrievePhotosRunnable.cancelRequest();
        }
        if (searchPhotosRunnable != null) {
            searchPhotosRunnable.cancelRequest();
        }
        if (retrieveRandomPhoto != null) {
            retrieveRandomPhoto.cancelRequest();
        }
    }

    private class RetrievePhotosRunnable implements Runnable {

        boolean cancelRequest;
        private int perPage;
        private int page;

        public RetrievePhotosRunnable(int perPage, int page) {
            this.perPage = perPage;
            this.page = page;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getCuratedPhotos(perPage, page).execute();
                Log.d(TAG, "RetrievePhotosRunnable: " + response.toString());
                if (cancelRequest) {
                    return;
                }
                if (response.isSuccessful()) {
                    PexelsResponse pexelsResponse = (PexelsResponse) response.body();
                    List<Photo> photoList = pexelsResponse.getPhotos();

                    if (page == 1) {
                        mPhotos.postValue(photoList);
                    } else {
                        List<Photo> current = mPhotos.getValue();
                        current.addAll(photoList);
                        mPhotos.postValue(current);
                    }
                } else {
                    Log.e(TAG, "run: " + response.errorBody().string());
                    mPhotos.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mPhotos.postValue(null);
            }
        }

        private Call<PexelsResponse> getCuratedPhotos(int perPage, int page) {
            return ServiceGenerator.getPexelsAPI().getCuratedPhotos(perPage, page);
        }

        void cancelRequest() {
            cancelRequest = true;
        }
    }

    private class SearchPhotosRunnable implements Runnable {

        boolean cancelRequest;
        private String query;
        private int perPage;
        private int page;

        SearchPhotosRunnable(String query, int perPage, int page) {
            this.query = query;
            this.perPage = perPage;
            this.page = page;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = searchPhotos(query, perPage, page).execute();
                Log.d(TAG, "SearchPhotosRunnable: " + response.toString());
                if (cancelRequest) {
                    return;
                }
                if (response.isSuccessful()) {
                    PexelsResponse pexelsResponse = (PexelsResponse) response.body();
                    List<Photo> photoList = pexelsResponse.getPhotos();
                    if (page == 1) {
                        mPhotos.postValue(photoList);
                    } else {
                        List<Photo> current = mPhotos.getValue();
                        current.addAll(photoList);
                        mPhotos.postValue(current);
                    }
                } else {
                    Log.e(TAG, "run: " + response.errorBody().string());
                    mPhotos.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mPhotos.postValue(null);
            }
        }

        private Call<PexelsResponse> searchPhotos(String query, int perPage, int page) {
            return ServiceGenerator.getPexelsAPI().searchPhotos(query, perPage, page);
        }

        void cancelRequest() {
            cancelRequest = true;
        }
    }

    private class RetrieveRandomPhoto implements Runnable {

        boolean cancelRequest;
        private int perPage;
        private int page;

        RetrieveRandomPhoto(int perPage, int page) {
            this.perPage = perPage;
            this.page = page;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRandomPhoto(perPage, page).execute();
                Log.d(TAG, "SearchPhotosRunnable: " + response.toString());
                if (cancelRequest) {
                    return;
                }
                if (response.isSuccessful()) {
                    PexelsResponse pexelsResponse = (PexelsResponse) response.body();
                    List<Photo> photoList = pexelsResponse.getPhotos();
                    Photo photo = photoList.get(0);
                    mRandomPhoto.postValue(photo);
                } else {
                    Log.e(TAG, "run: " + response.errorBody().string());
                    mRandomPhoto.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRandomPhoto.postValue(null);
            }
        }

        private Call<PexelsResponse> getRandomPhoto(int perPage, int page) {
            return ServiceGenerator.getPexelsAPI().getRandomPhoto(perPage, page);
        }

        void cancelRequest() {
            cancelRequest = true;
        }
    }

}
