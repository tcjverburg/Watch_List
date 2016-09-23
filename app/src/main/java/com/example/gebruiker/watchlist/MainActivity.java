package com.example.gebruiker.watchlist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

//Users should be able to search for movies, read descriptions and view poster art,
// as well as add such titles to a list of movies to watch.
// And of course, they should be able to remove titles from the list as well!
public class MainActivity extends Activity {

    private String request;
    private ArrayList<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //source: https://www.youtube.com/watch?v=Gyaay7OTy-w
        Button btnSearch = (Button) findViewById(R.id.btnHit);
        Button btntest = (Button) findViewById(R.id.btnHit2);
        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            //debug
            public void onClick(View v) {
                TextView textview = (TextView) findViewById(R.id.tvJsonItem1);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String [] array = reader(pref);
                textview.setText(array[1]);
                //1

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInput();



                new JSONTask().execute("http://www.omdbapi.com/?t="+request);



            }});




    }


    public String getUserInput()
    {
        EditText inputText = (EditText) findViewById((R.id.userInput));
        return request = String.valueOf(inputText.getText()).replaceAll(" ", "+");
    }




public String[] reader(SharedPreferences pref){
        ArrayList<String> list = new ArrayList<String>();
         Map<String, ?> allEntries = pref.getAll();

         for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
             list.add(entry.getValue().toString());}
            String[] arr = list.toArray(new String[list.size()]);

                return arr;
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
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            String [] array = reader(pref);

            String movie = result.substring(1,result.length()-1);
            if (movie.startsWith("\"Response\":\"False\",\"Error\"")){
                //debug
                textview.setText(array[0]);



            }
            else {
                String[] parts = movie.split("\",\"");
                Intent getNameScreen = new Intent(getApplicationContext(), Movie.class);
                //passes extra data story_end
                getNameScreen.putExtra("info", parts);
                startActivity(getNameScreen);


            }

        }
    }

}