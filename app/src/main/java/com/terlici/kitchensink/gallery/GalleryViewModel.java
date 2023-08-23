package com.terlici.kitchensink.gallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

public class GalleryViewModel extends AndroidViewModel {

    private static final int PAGE_SIZE = 128;
    public final LiveData<PagingData<GalleryItem>> mediaLiveData;

    public GalleryViewModel(@NonNull Application application) {
        super(application);

        Pager<Long, GalleryItem> pager = new Pager<>(new PagingConfig(PAGE_SIZE), () ->
                new GalleryPagingSource(getApplication().getContentResolver()));

        mediaLiveData = PagingLiveData.getLiveData(pager);
    }
}