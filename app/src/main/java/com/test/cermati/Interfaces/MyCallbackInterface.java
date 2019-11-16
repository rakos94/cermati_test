package com.test.cermati.Interfaces;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public interface MyCallbackInterface {
    int onCallBack(ResponseBody result) throws IOException, JSONException;
    void onCallBackError(Response response);
    void postCallback(int length);
}
