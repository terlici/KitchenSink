package com.terlici.kitchensink.galleryadvanced;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import java.util.ArrayList;
import java.util.List;

import kotlin.coroutines.Continuation;

public class GalleryPagingSource extends PagingSource<Long, GalleryItem> {

    private static final String SELECTION = MediaStore.Files.FileColumns.MEDIA_TYPE + " IN ('" +
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + "', '" +
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO + "') AND " +
            MediaStore.Files.FileColumns.MIME_TYPE + " NOT IN ('image/svg+xml')";
    private static final String[] PROJECTION = new String[] {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DURATION,
    };

    final private ContentResolver contentResolver;

    public GalleryPagingSource(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Nullable
    @Override
    public Long getRefreshKey(@NonNull PagingState<Long, GalleryItem> state) {
        Integer position = state.getAnchorPosition();
        if (position == null) {
            return null;
        }

        GalleryItem item = state.closestItemToPosition(position);
        if (item == null) {
            return null;
        }

        return item.id;
    }

    @Nullable
    @Override
    public Object load(@NonNull LoadParams<Long> params, @NonNull Continuation<? super LoadResult<Long, GalleryItem>> continuation) {
        List<GalleryItem> items = new ArrayList<>();
        Long prevKey = params.getKey();

        try (final Cursor cursor = getCursor(prevKey, params.getLoadSize())) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(0);
                    int type = cursor.getInt(1);
                    long duration = type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO ? cursor.getLong(3) : 0;
                    long date = cursor.getLong(2);

                    Uri uri = ContentUris.withAppendedId(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id);

                    items.add(new GalleryItem(id, uri, type, date, duration));
                }
            }
        } catch (SecurityException e) {
            Log.w("GalleryPagingSource", e);
            return new LoadResult.Error<Long, GalleryItem>(e);
        }

        Long nextKey = items.size() > 0 ? items.get(items.size() - 1).id : null;
        return new LoadResult.Page<>(items, prevKey, nextKey);
    }

    private Cursor getCursor(Long start, int size) {
        String selection = SELECTION + (start == null ? "" : (" AND _id < " + start));
        Uri uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);

        if (Build.VERSION.SDK_INT >= 30) {
            // LIMIT works differently since API 30
            Bundle queryArgs = new Bundle();
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection);
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, "_id DESC");
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, Integer.toString(size));

            return contentResolver.query(uri, PROJECTION, queryArgs, null);
        } else {
            return contentResolver.query(uri, PROJECTION, selection, null, "_id DESC LIMIT " + size, null);
        }
    }
}
