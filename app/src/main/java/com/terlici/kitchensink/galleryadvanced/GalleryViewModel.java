package com.terlici.kitchensink.galleryadvanced;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import java.util.ArrayList;

public class GalleryViewModel extends AndroidViewModel {

    private static final int PAGE_SIZE = 128;
    public final LiveData<PagingData<GalleryItem>> mediaLiveData;

    public final MutableLiveData<ArrayList<GalleryItem>> selectionLiveData = new MutableLiveData<>(new ArrayList<>());

    public GalleryViewModel(@NonNull Application application) {
        super(application);

        Pager<Long, GalleryItem> pager = new Pager<>(new PagingConfig(PAGE_SIZE), () ->
                new GalleryPagingSource(getApplication().getContentResolver()));

        mediaLiveData = PagingLiveData.getLiveData(pager);
    }

    public void select(GalleryItem item) {
        ArrayList<GalleryItem> selection = selectionLiveData.getValue();

        if (!selection.contains(item)) {
            selection.add(item);
        }

        selectionLiveData.postValue(selection);
    }

    public void deselect(GalleryItem item) {
        ArrayList<GalleryItem> selection = selectionLiveData.getValue();
        selection.remove(item);
        selectionLiveData.postValue(selection);
    }

    public boolean isSelected(GalleryItem item) {
        ArrayList<GalleryItem> selection = selectionLiveData.getValue();
        return selection.contains(item);
    }

    public int selectionIndex(GalleryItem item) {
        ArrayList<GalleryItem> selection = selectionLiveData.getValue();
        return selection.indexOf(item);
    }
}