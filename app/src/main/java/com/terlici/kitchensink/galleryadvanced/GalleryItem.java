package com.terlici.kitchensink.galleryadvanced;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.Objects;

public class GalleryItem {
    public final long id;
    public final Uri uri;
    //    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
    //    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
    public final int type;
    public final long date;
    public final long duration;

    public GalleryItem(long id, Uri uri, int type, long date, long duration) {
        this.id = id;
        this.uri = uri;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GalleryItem galleryItem = (GalleryItem) obj;
        return id == galleryItem.id && type == galleryItem.type;
    }
}
