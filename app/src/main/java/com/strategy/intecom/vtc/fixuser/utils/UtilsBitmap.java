package com.strategy.intecom.vtc.fixuser.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Mr. Ha on 7/12/16.
 */
public class UtilsBitmap {

    public static Bitmap resizeImage(Context context, String inFile,
                                   String outFile, int destHeight, int destWidth) {
        if (inFile == null) {
            return null;
        }
        if (outFile == null) {
            return null;
        }

        try {
            int inWidth = 0;
            int inHeight = 0;

            InputStream in = new FileInputStream(inFile);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;
            // byte per pixel = 4
            int sizeOfFile = inWidth * inHeight * 4;
            // 50M = 52428800
            if (sizeOfFile < 0) { // size of file is exceed 50M
                if (getAvailableMemory(context) - 20480 < 52428800) {
                    AppCore.showLog("Word Search", "Out of memory");
                    return null;
                }
            } else if (sizeOfFile > getAvailableMemory(context) - 20480) {
                AppCore.showLog("Word Search", "Out of memory");
                return null;
            }

            // decode full image pre-resized
            in = new FileInputStream(inFile);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / destWidth, inHeight
                    / destHeight);
            options.inPurgeable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            try {
                // decode full image
                Bitmap roughBitmap = BitmapFactory.decodeStream(in, null,
                        options);

                // calc exact destination size
                Matrix m = new Matrix();
                RectF inRect = new RectF(0, 0, roughBitmap.getWidth(),
                        roughBitmap.getHeight());
                RectF outRect = new RectF(0, 0, destWidth, destHeight);
                m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
                float[] values = new float[9];
                m.getValues(values);

                // resize bitmap
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
                        (int) (roughBitmap.getWidth() * values[0]),
                        (int) (roughBitmap.getHeight() * values[4]), true);

                in.close();

                // save image
                FileOutputStream out = new FileOutputStream(outFile);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.close();
                AppCore.showLog("Image Done");
                return resizedBitmap;
            } catch (Exception e) {
                AppCore.showLog("Image" + e.getMessage());
            } catch (OutOfMemoryError e) {
                AppCore.showLog("Image" + e.getMessage());
            }
        } catch (IOException e) {
            AppCore.showLog("Image" + e.getMessage());
        } catch (OutOfMemoryError e) {
            AppCore.showLog("Image" + e.getMessage());
        }
        return null;
    }


    public static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem;
    }

    /**
     * Get file path from URI
     *
     * @param context context of Activity
     * @param uri     uri of file
     * @return path of given URI
     */
    public static String getRealPathFromURI(final Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isThanFilesSize500K(List<String> lst) {
        if(lst == null || lst.size() <=0){
            return true;
        }
        long total = 0l;
        for (int i = 0; i < lst.size(); ++i) {
            File file = new File(lst.get(i));
            long kb = (file.length() / 1024);
            total += kb;
        }

        return total <= 500;
    }

    public static String convertImage(Bitmap bm) throws OutOfMemoryError {
        if(bm == null){
            return "";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static String convertImage(Bitmap bm, int quality)
            throws OutOfMemoryError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static float convertDpToPixels(float dp,Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi/160f);
        return px;
    }

    public static float convertPixelsToDp(float px,Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static Bitmap getRoundedCornerImage(Bitmap bitmap, int round) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = round;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        int saveCount = canvas.saveLayer(rectF, null, Canvas.MATRIX_SAVE_FLAG
                | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.restoreToCount(saveCount);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static File isFileOnList(String sPath, String sFileName) {

        AppCore.showLog("--------sPath------ : " + sPath);
        File f = new File(sPath);
        for (File temp : f.listFiles()) {
            if (temp.getName().equals(sFileName)) {
                return temp;
            }
        }
        return f;
    }

    public static BitmapFactory.Options getBitmapOption() {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inDither = false;
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmapOptions.inSampleSize = 1;
        bitmapOptions.inPurgeable = true;
        bitmapOptions.inPreferQualityOverSpeed = true;
        bitmapOptions.inTempStorage=new byte[32 * 1024];
        return bitmapOptions;
    }
}
