package com.example.julian.movielistbeta;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import android.os.Handler;

/**
 * Mit dieser Klasse werden die Suchergebnisse aus {@link MovieInfoActivity} angezeigt und zu OnItemClickListener
 *
 * Created by Julian on 20.06.2017.
 */

class MySearchResultAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {
    private Context ctx;

    /**
     * Die {@link ArrayList} von {@link MovieDataObject} in dem die Informationen zu den Filmen zwischengespeichert werden
     */
    private ArrayList<MovieDataObject> mvs = new ArrayList<>();

    /**
     * @param context Kontext in dem sich der Adapter befindet
     * @param resource Die XML-Datei mit der die Items angezeigt werden sollen
     * @param movies Eine Liste an {@link MovieDataObject}
     */
    MySearchResultAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<MovieDataObject> movies) {
        super(context, resource, movies);

        ctx = context;
        mvs = movies;

    }

    @Override
    public int getCount() {
        return mvs.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_image_item,null);

        String img = mvs.get(position).getPoster();
        ImageView imgView = (ImageView) convertView.findViewById(R.id.poster);


        if(img.equals("https://image.tmdb.org/t/p/w185null")){
            imgView.setImageResource(R.drawable.noimg);
        }else{
            Glide.with(ctx).load(img).crossFade().into(imgView);
        }

        String title = mvs.get(position).getTitle();
        TextView titleView = (TextView) convertView.findViewById(R.id.movie_title);
        titleView.setText(title);

        String genre = mvs.get(position).getGenre();
        TextView titleView_genre = (TextView) convertView.findViewById(R.id.movie_genre);
        if(genre.equals("")){
            titleView_genre.setText("Unknown Genre");
        }else{
            titleView_genre.setText(genre);
        }

        String runtime = mvs.get(position).getRuntime();
        TextView titleView_runtime = (TextView) convertView.findViewById(R.id.movie_runtime);
        if(runtime.equals("0 minutes")){
            titleView_runtime.setText("Unknown Runtime");
        }else{
            titleView_runtime.setText(runtime);
        }


        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MovieDataObject movie = mvs.get(i);

        Intent intent = new Intent(getContext(), SearchResultActivity.class);
        intent.putExtra("Movie",  movie);
        ctx.startActivity(intent);
    }
}
