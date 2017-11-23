package com.strategy.intecom.vtc.fixuser.utils;

import android.util.Log;

import com.strategy.intecom.vtc.fixuser.BuildConfig;

/**
 * Created by lugen on 9/6/16.
 */
public class Logger {
    private static final int LEVEL_VERBOVE = 0;
    private static final int LEVEL_INFO = 1;
    private static final int LEVEL_DEBUG = 2;
    private static final int LEVEL_WARNING = 3;
    private static final int LEVEL_ERROR = 4;

    private static final int LEVEL = LEVEL_VERBOVE;


    public static void v(String tag, Object object, String message) {
        if ((LEVEL <= LEVEL_VERBOVE) && BuildConfig.DEBUG) {
            Log.v(tag, "[" + ((object != null) ? object.getClass().getSimpleName() : "") + "] " + message);
        }
    }

    public static void i(String tag, Object object, String message) {
        if ((LEVEL <= LEVEL_INFO) && BuildConfig.DEBUG) {
            Log.i(tag, "[" + ((object != null) ? object.getClass().getSimpleName() : "") + "] " + message);
        }
    }

    public static void d(String tag, Object object, String message) {
        if ((LEVEL <= LEVEL_DEBUG) && BuildConfig.DEBUG) {
            Log.d(tag, "[" + ((object != null) ? object.getClass().getSimpleName() : "") + "] " + message);
        }
    }

    public static void w(String tag, Object object, String message) {
        if ((LEVEL <= LEVEL_WARNING) && BuildConfig.DEBUG) {
            Log.w(tag, "[" + ((object != null) ? object.getClass().getSimpleName() : "") + "] " + message);
        }
    }

    public static void e(String tag, Object object, String message) {
        if ((LEVEL <= LEVEL_ERROR) && BuildConfig.DEBUG) {
            Log.e(tag, "[" + ((object != null) ? object.getClass().getSimpleName() : "") + "] " + message);
        }
    }

    private Logger() {
    }


//    private static final String XOET = "|";
//
//    public static void initCreateNewFile(Activity context){
//
//        try {
//
//            String sPathFull = initGetPathName(context);
//
//            AppCore.showLog("initCreateNewFile--------------------- : " + sPathFull);
//
//            File file = new File(sPathFull);
//
//            if(!file.exists()){
//                if(file.createNewFile()){
//                    AppCore.showLog("initCreateNewFile--------------------- : is File Create Ok");
//                }else {
//                    AppCore.showLog("initCreateNewFile--------------------- : is File Create Not Ok");
//                }
//
//            }else {
//                AppCore.showLog("initCreateNewFile--------------------- : is File Exists");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            AppCore.showLog("initCreateNewFile--------------------- : " + e.getMessage());
//        }
//    }
//
//    public static void generateLoggerOnFile(Context context, String sContent){
//        AppCore.showLog("-sContent----------------------- : " + sContent);
//
//        try {
//
//            String sPathFull = initGetPathName(context);
//
//
//            AppCore.showLog("generateLoggerOnFile--------------------- : " + sPathFull);
//
//            File file = new File(sPathFull);
//
//            if(!file.exists()){
//                if(file.createNewFile()){
//                    AppCore.showLog("generateLoggerOnFile--------------------- : is File Create Ok");
//                }else {
//                    AppCore.showLog("generateLoggerOnFile--------------------- : is File Exists");
//                }
//            }else {
//                AppCore.showLog("generateLoggerOnFile--------------------- : is File Exists");
//            }
//
//            FileWriter fw = new FileWriter(sPathFull, true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write(sContent);
//            bw.newLine();
//            bw.close();
//            fw.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            AppCore.showLog("generateLoggerOnFile--------------------- : " + e.getMessage());
//        }
//    }
//
//    public static String readFromFile(String path) {
//
//        File dir = new File(path);
//
//        String ret = "File not found";
//        if(dir.exists()) {
//            try {
//                InputStream inputStream = new FileInputStream(dir);
//                if (inputStream != null) {
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                    String receiveString = "";
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    while ((receiveString = bufferedReader.readLine()) != null) {
//                        stringBuilder.append(receiveString);
//                        stringBuilder.append("\n");
//                    }
//
//                    inputStream.close();
//                    ret = stringBuilder.toString();
//                }
//            } catch (FileNotFoundException e) {
//            } catch (IOException e) {
//            }
//        }
//
//        return ret;
//    }
//
//    public static void deleteFile(String path) {
//        File dir = new File(path);
////        if (dir.isDirectory()) {
////            String[] children = dir.list();
////            for (int i = 0; i < children.length; i++) {
////                new File(dir, children[i]).delete();
////            }
////        }
//        if (dir.exists()) {
//            dir.delete();
//            AppCore.showLog( "Delete File Ok");
//        }
//
//    }
//
//    private static String initGetFileName(Context context){
//
//        return  "SB_android_HoangHa.txt";
//    }
//
//    public static String initGetPathName(Context context){
//
//        if(context == null)
//            context = AppCore.getCurrentActivity();
//
////        String sPath = context.getFilesDir().getParent();
//
//        String sPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//
////        File sPath = Environment.getExternalStorageDirectory();
//
//        File root = new File(sPath, "files");
//        if (!root.exists()) {
//            root.mkdirs();
//        }
//
//        String sPathFull = root.getAbsolutePath() + File.separator + initGetFileName(context);
//
//        return sPathFull;
//    }
}
