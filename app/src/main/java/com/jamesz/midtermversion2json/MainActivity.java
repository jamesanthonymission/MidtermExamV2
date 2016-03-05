package com.jamesz.midtermversion2json;

import android.app.ListActivity;
import android.content.ClipData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView listview;
    Spinner spinner;
    EditText editTextSearch;
    EditText editTextAuthor;
    private static final String BOOK_NAME = "title";
    ArrayList<HashMap<String, String>> bookList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editTextSearch = (EditText)findViewById(R.id.EditTextSearch);
        editTextAuthor = (EditText)findViewById(R.id.EditTextAuthor);


        bookList = new ArrayList<HashMap<String, String>>();

        new FetchBooks().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spinner = (Spinner)findViewById(R.id.spin);
        spinner.setOnItemSelectedListener(this);
        spinner.setVisibility(View.INVISIBLE);
        editTextSearch.setVisibility(View.INVISIBLE);
        editTextAuthor.setVisibility(View.INVISIBLE);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        spinner.setPrompt("Search By: ");

        if(spinner.getSelectedItem().toString().equals("Genre")){
            editTextSearch.setVisibility(View.VISIBLE);
            editTextAuthor.setVisibility(View.INVISIBLE);
        }
        else if(spinner.getSelectedItem().toString().equals("Author")){
            editTextAuthor.setVisibility(View.VISIBLE);
            editTextSearch.setVisibility(View.INVISIBLE);
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void RefreshBtn(View view){
        Toast.makeText(this,"Refresh",Toast.LENGTH_LONG).show();
    }



    public void searchBtn(View view){
        editTextSearch.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.VISIBLE);
        editTextAuthor.setVisibility(View.INVISIBLE);
       // Toast.makeText(this,"try", Toast.LENGTH_LONG).show();


        spinner.setPrompt("Search By:");

        List<String> categories = new ArrayList<String>();
        categories.add("Genre");
        categories.add("Author");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Search By");
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast.makeText(this,"i was clicked", Toast.LENGTH_LONG).show();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class FetchBooks extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {


            try {
                //String query  = URLEncoder.encode("Victory Hugo", "UTF-8");
                String json = HttpUtils.getResponse("http://joseniandroid.herokuapp.com/api/books/", "GET");
                Log.d("Response: ", json);
                if (json != null) {
                    try {
                        JSONArray jArray = new JSONArray(json);


                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject b = jArray.getJSONObject(i);
                            String title = b.getString(BOOK_NAME);
                            //String author = b.getString(AUTHOR);
                            HashMap<String, String> book = new HashMap<String, String>();

                            book.put(BOOK_NAME, title);
                            // book.put("author",author);
                            bookList.add(book);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, bookList, R.layout.listview, new String[]{"title"}, new int[]{R.id.bookName});
            //listview = (ListView)findViewById(R.id.listview);
            //listview.setAdapter(adapter);
            listview.setAdapter(adapter);
        }
    }
}
