package com.test.cermati.Models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.test.cermati.Interfaces.MyCallbackInterface;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GitHubModel {

    private final OkHttpClient client = new OkHttpClient();
    private String url;
    private MyCallbackInterface callback;

    public GitHubModel(MyCallbackInterface callback, String url) {
        this.url = url;
        this.callback = callback;
    }

    public void run(String q) throws Exception {

        HttpUrl.Builder httpBuider = HttpUrl.parse(this.url).newBuilder();
        httpBuider.addQueryParameter("q",q);

        Request request = new Request.Builder()
                .url(httpBuider.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    int length = 0;
                    if (!response.isSuccessful()) {
                        callback.onCallBackError(response);
                    } else {
                        try {
                            length = callback.onCallBack(responseBody);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    callback.postCallback(length);
                }
            }
        });
    }


}
