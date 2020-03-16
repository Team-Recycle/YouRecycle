package com.example.yourecycle.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yourecycle.Adapters.NewsFeedAdapter;
import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.Models.User;
import com.example.yourecycle.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LearningFragment extends Fragment {

    private View view;
    private User user;
    private NewsFeedAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public class LoadNews extends AsyncTask<JSONArray, Void, Void>{

        @Override
        protected Void doInBackground(JSONArray... jsonArrays) {
            requestComplete(jsonArrays[0]);
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_learning, container, false);

        ((Toolbar) view.findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        recyclerView = view.findViewById(R.id.news_list_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateNews();
            }
        });
        populateNews();
        return view;
    }

    private void populateNews() {

        swipeRefreshLayout.setRefreshing(true);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://newsapi.org/v2/everything?q=recycling&sortBy=publishedAt&apiKey=7731fba84a8a40dcbbfd25c8aee61530")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Helper.log("Error: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    if (response.code() == 200){
                        String data = null;
                        try {
                            data = response.body().string();
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray jsonArray = jsonObject.getJSONArray("articles");
                            new LoadNews().execute(jsonArray);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else{
                        Helper.log("Error message: "+response.message());
                    }
                }
            }
        });
    }

    private void requestComplete(final JSONArray jsonArray) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                startAdapter(jsonArray);
            }
        });
    }

    private void startAdapter(JSONArray jsonArray) {
        adapter = new NewsFeedAdapter(jsonArray, null);
        recyclerView.setAdapter(adapter);
    }

    private View findViewById(int id){
        return getView().findViewById(id);
    }
}
