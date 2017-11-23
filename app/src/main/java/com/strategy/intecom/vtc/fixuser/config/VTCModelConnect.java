package com.strategy.intecom.vtc.fixuser.config;

import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Mr. Sup on 6/20/16.
 */
public class VTCModelConnect {
    private TypeActionConnection actionConnection;
    private String API;
    private boolean isPost;
    private boolean isShowProcess;
    private JSONObject parameter;
    private List<String> lstFile;
    private String file;

    public TypeActionConnection getActionConnection() {
        return actionConnection;
    }

    public void setActionConnection(TypeActionConnection actionConnection) {
        this.actionConnection = actionConnection;
    }

    public String getAPI() {
        return API;
    }

    public void setAPI(String API) {
        this.API = API;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public boolean isShowProcess() {
        return isShowProcess;
    }

    public void setShowProcess(boolean showProcess) {
        isShowProcess = showProcess;
    }

    public JSONObject getParameter() {
        return parameter;
    }

    public void setParameter(JSONObject parameter) {
        this.parameter = parameter;
    }

    public List<String> getLstFile() {
        return lstFile;
    }

    public void setLstFile(List<String> lstFile) {
        this.lstFile = lstFile;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
