package com.strategy.intecom.vtc.fixuser.view.custom.loadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.utils.UtilsBitmap;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.BitmapTransform;

import java.io.File;

/**
 * Created by Mr. Ha on 5/26/16.
 */


public class ImageLoadAsync extends AsyncTask<String, String, String> {

    private ImageView mImageView;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private boolean isRound = Boolean.FALSE;

    public ImageLoadAsync(Context context, ImageView imageView, int width) {
        mImageView = imageView;
        mContext = context;
        mWidth = width;
        mHeight = width;
        this.isRound = Boolean.FALSE;
    }

    public ImageLoadAsync(Context context, ImageView imageView, int width, boolean isRound) {
        mImageView = imageView;
        mContext = context;
        mWidth = width;
        mHeight = width;
        this.isRound = isRound;
    }
    public ImageLoadAsync(Context context, ImageView imageView, int width, int height) {
        mImageView = imageView;
        mContext = context;
        mWidth = width;
        mHeight = height;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        if(params != null && params.length > 0){
            return String.valueOf(params[0]);
        }
            return  "";
    }

    @Override
    protected void onPostExecute(String result) {

        if(initCheckLink(result)){
            if(isRound){
                Glide.with(mContext)
                        .load(result)
                        .asBitmap()
                        .centerCrop()
                        .error(R.drawable.ic_avatar_default)
                        .into(new BitmapImageViewTarget(mImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mImageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }else {
                Glide.with(mContext)
                        .load(result)
//                .animate(android.R.anim.slide_in_left)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new BitmapTransform(mContext, mWidth, mHeight))
                        .override(mWidth, mHeight)
                        .centerCrop()
                        .error(R.drawable.ic_avatar_default)
//                .placeholder(android.R.drawable.bottom_bar)
                        .into(mImageView);
            }
        }else {
            if(isRound){
                Glide.with(mContext)
                        .load(new File(result))
                        .asBitmap()
                        .centerCrop()
                        .error(R.drawable.ic_avatar_default)
                        .into(new BitmapImageViewTarget(mImageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                mImageView.setImageDrawable(circularBitmapDrawable);

                                mImageView.setImageBitmap(UtilsBitmap.getRoundedCornerImage(resource, 100));
                            }
                        });
            }else {
                Glide.with(mContext)
                        .load(new File(result))
//                .animate(android.R.anim.slide_in_left)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new BitmapTransform(mContext, mWidth, mHeight))
                        .override(mWidth, mHeight)
                        .centerCrop()
                        .error(R.drawable.ic_avatar_default)
//                .placeholder(android.R.drawable.bottom_bar)
                        .into(mImageView);
            }
        }

    }

    private boolean initCheckLink(String link){
        if(link.startsWith("http") || link.startsWith("https"))
        {
            return true;
        }

        return false;
    }

}

