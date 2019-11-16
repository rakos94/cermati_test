package com.test.cermati;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.test.cermati.Adapters.ListViewAdapter;
import com.test.cermati.Interfaces.MyCallbackInterface;
import com.test.cermati.Models.GitHubModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MyCallbackInterface {

    SearchView simpleSearchView;
    ListViewAdapter adapter;
    ArrayList<Map<String, String>> list = new ArrayList<>();
    RelativeLayout progressBarlayout, emptyResultSection;
    ListView listView;
    GitHubModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleSearchView = (SearchView) findViewById(R.id.search_view);
        CharSequence query = simpleSearchView.getQuery();
        simpleSearchView.setIconifiedByDefault(false);
        simpleSearchView.setQueryHint("Search Github users");
        boolean isIconfied = simpleSearchView.isIconfiedByDefault();

        listView = findViewById(R.id.listview);
        progressBarlayout = findViewById(R.id.progressBarlayout);
        emptyResultSection = findViewById(R.id.emptyResultSection);

        adapter = new ListViewAdapter(this, list);
        listView.setAdapter(adapter);
        simpleSearchView.setOnQueryTextListener(this);

        model = new GitHubModel(this, "https://api.github.com/search/users");
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        simpleSearchView.clearFocus();
        progressBarlayout.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        emptyResultSection.setVisibility(View.GONE);
        getUserData(newText);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void getUserData(String newText){
        if(!newText.isEmpty()) {
            try {
                model.run(newText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int onCallBack(ResponseBody result) throws IOException, JSONException {
        JSONObject res = new JSONObject(result.string());
        final JSONArray Jarray = res.getJSONArray("items");
        list.clear();
        for(int i = 0; i < Jarray.length();++i){
            JSONObject newRes = Jarray.getJSONObject(i);
            Map map = new Gson().fromJson(newRes.toString(), HashMap.class);
            list.add(map);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        return Jarray.length();
    }

    @Override
    public void onCallBackError(Response response) {
        View contextView = findViewById(R.id.main_layout);
        Snackbar.make(contextView, R.string.api_limit, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void postCallback(final int length) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBarlayout.setVisibility(View.GONE);
                if(length > 0) {
                    listView.setVisibility(View.VISIBLE);
                    emptyResultSection.setVisibility(View.GONE);
                } else {
                    emptyResultSection.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }
        });
    }
}
