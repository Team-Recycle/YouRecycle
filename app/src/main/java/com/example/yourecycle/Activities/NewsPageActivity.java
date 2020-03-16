package com.example.yourecycle.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yourecycle.Helpers.Helper;
import com.example.yourecycle.R;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_page);

        ((Toolbar) findViewById(R.id.toolbar))
                .setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        try {
            final JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("data"));

            findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        shareTextUrl(jsonObject.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.read_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(jsonObject.getString("url")));
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Glide.with(getApplicationContext())
                    .load(Uri.parse(jsonObject.getString("urlToImage")))
                    .into((ImageView) findViewById(R.id.image));

            ((TextView) findViewById(R.id.title)).setText(jsonObject.getString("title"));
            String content = jsonObject.getString("content");
            int index = content.lastIndexOf("[+");
            Helper.log(content);
            if (index != -1)
                content = content.substring(0, index);
            ((TextView) findViewById(R.id.content)).setText(content);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void shareTextUrl(String link) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, "Link To News");
        share.putExtra(Intent.EXTRA_TEXT, link);

        startActivity(Intent.createChooser(share, "Share link!"));
    }
}
