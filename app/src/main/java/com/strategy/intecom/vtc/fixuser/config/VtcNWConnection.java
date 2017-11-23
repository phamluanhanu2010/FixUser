package com.strategy.intecom.vtc.fixuser.config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by Mr. Ha on 6/2/16.
 */
public class VtcNWConnection extends VtcHttpConnection {

    private ProgressDialog progress;

    public VtcNWConnection(Activity context, RequestListener requestConnection) {
        super(context, requestConnection);
    }

    /**
     * <d>Call when request Api server</d>
     * <d>show dialog process</d>
     */
    private void initShowDialogProcess() {

        initCloseDialogProcess();

        progress = ProgressDialog.show(getContext(),
                getContext().getResources().getString(R.string.title_dialog_message),
                getContext().getResources().getString(R.string.title_dialog_process_confirm), true);
    }

    /**
     * <d>Call when request Api server</d>
     * <d>Dismiss dialog process</d>
     */
    private void initCloseDialogProcess() {

        if (progress != null && progress.isShowing()) {

            progress.dismiss();
            progress = null;
        }
    }

    /**
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
<<<<<<< .mine
     * @param sAPi             link Api connect to server
=======
     * @param sAPi link Api connect to server
     * @param isPost setting method post
     *
>>>>>>> .r1265
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, boolean isPost) {

        onExcuteProcess(actionConnection, sAPi, null, isPost, true);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param sAPi link Api connect to server
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi) {

        onExcuteProcess(actionConnection, sAPi, null, true, true);
    }

    /**
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
<<<<<<< .mine
     * @param sAPi             link Api connect to server
     * @param isShowProcess    = true then show dialog process,
     *                         = false then dismiss dialog process.
=======
     * @param parameter out put
     * @param sAPi link Api connect to server
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, JSONObject parameter) {

        onExcuteProcess(actionConnection, sAPi, parameter, true, true);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param parameter out put
     * @param sAPi link Api connect to server
     * @param isPost setting method post
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, JSONObject parameter, boolean isPost) {

        onExcuteProcess(actionConnection, sAPi, parameter, isPost, true);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param sAPi link Api connect to server
     * @param param out put
     * @param isShowProcess = true then show dialog process,
     *                      = false then dismiss dialog process.
     *
>>>>>>> .r1265
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, JSONObject param, boolean isPost, boolean isShowProcess) {

        VTCModelConnect vtcModelConnect = new VTCModelConnect();
        vtcModelConnect.setActionConnection(actionConnection);
        vtcModelConnect.setAPI(sAPi);
        vtcModelConnect.setParameter(param);
        vtcModelConnect.setPost(isPost);
        vtcModelConnect.setShowProcess(isShowProcess);

        onExcute(vtcModelConnect);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param sAPi link Api connect to server
     * @param lstFile out put list file
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, List<String> lstFile) {

        VTCModelConnect vtcModelConnect = new VTCModelConnect();
        vtcModelConnect.setActionConnection(actionConnection);
        vtcModelConnect.setAPI(sAPi);
        vtcModelConnect.setLstFile(lstFile);

        vtcModelConnect.setShowProcess(false);

        onExcute(vtcModelConnect);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param sAPi link Api connect to server
     * @param file out put list file
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, String file, boolean isShowProcess) {

        VTCModelConnect vtcModelConnect = new VTCModelConnect();
        vtcModelConnect.setActionConnection(actionConnection);
        vtcModelConnect.setAPI(sAPi);
        vtcModelConnect.setFile(file);

        vtcModelConnect.setShowProcess(isShowProcess);

        onExcute(vtcModelConnect);
    }

    private void onExcute(VTCModelConnect vtcModelConnect) {
        ProcessConnection execute = new ProcessConnection(vtcModelConnect);

        if (execute.getStatus() == AsyncTask.Status.RUNNING) {

            setPoolQueue(vtcModelConnect.getAPI(), vtcModelConnect);
        } else {

            boolean isStatusNetWork = AppCore.getGpsTracker(getContext()).isConnection();

<<<<<<< .mine
//                AppCore.showLog("2----Build.VERSION.SDK_INT------- : " + Build.VERSION.SDK_INT);
            execute.execute(sAPi);
//            }
=======
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                execute.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isStatusNetWork);
            } else {

                execute.execute(isStatusNetWork);
            }
>>>>>>> .r1265
        }
    }

    /**
     * <d>Call when wan connect server request API</d>
     * <p>
     * <d>Thread process connect and receiver data from server</d>
     */
    private class ProcessConnection extends AsyncTask<Boolean, String, String> {

        private VTCModelConnect vtcModelConnect;

        public ProcessConnection(VTCModelConnect vtcModelConnect) {
            this.vtcModelConnect = vtcModelConnect;
        }

        private boolean isCheckNull(){
            if(vtcModelConnect == null){
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (isCheckNull() && vtcModelConnect.isShowProcess()) {
                initShowDialogProcess();
            }

<<<<<<< .mine
            AppCore.showLog("---------- : onPreExecute");
=======
>>>>>>> .r1265
        }

        @Override
        protected String doInBackground(Boolean... params) {
            String sData = "";

<<<<<<< .mine
            if (BuildConfig.DEBUG) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
=======
            if (params[0]) {
                if (isCheckNull()) {
>>>>>>> .r1265

                    switch (vtcModelConnect.getActionConnection()) {
                        case TYPE_ACTION_UPLOAD_AVATAR:

                            return uploadFile(vtcModelConnect.getFile(), vtcModelConnect.getAPI());
                        default:
                            String parameter = "";

<<<<<<< .mine
                    AppCore.showLog("---------- : doInBackground" + params[0]);
                    sData = initRequestConnection(params[0]);
=======
                            if (vtcModelConnect.getParameter() != null) {
                                parameter = String.valueOf(vtcModelConnect.getParameter());
                            }

                            return initRequestConnection(vtcModelConnect.getAPI(), parameter, vtcModelConnect.isPost());
                    }
                } else {

                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR);
>>>>>>> .r1265
                }

            } else {
                // No Internet Connection
                setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_NO_INTERNET);
            }

            return sData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AppCore.showLog("-------------------------onPostExecute-----------");

                if (getRequestConnection() != null) {

                    if (getErrorConnection() == TypeErrorConnection.TYPE_CONNECTION && !s.equals("")) {

                        if (ParserJson.getStatusSuccess(s)) {

                            getRequestConnection().onPostExecuteSuccess(vtcModelConnect.getActionConnection(), ParserJson.getResponseData(s), ParserJson.getStatusMsg(s));
                        } else {

                            int errorCode = ParserJson.getErrorCode(s);

                            switch (TypeErrorConnection.forValue(errorCode)){

                                case TYPE_CONNECTION_ERROR_CODE_VERIFY_CODE:

                                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR_CODE_VERIFY_CODE);
                                    break;
                            }

                            getRequestConnection().onPostExecuteError(vtcModelConnect.getActionConnection(), getErrorConnection(), ParserJson.getStatusMsg(s));
                        }

                    } else {

                        getRequestConnection().onPostExecuteError(vtcModelConnect.getActionConnection(), getErrorConnection(), "");
                    }
                }
//            }

//            if (vtcModelConnect.isShowProcess()) {
                initCloseDialogProcess();
//            }

            if (getApiQueueSize() > 0) {

                String sApi = getApiQueue();

                onExcute(getVtcModelConnect(sApi));
            }
        }
    }

    /**
     *
     * <d>Call when wan connect server Socket request API</d>
     *
     */
    public void onExcuteResultSocket() {
        initConnectSocket(AppCore.getPreferenceUtil(getContext()).getValueString(PreferenceUtil.AUTH_TOKEN));
    }

    /**
     *
     * <d>Call when wan connect server Socket request API</d>
     *
     */
    public void onExcuteDisconnectSocket() {
        disConnectSocket();
    }

}
