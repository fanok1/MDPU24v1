package com.fanok.mdpu24v1.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fanok.mdpu24v1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Objects;

public class ScreenSlidePageFragment extends android.support.v4.app.Fragment {
    private static final String PIC_URL = "screenslidepagefragment.picurl";

    public static ScreenSlidePageFragment newInstance(String picUrl) {
        Bundle arguments = new Bundle();
        arguments.putString(PIC_URL, picUrl);
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ImageView imageView = rootView.findViewById(R.id.image);
        final ProgressBar progressBar = rootView.findViewById(R.id.progress);//
        Bundle arguments = getArguments();
        String url = Objects.requireNonNull(arguments).getString(PIC_URL);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(Objects.requireNonNull(getActivity())));
        ImageLoader.getInstance()
                .displayImage(url, imageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar.setProgress(2);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                    }
                }, (imageUri, view, current, total) -> progressBar.setProgress(Math.round(100.0f * current / total)));
        return rootView;
    }
}
