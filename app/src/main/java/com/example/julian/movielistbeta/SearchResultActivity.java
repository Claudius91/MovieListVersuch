package com.example.julian.movielistbeta;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Dient der Darstellung der Informationen eines der Suchergebnisse
 *
 * Created by Julian on 21.06.2017.
 */

public class SearchResultActivity extends AppCompatActivity {
    /**
     * Das {@link MovieDataObject} mit dem die Daten eines Films zwischengespeichert werden
     */
    private MovieDataObject movie;
    /**
     * Die {@link MovieListDataSource} mit der die Datenbankzugriffe erfolgen
     */
    private MovieListDataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        dataSource = new MovieListDataSource(this);

        Intent intent = getIntent();
        movie = (MovieDataObject) intent.getSerializableExtra("Movie");


        ImageView imgPoster = (ImageView) findViewById(R.id.image_poster);
        TextView title = (TextView) findViewById(R.id.textview_title);
        TextView genre = (TextView) findViewById(R.id.textview_genre);
        TextView runtime = (TextView) findViewById(R.id.textview_runtime);
        TextView plot = (TextView) findViewById(R.id.textview_plot);
        Button btnAdd = (Button) findViewById(R.id.button_move);

        title.setText(movie.getTitle());

        if(movie.getGenre().equals("")){
            genre.setText("Unknown Genre");
        }else{
            genre.setText(movie.getGenre());
        }

        if(movie.getRuntime().equals("0 minutes")){
            runtime.setText("Unknown Runtime");
        }else{
            runtime.setText(movie.getRuntime());
        }

        plot.setText(movie.getPlot());

        if((movie.getPoster()).equals("https://image.tmdb.org/t/p/w185null")){
            imgPoster.setImageResource(R.drawable.noimg);
        }else{
            Glide.with(this).load(movie.getPoster()).crossFade().into(imgPoster);
        }

        btnAdd.setText("ADD");
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String[]values = {"Interesting","Currently watching","Watched"};
                final Dialog listDialog = new Dialog(SearchResultActivity.this);

                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View v = inflater.inflate(R.layout.move_to_status_view, null, false);

                listDialog.setContentView(v);
                listDialog.setCancelable(true);
                listDialog.setCanceledOnTouchOutside(true);

                ListView list = (ListView) listDialog.findViewById(R.id.status_view);
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchResultActivity.this, android.R.layout.simple_list_item_1, values);
                list.setAdapter(adapter);

                listDialog.show();

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        movie.setStatus(String.valueOf(i+1));

                        dataSource.open();

                        if(dataSource.checkMovieIsInDB(movie.getTitle())){
                            Toast.makeText(SearchResultActivity.this,"Movie already exists in Database", Toast.LENGTH_LONG).show();
                        }

                        if(!dataSource.checkMovieIsInDB(movie.getTitle())){
                            dataSource.addMovieDataObject(movie);
                            Toast.makeText(SearchResultActivity.this,"Movie added to " + adapter.getItem(i), Toast.LENGTH_LONG).show();
                        }

                        dataSource.close();
                        listDialog.dismiss();
                        finish();
                    }
                });

            }
        });
    }
}
