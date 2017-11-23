package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMPlaceSlideImage;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

/**
 * Created by Mr. Ha on 6/1/16.
 */
public class AdtPlaceSlidesImage extends FragmentPagerAdapter implements
        IconPagerAdapter {

    private List<String> images;
    private int width = 0;
    private int height = 0;

    public AdtPlaceSlidesImage(FragmentManager fm, List<String> images, int width, int height) {
        super(fm);
        this.images = images;
        this.width = width;
        this.height = height;
    }

    @Override
    public Fragment getItem(int position) {
        return new FMPlaceSlideImage(images.get(position), width, height);
    }

    @Override
    public int getCount() {
        if(images == null){
            return 0;
        }
        return images.size();
    }

    @Override
    public int getIconResId(int index) {
        return index;
    }
}