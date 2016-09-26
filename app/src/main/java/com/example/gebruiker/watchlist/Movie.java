package com.example.gebruiker.watchlist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        //getting intent from MainActivity and all the information from the user input from search
        Intent activityThatCalled = getIntent();
        String[] parts = activityThatCalled.getExtras().getStringArray("info");

        TextView textview = (TextView) findViewById(R.id.tvJsonItem1);
        TextView textview2 = (TextView) findViewById(R.id.tvJsonItem2);
        ImageView imageView = (ImageView) findViewById(R.id.poster);
        Button btnSave = (Button) findViewById(R.id.save);

        //defines title, plot and poster and put them in matching textviews and/or imageview
        title(parts);
        plot(parts);
        poster(parts);

        textview.setText("Title: " + title);
        textview2.setText(plot);

        Picasso.with(getApplicationContext())
                .load(poster)
                .into(imageView);


        //saves movie in shared preferences
        btnSave.setOnClickListener(new View.OnClickListener() {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            @Override
            public void onClick(View v) {
                persistence(title);
            }
        });

}
    //defines title
    public String title(String [] parts){
        return title = (parts[0].replaceAll("\"", "")).replace("Title:", "");
    }

    //defines plot

    public String plot(String[] parts){
        return plot = parts[9].replaceAll("\"", "");
    }

    //defines poster
    public String poster(String[] parts){
        return poster = parts[13].replaceAll("\"", "").substring(7);
    }
    //adds the key and value title to the shared preferences and gives the user feedback
    public void persistence(String title){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(title, title);
        editor.commit();
        Toast.makeText(Movie.this, "You have added " + title + " to your watch list.", Toast.LENGTH_SHORT).show();
    }
}
