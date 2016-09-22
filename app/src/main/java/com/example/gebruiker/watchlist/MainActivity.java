package com.example.gebruiker.watchlist;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//Users should be able to search for movies, read descriptions and view poster art,
// as well as add such titles to a list of movies to watch.
// And of course, they should be able to remove titles from the list as well!
public class MainActivity extends Activity {

    private String request;
    private String title;
    private String plot;
    private String poster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //source: https://www.youtube.com/watch?v=Gyaay7OTy-w
        Button btnSearch = (Button) findViewById(R.id.btnHit);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInput();
                new JSONTask().execute("http://www.omdbapi.com/?t="+request);

            }}
        );

    }
    public String getUserInput()
    {
        EditText inputText = (EditText) findViewById((R.id.userInput));
        return request = String.valueOf(inputText.getText()).replaceAll(" ", "+");
    }
    public void test(){
        TextView textView = (TextView)findViewById(R.id.tvJsonItem1);
        textView.setText(plot+"gelukt");
    }
    public class JSONTask extends AsyncTask<String, String, String> {

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


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            TextView textview = (TextView) findViewById(R.id.tvJsonItem1);
            TextView textview2 = (TextView) findViewById(R.id.tvJsonItem2);
            ImageView imageView = (ImageView) findViewById(R.id.poster);

            String movie = result.substring(1,result.length()-1);
            if (movie.startsWith("\"Response\":\"False\",\"Error\"")){
                textview.setText(movie);
            }
            else {
                String[] parts = movie.split("\",\"");
                String title = parts[0].replaceAll("\"", "");
                String plot = parts[9].replaceAll("\"", "");
                String poster = parts[13].replaceAll("\"", "").substring(7);

                textview.setText(title);
                textview2.setText(plot);

                Picasso.with(getApplicationContext())
                        .load(poster)
                        .into(imageView);
            }

        }
    }

}