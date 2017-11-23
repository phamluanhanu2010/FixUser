package com.strategy.intecom.vtc.fixuser.view.custom.loadimage;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.GalleryCache;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.GalleryRetainCache;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class VideoLoadAsync extends MediaAsync<String,String, String> {

    public Fragment fragment;

    private ImageView mImageView;
    private static GalleryCache mCache;
    private boolean mIsScrolling;
    private int mWidth;
    private int mHeight;


    public VideoLoadAsync(Fragment fragment, ImageView imageView, boolean isScrolling, int width) {
        synchronized (this) {
            mImageView = imageView;
            this.fragment = fragment;
            mWidth = width;
            mHeight = width;
            mIsScrolling = isScrolling;

            initDate();
        }
    }

    public VideoLoadAsync(Fragment fragment, ImageView imageView, boolean isScrolling, int width, int height) {
        synchronized (this) {
            mImageView = imageView;
            this.fragment = fragment;
            mWidth = width;
            mHeight = height;
            mIsScrolling = isScrolling;

            initDate();
        }
    }

    private synchronized void initDate() {
        synchronized (this) {
            final int memClass = ((ActivityManager) fragment.getActivity().getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            final int size = 1024 * 1024 * memClass / 8;

            // Handle orientation change.
            GalleryRetainCache c = GalleryRetainCache.getOrCreateRetainableCache();
            mCache = c.mRetainedCache;

            if (mCache == null) {
                // The maximum bitmap pixels allowed in respective direction.
                // If exceeding, the cache will automatically scale the
                // bitmaps.
			/*	final int MAX_PIXELS_WIDTH  = 100;
			final int MAX_PIXELS_HEIGHT = 100;*/
                mCache = new GalleryCache(size, mWidth, mHeight);
                c.mRetainedCache = mCache;
            }
        }
    }

    @Override
    protected synchronized String doInBackground(String... params) {
        synchronized (this) {
            String url = params[0].toString();
            return url;
        }
    }

    @Override
    protected synchronized void onPostExecute(String result) {
        synchronized (this) {
            mCache.loadBitmap(fragment, result, mImageView, mIsScrolling);
        }
    }
}