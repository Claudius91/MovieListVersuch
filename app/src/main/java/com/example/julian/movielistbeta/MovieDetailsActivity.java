package com.example.julian.movielistbeta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
 * Dient der Darstellung der Informationen eines Films
 *
 * Created by Julian on 21.05.2017.
 */

public class MovieDetailsActivity extends AppCompatActivity{
    private String movieID;
    private Context ctx;

    /**
     * Die {@link MovieListDataSource} mit der die Datenbankzugriffe erfolgen
     */
    private MovieListDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ImageView imgPoster = (ImageView) findViewById(R.id.image_poster);
        TextView title = (TextView) findViewById(R.id.textview_title);
        TextView genre = (TextView) findViewById(R.id.textview_genre);
        TextView runtime = (TextView) findViewById(R.id.textview_runtime);
        TextView plot = (TextView) findViewById(R.id.textview_plot);
        Button btnMoveTo = (Button) findViewById(R.id.button_move);
        //ScrollView searchResult = (ScrollView) findViewById(R.id.search_result);

        dataSource = new MovieListDataSource(this);

        Intent intent = getIntent();
        movieID = intent.getStringExtra("ID");

        dataSource.open();
        MovieDataObject movie = dataSource.getMovie(movieID);
        dataSource.close();

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

        btnMoveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[]values = {"Interesting", "Currently watching","Watched","Delete"};
                final Dialog listDialog = new Dialog(MovieDetailsActivity.this);

                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View v = inflater.inflate(R.layout.move_to_status_view, null, false);

                listDialog.setContentView(v);
                listDialog.setCancelable(true);
                listDialog.setCanceledOnTouchOutside(true);

                ListView list = (ListView) listDialog.findViewById(R.id.status_view);
                list.setAdapter(new ArrayAdapter<>(MovieDetailsActivity.this, android.R.layout.simple_list_item_1, values));

                listDialog.show();

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final int[] itemID = {i};

                        if(i == 3){
                            AlertDialog alert = new AlertDialog.Builder(MovieDetailsActivity.this).create();
                            alert.setTitle("Delete");
                            alert.setMessage("Do you want to delete this movie ?");
                            alert.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    dataSource.open();
                                    dataSource.updateStatus(Integer.parseInt(movieID), Integer.toString(itemID[0] + 1));
                                    dataSource.close();

                                    Toast.makeText(MovieDetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    listDialog.dismiss();
                                    finish();
                                }
                            });
                            alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    listDialog.dismiss();
                                }
                            });

                            final TextView myView = new TextView(getApplicationContext());
                            myView.setText("Delete");
                            myView.setPadding(65, 10, 10, 10);
                            myView.setTextSize(30);
                            myView.setTextColor(Color.WHITE);
                            alert.setCustomTitle(myView);

                            alert.show();
                        }else{
                            dataSource.open();
                            dataSource.updateStatus(Integer.parseInt(movieID), Integer.toString(i + 1));
                            dataSource.close();
                            finish();
                        }
                        listDialog.cancel();
                    }
                });
            }
        });
    }
}
