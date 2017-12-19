package com.example.julian.movielistbeta;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Mit dieser Klasse werden die Filme aus der Datenbank in Abhaengigkeit von dem Status in einer ListView dargestellt
 *
 * Created by Julian on 18.05.2017.
 */
class MyAdapter extends CursorAdapter{
    private LayoutInflater inflater;
    private int itemLayout;
    private String[] from;
    private int [] to;

    /**
     * Erzeugt einen Cursor Adapter, welcher den Datenbankinhalt mit einer View verknuepft
     *
     * @param context Kontext in dem sich der Adapter befindet
     * @param itemLayout ID der XML-Datei, welche das Aussehen jedes Items bestimmt
     * @param c Ergebnistabelle aus der Datenbank
     * @param from Array mit den Spalten der Ergebnistabelle
     * @param to Array mit den entsprechenden ID's der Views
     * @param flags Legt fest wie mit einem Intent verfahren wird
     */
    MyAdapter(Context context, int itemLayout, Cursor c, String[] from, int[] to, int flags) {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
        this.itemLayout = itemLayout;
        this.from = from;
        this.to = to;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return inflater.inflate(itemLayout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String image = cursor.getString(cursor.getColumnIndexOrThrow(from[0]));
        ImageView iview = (ImageView) view.findViewById(to[0]);

        if(image.equals("https://image.tmdb.org/t/p/w185null")){
            iview.setImageResource(R.drawable.noimg);
        }else{
            Glide.with(context).load(image).crossFade().into(iview);
        }

        String title = cursor.getString(cursor.getColumnIndexOrThrow(from[1]));
        TextView titleView = (TextView) view.findViewById(to[1]);
        titleView.setText(title);

        String genre = cursor.getString(cursor.getColumnIndexOrThrow(from[2]));
        TextView titleView_genre = (TextView) view.findViewById(to[2]);
        if(genre.equals("")){
            titleView_genre.setText("Unknown Genre");
        }else{
            titleView_genre.setText(genre);
        }

        String runtime = cursor.getString(cursor.getColumnIndexOrThrow(from[3]));
        TextView titleView_runtime = (TextView) view.findViewById(to[3]);
        if(runtime.equals("0 minutes")){
            titleView_runtime.setText("Unknown Runtime");
        }else{
            titleView_runtime.setText(runtime);
        }

    }
}
