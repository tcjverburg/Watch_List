package com.example.gebruiker.watchlist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Gebruiker on 23-9-2016.
 */
public class Movie extends Activity {
    private String title;
    private String plot;
    private String poster;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent activityThatCalled = getIntent();
        String[] parts = activityThatCalled.getExtras().getStringArray("info");

        TextView textview = (TextView) findViewById(R.id.tvJsonItem1);
        TextView textview2 = (TextView) findViewById(R.id.tvJsonItem2);
        ImageView imageView = (ImageView) findViewById(R.id.poster);
        Button btnSave = (Button) findViewById(R.id.save);

        title(parts);
        plot(parts);
        poster(parts);

        textview.setText(title);
        textview2.setText(plot);

        Picasso.with(getApplicationContext())
                .load(poster)
                .into(imageView);

        btnSave.setOnClickListener(new View.OnClickListener() {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            @Override
            public void onClick(View v) {
                persistence(title);
            }
        });

}
    public String title(String [] parts){
        return  title = parts[0].replaceAll("\"", "");
    }
    public String plot(String[] parts){
        return plot = parts[9].replaceAll("\"", "");
    }
    public String poster(String[] parts){
        return poster = parts[13].replaceAll("\"", "").substring(7);
    }
    public void persistence(String title){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(title, title);
        editor.commit();}
}
