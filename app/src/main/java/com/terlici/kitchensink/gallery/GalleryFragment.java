package com.terlici.kitchensink.gallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.terlici.kitchensink.R;
import com.terlici.kitchensink.databinding.FragmentGalleryBinding;
import com.terlici.kitchensink.databinding.GalleryItemBinding;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private final ActivityResultLauncher<String[]> requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
        boolean allGranted = true;
        for (Boolean permissionGranted : isGranted.values()) {
            allGranted = allGranted & permissionGranted;
        }

        if (allGranted) {
            load();
        } else {
            notifyMissingPermissions();
        }
    });

    private FragmentGalleryBinding binding;
    private GalleryViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding.gallery.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        binding.gallery.addItemDecoration(new GalleryItemSpacingDecoration(10, 4));

        requestPermissions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestPermissions() {
        ArrayList<String> missingPermissions = new ArrayList<>(4);

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.READ_MEDIA_IMAGES);
            }

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.READ_MEDIA_VIDEO);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        if (missingPermissions.size() > 0) {
            requestPermissionsLauncher.launch(missingPermissions.toArray(new String[0]));
        } else {
            load();
        }
    }

    private void notifyMissingPermissions() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.gallery_missing_permissions_title)
                .setMessage(R.string.gallery_missing_permissions_message)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                })
                .show();
    }

    private void load() {
        GalleryAdapter adapter = new GalleryAdapter((item, position) -> {
            Snackbar.make(binding.gallery, "Clicked on item " + position, Snackbar.LENGTH_LONG)
                    .setAnchorView(binding.gallery)
                    .setAction("Action", null).show();
        });
        binding.gallery.setAdapter(adapter);

        viewModel.mediaLiveData.observe(getViewLifecycleOwner(), data ->
            adapter.submitData(getLifecycle(), data)
        );
    }

    private interface GalleryOnClickListener {
        void onClick(@NonNull GalleryItem item, int position);
    }

    private static class GalleryViewHolder extends RecyclerView.ViewHolder {
        private final GalleryItemBinding binding;

        public GalleryViewHolder(@NonNull GalleryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull GalleryItem item, @NonNull View.OnClickListener onClickListener) {
            binding.thumbnail.setOnClickListener(onClickListener);
            Glide.with(itemView)
                    .load(item.uri)
                    .into(binding.thumbnail);
        }
    }

    private static class GalleryAdapter extends PagingDataAdapter<GalleryItem, GalleryViewHolder> {
        private final GalleryOnClickListener onClickListener;

        public GalleryAdapter(@NonNull GalleryOnClickListener onClickListener) {
            super(new DiffUtil.ItemCallback<GalleryItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull GalleryItem oldItem, @NonNull GalleryItem newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull GalleryItem oldItem, @NonNull GalleryItem newItem) {
                    return oldItem.equals(newItem);
                }
            });

            this.onClickListener = onClickListener;
        }

        @NonNull
        @Override
        public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GalleryItemBinding binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new GalleryViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
            GalleryItem item = getItem(position);

            if (item != null) {
                holder.bind(item, (v) -> onClickListener.onClick(item, position));
            }
        }
    }

    public static class GalleryItemSpacingDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;
        private final int columnsCount;

        public GalleryItemSpacingDecoration(int spacing, int columnsCount) {
            this.spacing = spacing;
            this.columnsCount = columnsCount;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % columnsCount;

            outRect.bottom = spacing;
            outRect.left = column * spacing / columnsCount;
            outRect.right = spacing - (column + 1) * spacing / columnsCount;
        }
    }
}