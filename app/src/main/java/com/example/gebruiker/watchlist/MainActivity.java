package com.example.gebruiker.watchlist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * MainActivity.java
 * Watch List
 *
 * Created by Tom Verburg on 23-9-2016.
 *
 * This class is the starting main screen of the application. Here the watchlist
 * is shown with a search bar to search for new movies to add to the list.
 * The searching is done by requesting information from omdbapi (an api of imdb)
 * based on the user input in the edit text field. The calling of the watchlist
 * is done by getting information from the shared preferences that have been saved
 * in the Movie.java activity.
 *
 *
 */
public class MainActivity extends Activity {

    private String request;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText)findViewById(R.id.userInput);
        editText.setHint( "Search for movie to add to watch list.");
        if (savedInstanceState != null){
            String input = (String)savedInstanceState.getString("user input");
            editText.setText(input);
        }

        Button btnSearch = (Button) findViewById(R.id.btnHit);

        //get shared preferences, remove doubles and returning it as an String[]
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Set<String> set = new HashSet<String>(Arrays.asList(reader(pref)));
        String[] watchlist = set.toArray(new String[set.size()]);

        //Setting up the listview and adapter for the movies from shared preferences
        ListAdapter theAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,  watchlist);
        ListView theListView = (ListView)findViewById(R.id.theListView);
        theListView.setAdapter(theAdapter);

        //The on click method that uses an intent to see more info about a particular movie
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String moviePicked = String.valueOf(adapterView.getItemAtPosition(position));

                Intent getNameScreen = new Intent(getApplicationContext(), MovieList.class);
                getNameScreen.putExtra("Movie selected", moviePicked);
                startActivity(getNameScreen);
            }
        });

        //on click method to search for movies and information about them using the omdapi api and the user input
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInput();

                new JSONTask().execute("http://www.omdbapi.com/?t="+request);

            }});
    }

    //reloads page so that the watch list is up to date after saving/removing movies
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    //saves user input when rotating screen
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String userInput = getUserInput();
        outState.putSerializable("user input", userInput);
    }

    //gets user input from edittext
    public String getUserInput()
    {
        EditText inputText = (EditText) findViewById((R.id.userInput));
        return request = String.valueOf(inputText.getText()).replaceAll(" ", "+");
    }

    //gets all the shared preferences and returns array(with doubles, in case the user accidentally added movie twice)
public String[] reader(SharedPreferences pref){
        ArrayList<String> list = new ArrayList<String>();
         Map<String, ?> allEntries = pref.getAll();

         for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
             list.add(entry.getValue().toString());}
            String[] arr = list.toArray(new String[list.size()]);

                return arr;
            }

    //source: https://www.youtube.com/watch?v=Gyaay7OTy-w
    //connecting tot the api and returning string with all the information about the particular movie or an error
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            //tries to make connection if possible
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                //adds line by line to the buffer from the api
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

        //filters the string we got as a result of the method getting the information from the api
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //splitting the result in different strings and returning error if the user input was not valid
            String movie = result.substring(1,result.length()-1);
            if (movie.startsWith("\"Response\":\"False\",\"Error\"")){
                Toast.makeText(MainActivity.this, "Error: Not a valid movie title!", Toast.LENGTH_SHORT).show();
            }
            else {
                String[] parts = movie.split("\",\"");
                Intent getNameScreen = new Intent(getApplicationContext(), Movie.class);
                getNameScreen.putExtra("info", parts);
                startActivity(getNameScreen);


            }
        }
    }

}