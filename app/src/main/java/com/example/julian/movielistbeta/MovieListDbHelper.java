package com.example.julian.movielistbeta;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Erzeugt eine Datenbank und legt eine Tabelle an, sofern diese noch nicht existieren
 *
 * Created by Julian on 17.05.2017.
 */
class MovieListDbHelper extends SQLiteOpenHelper {
    /**
     * Log Tag für das Debuggen
     */
    private static final String LOG_TAG = MovieListDbHelper.class.getSimpleName();

    /**
     * Name der Datenbank
     */
    private static final String DB_NAME = "MovieDataBase";
    /**
     * Version der Datenbank
     */
    private static final int DB_VERSION = 1;

    /**
     * Name der Tabelle in der Datenbank
     */
    static final String TABLE_DATA_LIST = "movie_db";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_YEAR = "year";
    static final String COLUMN_RUNTIME = "runtime";
    static final String COLUMN_GENRE = "genre";
    static final String COLUMN_PLOT = "plot";
    static final String COLUMN_POSTER = "poster";
    static final String COLUMN_LANGUAGE = "language";
    static final String COLUMN_STATUS = "status";

    /**
     * SQL-Statement fur das anlegen einer Tabelle
     */
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_DATA_LIST + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_YEAR + " INTEGER NOT NULL, " +
                    COLUMN_RUNTIME + " INTEGER NOT NULL, " +
                    COLUMN_GENRE + " TEXT NOT NULL, " +
                    COLUMN_PLOT + " TEXT NOT NULL, " +
                    COLUMN_POSTER + " TEXT NOT NULL, " +
                    COLUMN_LANGUAGE + " TEXT NOT NULL, " +
                    COLUMN_STATUS + " INTEGER NOT NULL);";


    /**
     * Erzeugt die Datenbank
     *
     * @param context Kontext in dem die Datenbank genutzt wird
     */
    MovieListDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "MovieListDbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt");
    }

    /**
     * Legt eine Tabelle in der Datenbank an
     *
     * @param db Die Datenbank, der die Tabelle hinzugefuegt wird
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);

        }catch (Exception e){
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
