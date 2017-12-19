package com.example.julian.movielistbeta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.MonthDisplayHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Bildet mithilfe des {@link MovieListDbHelper} die zur Datenbank
 *
 * Created by Julian on 17.05.2017.
 */

class MovieListDataSource {
    /**
     * Log Tag für das Debuggen
     */
    private static final String LOG_TAG = MovieListDataSource.class.getSimpleName();
    /**
     * Variable für die lokale Datenbank
     */
    private SQLiteDatabase database;
    /**
     *  {@link MovieListDbHelper} ueber den die Datenbank erstellt, oder auf diese zugegriffen wird
     */
    private MovieListDbHelper dbHelper;

    /**
     * Array mit Angabe der Spalten der Tabelle in der Datenbank
     */
    private String[] columns = {
            MovieListDbHelper.COLUMN_ID,
            MovieListDbHelper.COLUMN_TITLE,
            MovieListDbHelper.COLUMN_YEAR,
            MovieListDbHelper.COLUMN_RUNTIME,
            MovieListDbHelper.COLUMN_GENRE,
            MovieListDbHelper.COLUMN_PLOT,
            MovieListDbHelper.COLUMN_POSTER,
            MovieListDbHelper.COLUMN_LANGUAGE,
            MovieListDbHelper.COLUMN_STATUS
    };

    /**
     * Erstellt einen MovieListDataSource durch die, mithilfe eines {@link MovieListDbHelper}, die Datenbank verwaltet wird
     *
     * @param c Kontext in dem die Datenbank genutzt wird
     */
    MovieListDataSource(Context c){
        Log.d(LOG_TAG, "MovieListDataSource erzeugt jzt den dbHelper. ");
        dbHelper = new MovieListDbHelper(c);
    }

    /**
     * Stellt ueber einen {@link MovieListDbHelper} die Verbindung zur Datenbank her
     */
    void open(){
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    /**
     * Schliesst ueber einen {@link MovieListDbHelper} die Verbindung zur Datenbank
     */
    void close(){
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    /**
     * Fuegt der Datenbank ueber ein {@link MovieDataObject} ein Film hinzu
     *
     * @param object MovieDataObject eines Films
     */
    void addMovieDataObject(MovieDataObject object){
        ContentValues values = new ContentValues();

        values.put(MovieListDbHelper.COLUMN_RUNTIME, object.getRuntime());
        values.put(MovieListDbHelper.COLUMN_TITLE, object.getTitle());
        values.put(MovieListDbHelper.COLUMN_YEAR, object.getYear());
        values.put(MovieListDbHelper.COLUMN_GENRE, object.getGenre());
        values.put(MovieListDbHelper.COLUMN_PLOT, object.getPlot());
        values.put(MovieListDbHelper.COLUMN_POSTER, object.getPoster());
        values.put(MovieListDbHelper.COLUMN_LANGUAGE, object.getLanguage());
        values.put(MovieListDbHelper.COLUMN_STATUS, object.getStatus());

        object.setId((int) database.insert(MovieListDbHelper.TABLE_DATA_LIST,null,values));
    }

    /**
     *  Erstellt mit einem Cursor ein {@link MovieDataObject}
     *
     * @param cursor Ein Film aus der Datenbank
     * @return Das MovieDataObject eines Films
     */
    private MovieDataObject cursorToMovieDataObject(Cursor cursor){
        int idIndex = cursor.getColumnIndex(MovieListDbHelper.COLUMN_ID);
        int idRuntime = cursor.getColumnIndex(MovieListDbHelper.COLUMN_RUNTIME);
        int idTitle = cursor.getColumnIndex(MovieListDbHelper.COLUMN_TITLE);
        int idYear = cursor.getColumnIndex(MovieListDbHelper.COLUMN_YEAR);
        int idGenre = cursor.getColumnIndex(MovieListDbHelper.COLUMN_GENRE);
        int idPlot = cursor.getColumnIndex(MovieListDbHelper.COLUMN_PLOT);
        int idPoster = cursor.getColumnIndex(MovieListDbHelper.COLUMN_POSTER);
        int idLanguage = cursor.getColumnIndex(MovieListDbHelper.COLUMN_LANGUAGE);
        int idStatus = cursor.getColumnIndex(MovieListDbHelper.COLUMN_STATUS);

        int id = cursor.getInt(idIndex);

        String runtime = cursor.getString(idRuntime);
        String title = cursor.getString(idTitle);
        String year = cursor.getString(idYear);
        String genre = cursor.getString(idGenre);
        String plot = cursor.getString(idPlot);
        String poster = cursor.getString(idPoster);
        String language = cursor.getString(idLanguage);
        String status = cursor.getString(idStatus);

        return new MovieDataObject(id,runtime,title,year,genre,plot,poster,language,status);
    }

    /**
     * Stellt alle Filme eines bestimmten Tabs der {@link MainActivity} zur Verfuegung
     *
     * @param sectionNumber Der Status des Films
     * @return Die Ergebnistabelle der Datenbank mit den Filmen eines Status/Tabs der MainActivity
     */
    Cursor getAllMoviesCursor(int sectionNumber){
        String Query = "SELECT * FROM " + MovieListDbHelper.TABLE_DATA_LIST + " WHERE " + MovieListDbHelper.COLUMN_STATUS + "=" + "'"
                + Integer.toString(sectionNumber) + "' " + "ORDER BY " + MovieListDbHelper.COLUMN_TITLE;
        Cursor cursor = database.rawQuery(Query, null);
        cursor.moveToFirst();

        return cursor;
    }

    /**
     * Liefert ein Film als {@link MovieDataObject} aus der Datenbank
     *
     * @param id ID des Films in der Datenbank
     * @return Das MovieDataObject des Films
     */
    MovieDataObject getMovie(String id){
        Cursor cursor = database.query(MovieListDbHelper.TABLE_DATA_LIST,columns,
                MovieListDbHelper.COLUMN_ID + "='" + id + "'",
                null,null,null,null);
        cursor.moveToFirst();

        MovieDataObject movie = cursorToMovieDataObject(cursor);

        cursor.close();

        return movie;
    }

    /**
     * Ueberprueft ob sich der gesuchte Film bereits in der Datenbank befindet
     *
     * @param title Der Titel des gesuchten Films
     * @return Der Film befindet sich in der Datenbank
     */
    boolean checkMovieIsInDB(String title){

        String query = "SELECT * FROM " + MovieListDbHelper.TABLE_DATA_LIST + " WHERE " + MovieListDbHelper.COLUMN_TITLE + " = ?";
        Cursor cursor =database.rawQuery(query, new String[]{title});
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        return true;
    }

    /**
     * Aendert den Status eines Films oder loescht diesen aus der Datenbank
     *
     * @param id ID des Films
     * @param status Status des Films
     */
    void updateStatus(int id, String status){
        if(!status.equals("4")){
            ContentValues values = new ContentValues();
            values.put(MovieListDbHelper.COLUMN_STATUS, status);
            String where = MovieListDbHelper.COLUMN_ID + "=?";
            String[] whereArgs = new String[]{Integer.toString(id)};
            database.update(MovieListDbHelper.TABLE_DATA_LIST,values,where,whereArgs);
        }else{
            String where = MovieListDbHelper.COLUMN_ID + "=?";
            String[] whereArgs = new String[]{Integer.toString(id)};
            database.delete(MovieListDbHelper.TABLE_DATA_LIST,where,whereArgs);
        }
    }
}
