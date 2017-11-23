package com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class GalleryCache {
    private LruCache<String, Bitmap> mBitmapCache;
    private ArrayList<String> mCurrentTasks;
    private int mMaxWidth;

    public GalleryCache(int size, int maxWidth, int maxHeight) {
        synchronized (this) {
            mMaxWidth = maxWidth;

            mBitmapCache = new LruCache<String, Bitmap>(size) {
                @Override
                protected int sizeOf(String key, Bitmap b) {
                    // Assuming that one pixel contains four bytes.
                    return b.getHeight() * b.getWidth() * 4;
                }
            };

            mCurrentTasks = new ArrayList<String>();
        }
    }

    private synchronized void addBitmapToCache(String key, Bitmap bitmap) {
        synchronized (this) {
            if (getBitmapFromCache(key) == null) {
                mBitmapCache.put(key, bitmap);
            }
        }
    }

    private Bitmap getBitmapFromCache(String key) {
        return mBitmapCache.get(key);
    }

    /**
     * Gets a bitmap from cache. <br/>
     * <br/>
     * If it is not in cache, this method will: <br/>
     * <b>1:</b> check if the bitmap url is currently being processed in the
     * BitmapLoaderTask and cancel if it is already in a task (a control to see
     * if it's inside the currentTasks list). <br/>
     * <b>2:</b> check if an internet connection is available and continue if
     * so. <br/>
     * <b>3:</b> download the bitmap, scale the bitmap if necessary and put it
     * into the memory cache. <br/>
     * <b>4:</b> Remove the bitmap url from the currentTasks list. <br/>
     * <b>5:</b> Notify the ListAdapter.
     *
     * @param mainActivity - Reference to activity object, in order to call
     *                     notifyDataSetChanged() on the ListAdapter.
     * @param imageKey     - The bitmap url (will be the key).
     * @param imageView    - The ImageView that should get an available bitmap or a
     *                     placeholder image.
     * @param isScrolling  - If set to true, we skip executing more tasks since the user
     *                     probably has flinged away the view.
     */
    public synchronized void loadBitmap(Fragment mainActivity, String imageKey,
                                        ImageView imageView, boolean isScrolling) {
        synchronized (this) {
            final Bitmap bitmap = getBitmapFromCache(imageKey);


            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(android.R.drawable.bottom_bar);
                //			imageView.setImageResource(R.drawable.transprent_drawable);
                if (!isScrolling && !mCurrentTasks.contains(imageKey)) {
                    BitmapLoaderTask task = new BitmapLoaderTask(imageKey);
                    task.execute();

                }
            }
        }
    }

    private class BitmapLoaderTask extends AsyncTask<Void, Void, Bitmap> {
        private String mImageKey;

        public BitmapLoaderTask(String imageKey) {
            mImageKey = imageKey;
        }

        @Override
        protected synchronized void onPreExecute() {
            synchronized (this) {
                mCurrentTasks.add(mImageKey);
            }
        }

        @Override
        protected synchronized Bitmap doInBackground(Void... params) {
            synchronized (this) {
                Bitmap bitmap = null;
                try {
                    bitmap = ThumbnailUtils.createVideoThumbnail(mImageKey, MediaStore.Video.Thumbnails.MINI_KIND);

                    if (bitmap != null) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, mMaxWidth, mMaxWidth, false);
                        addBitmapToCache(mImageKey, bitmap);
                        return bitmap;
                    }
                    return null;
                } catch (Exception e) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
        }

        @Override
        protected synchronized void onPostExecute(Bitmap param) {
            synchronized (this) {
                mCurrentTasks.remove(mImageKey);
                if (param != null) {

                }
            }
        }
    }

    public void clear() {
        mBitmapCache.evictAll();
    }
}