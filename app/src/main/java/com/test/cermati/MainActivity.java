package com.test.cermati;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.test.cermati.Adapters.ListViewAdapter;
import com.test.cermati.Models.GitHubModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListViewAdapter adapter;
    ArrayList<Map<String, String>> list = new ArrayList<>();

    /* INTERFACE */
    public interface MyCallbackInterface {
        void onCallBack(ResponseBody result) throws IOException, JSONException;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView simpleSearchView = (SearchView) findViewById(R.id.search_view);
        CharSequence query = simpleSearchView.getQuery();
        simpleSearchView.setIconifiedByDefault(false);
        simpleSearchView.setQueryHint("Search Github users");
        boolean isIconfied = simpleSearchView.isIconfiedByDefault();

        ListView listView = (ListView) findViewById(R.id.listview);

        adapter = new ListViewAdapter(this, list);
        listView.setAdapter(adapter);
        simpleSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        Handler mainHandler = new Handler(getMainLooper());
        mainHandler.removeCallbacksAndMessages(null);
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserData(newText);   //Put your call to the server here (with mQueryString)
            }
        }, 500);

        return false;
    }

    private void getUserData(String newText){
        GitHubModel model = new GitHubModel(new MyCallbackInterface() {
            @Override
            public void onCallBack(ResponseBody result) throws IOException, JSONException {
                JSONObject res = new JSONObject(result.string());
                JSONArray Jarray = res.getJSONArray("items");
                list.clear();
                for(int i = 0; i < Jarray.length();++i){
                    JSONObject newRes = Jarray.getJSONObject(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("login",newRes.getString("login"));
                    map.put("avatar_url",newRes.getString("avatar_url"));
                    list.add(map);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }, "https://api.github.com/search/users");

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
}
