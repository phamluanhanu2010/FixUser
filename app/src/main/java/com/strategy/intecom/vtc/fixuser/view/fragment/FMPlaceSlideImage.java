package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

/**
 * Created by Mr. Ha on 6/1/16.
 */
@SuppressLint("ValidFragment")
public final class FMPlaceSlideImage extends Fragment {
    private String img;
    private int width;
    private int height;

    public FMPlaceSlideImage(String img, int width, int height) {
        this.img = img;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.tmp_sliding_image_item, container, false);

        ImageView image = (ImageView) viewRoot.findViewById(R.id.img_avatar);

        ImageLoadAsync loadAsyncAvatar = new ImageLoadAsync(getActivity(), image, width, height);
        loadAsyncAvatar.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, img);

        return viewRoot;
    }
}
