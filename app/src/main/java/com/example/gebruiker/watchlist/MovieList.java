package com.example.gebruiker.watchlist;

/**
 * Created by Gebruiker on 25-9-2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gebruiker on 23-9-2016.
 */
public class MovieList extends Activity {
    private String title;
    private String plot;
    private String poster;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);


        Button btnRemove = (Button) findViewById(R.id.remove);

        //gets intent from MainActivity
        Intent activityThatCalled = getIntent();
        String SelectedMovie = activityThatCalled.getExtras().getString("Movie selected").replace("Title:","");

        //request data again for poster url and plot
        new JSONTask2().execute("http://www.omdbapi.com/?t="+SelectedMovie.replaceAll(" ","+"));

        //on click method for the removal of a specific movie from the list
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {persistence(title);
            }
        });

    }

        //same as JSONTask in Mainactivity
    public class JSONTask2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        //gets proper information from the result and sets them to matching textview/imageview
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String movie = result.substring(1, result.length() - 1);
            String[] parts = movie.split("\",\"");
            TextView textview = (TextView) findViewById(R.id.tvJsonItem1);
            TextView textview2 = (TextView) findViewById(R.id.tvJsonItem2);
            ImageView imageView = (ImageView) findViewById(R.id.poster);
            title(parts);
            plot(parts);
            poster(parts);
            textview.setText("Title: " + title);
            textview2.setText(plot);

            Picasso.with(getApplicationContext())
                    .load(poster)
                    .into(imageView);
        }
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

        //on the click of the button, movie removed from watchlist and gives user feedback.
        public void persistence(String title){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(title);
            editor.commit();
            Toast.makeText(MovieList.this, "You have removed " + title + " from your watch list.", Toast.LENGTH_SHORT).show();
        }

    }


