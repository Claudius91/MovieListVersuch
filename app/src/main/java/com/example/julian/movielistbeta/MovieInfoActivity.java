package com.example.julian.movielistbeta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Stellt eine Suchfunktion zur Verfügung, welche über den Namen eines Filmes ausgeführt wird.
 * Bei erfolgreicher Suche werden die entsprechenden Filme angezeigt.
 *
 * Created by Julian on 18.05.2017.
 */

public class MovieInfoActivity extends AppCompatActivity{
    private EditText searchTerm;
    private ListView searchResult;

    /**
     * Die {@link ArrayList} von {@link MovieDataObject} in dem die Informationen zu den Filmen zwischengespeichert werden
     */
    private ArrayList<MovieDataObject> movies;
    /**
     * Der {@link MySearchResultAdapter} der die View mit den jeweiligen Filmen aufbaut
     */
    private  MySearchResultAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        searchTerm = (EditText) findViewById(R.id.edittext_search);
        Button btnSearch = (Button) findViewById(R.id.button_search);
        //Button btnAdd = (Button) findViewById(R.id.button_add);
        searchResult = (ListView) findViewById(R.id.search_result);

        movies = new ArrayList<>();
        searchAdapter = new MySearchResultAdapter(this,R.layout.list_image_item, movies);
        searchResult.setAdapter(searchAdapter);
        searchResult.setOnItemClickListener(searchAdapter);

        searchTerm.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (i == EditorInfo.IME_NULL) && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (searchTerm.getText().toString().equals("")){
                        Toast.makeText(MovieInfoActivity.this,"Please insert title", Toast.LENGTH_LONG).show();
                    }else{
                        MyAsyncTask omdbLoader = new MyAsyncTask();
                        omdbLoader.execute(searchTerm.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchTerm.getText().toString().equals("")){
                    Toast.makeText(MovieInfoActivity.this,"Please insert title", Toast.LENGTH_LONG).show();
                }else{
                    MyAsyncTask omdbLoader = new MyAsyncTask();
                    omdbLoader.execute(searchTerm.getText().toString());
                }
            }
        });
    }

    /**
     * Sorgt dafür, das die Anzeige der {@link MovieDataObject} aktualisiert wird
     *
     * @param movies Die {@link ArrayList} der Filme
     */
    private void updateView(ArrayList<MovieDataObject> movies){
        searchAdapter.clear();

        for(MovieDataObject object : movies){
            searchAdapter.insert(object, searchAdapter.getCount());
        }
        searchAdapter.notifyDataSetChanged();
    }

    /**
     * {@link AsyncTask} der mithilfe einer URL die Daten der Filme bezieht
     *
     * Created by Julian on 18.05.2017.
     */
    private class MyAsyncTask extends AsyncTask<String,Void,ArrayList<MovieDataObject>>{
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MovieInfoActivity.this, "Search", "Searching");
            progressDialog.setMessage("Searching...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    MyAsyncTask.this.cancel(true);
                }
            });
        }

        @Override
        protected ArrayList<MovieDataObject> doInBackground(String... strings) {
            movies = JSONParser.getMovieDataObjectFromURL(strings);

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDataObject> objects) {
            if(movies.size() > 0){
                updateView(movies);
            }else{
                Toast.makeText(MovieInfoActivity.this,"Movie not found", Toast.LENGTH_LONG).show();
            }

            progressDialog.cancel();
        }
    }
}
