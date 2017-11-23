package com.strategy.intecom.vtc.fixuser.config;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.strategy.intecom.vtc.fixuser.BuildConfig;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixuser.utils.FileUploader;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreBase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Mr. Ha on 6/2/16.
 */
public class VtcHttpConnection {

    private static String URL_CONNECT_SERVER = "";
    private static String URL_CONNECT_SERVER_SOCKET = "";

    private static final String URL_CONNECT_SERVER_DEBUG = "http://117.103.207.42";

    private static final String URL_CONNECT_SERVER_RELEASE = "https://api.suado.vn";
    private static final String URL_CONNECT_SOCKET_RELEASE = "https://ws.suado.vn";


    private final int CONNECT_TIME_OUT = 1000 * 30;

    private Activity context;
    private RequestListener requestConnection;

    private TypeErrorConnection errorConnection = TypeErrorConnection.TYPE_CONNECTION;

    private BlockingQueue<String> pollQueue = new LinkedBlockingQueue<>();
    private Map<String, VTCModelConnect> mapQueue = new HashMap<>();

    private static Socket socket;

    protected VTCModelConnect getVtcModelConnect(String sAPI) {

        if (mapQueue != null) {
            return mapQueue.get(sAPI);
        }
        return null;
    }

    protected String getApiQueue() {

        if (pollQueue != null) {
            return pollQueue.poll();
        }
        return "";
    }

    protected int getApiQueueSize() {

        if (pollQueue != null) {
            return pollQueue.size();
        }
        return 0;
    }

    protected void setPoolQueue(String sAPI, VTCModelConnect vtcModelConnect) {

        if (pollQueue != null) {
            pollQueue.add(sAPI);
        }

        if (mapQueue != null) {
            mapQueue.put(sAPI, vtcModelConnect);
        }
    }

    protected Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        if (context != null) {
            this.context = context;
        }
    }

    protected RequestListener getRequestConnection() {
        return requestConnection;
    }

    protected TypeErrorConnection getErrorConnection() {
        return errorConnection;
    }

    protected void setErrorConnection(TypeErrorConnection errorConnection) {
        this.errorConnection = errorConnection;
    }

    protected VtcHttpConnection(Activity context, RequestListener requestConnection) {
        this.context = context;
        this.requestConnection = requestConnection;

//        if (BuildConfig.DEBUG) {
//            VtcHttpConnection.URL_CONNECT_SERVER = VtcHttpConnection.URL_CONNECT_SERVER_DEBUG + ":8888";
//            VtcHttpConnection.URL_CONNECT_SERVER_SOCKET = VtcHttpConnection.URL_CONNECT_SERVER_DEBUG + ":3000";
//        } else {
            VtcHttpConnection.URL_CONNECT_SERVER = VtcHttpConnection.URL_CONNECT_SERVER_RELEASE;
            VtcHttpConnection.URL_CONNECT_SERVER_SOCKET = VtcHttpConnection.URL_CONNECT_SOCKET_RELEASE;
//        }
    }

    protected String initRequestConnection(String sApi, String urlParameters, boolean post_get) {
        try {

            Charset charset = Charset.forName("UTF-8");
            URL url = new URL(VtcHttpConnection.URL_CONNECT_SERVER + sApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setRequestProperty("charset", "utf-8");
            connection.setConnectTimeout(CONNECT_TIME_OUT);

            if (post_get) {
                connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
                connection.setRequestMethod(String.valueOf("POST"));
                connection.setDoOutput(true);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters);
                bw.flush();
                bw.close();
            } else {
                connection.setRequestMethod(String.valueOf("GET"));
            }

            int responseCode = connection.getResponseCode();

            AppCore.showLog("----responseCode----- : " + responseCode + " ------ : " + connection.getRequestMethod());

            switch (responseCode) {

                case HttpURLConnection.HTTP_OK:
                    final StringBuilder output = new StringBuilder("Request URL " + url);
                    output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                    output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                    output.append(System.getProperty("line.separator") + "Type " + connection.getRequestMethod());
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
                    String line = "";
                    StringBuilder responseOutput = new StringBuilder();
                    AppCore.showLog("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();

                    output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                    AppCore.showLog("------ : " + output.toString());

                    connection.disconnect();
                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION); // fine, go on
                    return responseOutput.toString();
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:

                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_TIMEOUT); // retry

                    connection.disconnect();
                    return "";
                case HttpURLConnection.HTTP_UNAVAILABLE:

                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_NOT_CONNECT_SERVER); // retry, server is unstable

                    connection.disconnect();
                    return "";

                default:

                    connection.disconnect();
                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR); // abort
                    return "";
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR);
        }
        return "";
    }

    public static String readUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            AppCore.showLog("Exception while reading url : " + e.toString());
        } finally {
            if (iStream != null) {
                iStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }

    public static byte[] urlParameterJson(Map<?, ?> map) {
        JSONObject jsonObject = new JSONObject();
        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    public static String urlEncodeUTF8(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        try {
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry != null) {
                        if (sb.length() > 0) {
                            sb.append("&");
                        } else {
                            sb.append("?");
                        }

                        String value = "";

                        String key = "";

                        if (entry.getValue() != null) {
                            value = String.valueOf(entry.getValue());
                        }
                        if (entry.getKey() != null) {
                            key = String.valueOf(entry.getKey());
                        }

                        String encodeValue = "";
                        String encodeKey = "";

                        if (null != value || !value.isEmpty()) {
                            encodeValue = urlEncodeUTF8(value);
                        }
                        if (null != key || !key.isEmpty()) {
                            encodeKey = urlEncodeUTF8(key);
                        }

                        sb.append(String.format("%s=%s", encodeKey, encodeValue));
                    }
                }
            }
        } catch (NumberFormatException e) {

        }
        return sb.toString();
    }

    private static String urlEncodeUTF8(String s) {
        try {
            if (s != null && !s.isEmpty()) {
                s = URLEncoder.encode(s, "UTF-8");
            } else {
                s = "";
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String uploadFile(String imgPath, String sAPI) {
        final File uploadFile = new File(imgPath);
        try {
            final MultipartUtility http = new MultipartUtility(getContext(), getRequestConnection(), new URL(URL_CONNECT_SERVER + sAPI));
            http.addFormField("fileName", uploadFile.getName());
            http.addFormField("mimeType", "image/jpeg");
            http.addFilePart("file", uploadFile);
           return http.finish();
        } catch (IOException e) {
            e.printStackTrace();
            AppCore.showLog("-----uploadFile------------ : " + e.getMessage());
        }

        return "";
    }

    protected void initConnectSocket(String auth) {
        try {

            disConnectSocket();

            AppCore.showLog("-----initConnectSocket------------- : ");
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.query = ParserJson.API_PARAMETER_TOKEN + "=" + auth;
            socket = IO.socket(VtcHttpConnection.URL_CONNECT_SERVER_SOCKET, opts);
            socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    if (args != null && args.length > 0) {
                        AppCore.showLog("------Socket connected--------------");
                    }
                }

            }).on(RequestListener.SOCKET_EVENT_ACCEPT_RESULT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    if (getRequestConnection() != null && args != null && args.length > 0) {
                        AppCore.showLog("---------Socket accept result----------- : " + String.valueOf(args[0]));
                        getRequestConnection().onResultSocket(TypeActionConnection.TYPE_ACTION_SOCKET_ACCEPT_RESULT, socket, String.valueOf(args[0]));
                    }
                }

            }).on(RequestListener.SOCKET_EVENT_RECEIVE_LIST_AGENCY, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    if (getRequestConnection() != null && args != null && args.length > 0) {
                        AppCore.showLog("---------Socket receive list agency----------- : " + String.valueOf(args[0]));
                        getRequestConnection().onResultSocket(TypeActionConnection.TYPE_ACTION_SOCKET_LIST_AGENCY, socket, String.valueOf(args[0]));
                    }
                }

            }).on(RequestListener.SOCKET_EVENT_REQUEST_LOCATION, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    if (getRequestConnection() != null && args != null && args.length > 0) {
                        AppCore.showLog("---------Socket request location----------- : " + String.valueOf(args[0]));
                        getRequestConnection().onResultSocket(TypeActionConnection.TYPE_ACTION_SOCKET_RECEIVE_LOCATION, socket, String.valueOf(args[0]));
                    }
                }

            }).on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    if (args != null && args.length > 0) {
                        AppCore.showLog("-----Socket disconnected---------------");
                    }
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        if(isConnectSocket()){
            return socket;
        }else {
            initConnectSocket(AppCore.getPreferenceUtil(getContext()).getValueString(PreferenceUtil.AUTH_TOKEN));
            return socket;
        }
    }

    protected void disConnectSocket(){
        if(isConnectSocket()){
            socket.disconnect();
            socket = null;
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private boolean isConnectSocket(){
        if(socket != null){
            return socket.connected();
        }
        return false;
    }
}