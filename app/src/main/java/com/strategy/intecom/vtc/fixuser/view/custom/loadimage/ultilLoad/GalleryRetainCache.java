package com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad;

/**
 * Created by Mr. Ha on 5/26/16.
 */
public class GalleryRetainCache {
    private static GalleryRetainCache sSingleton;
    public GalleryCache mRetainedCache;

    public synchronized static GalleryRetainCache getOrCreateRetainableCache() {
        if (sSingleton == null) {
            sSingleton = new GalleryRetainCache();
        }
        return sSingleton;
    }

}
