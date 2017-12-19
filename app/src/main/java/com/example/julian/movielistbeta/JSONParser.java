package com.example.julian.movielistbeta;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Stellt Methoden zur Verfuegung, um mithilfe einer URL und einem beliebigen Suchbegriff über die Schnittstelle der "The Movie Database API" einen FIlm zu suchen.
 * Der Response liefert ein JSONObjekt, welches in ein {@link MovieDataObject} umgewandelt und zurueckgeliefert wird.
 *
 * Created by Julian on 18.05.2017.
 */
class JSONParser {
    /**
     * Log Tag für das Debuggen
     */
    private static final String LOG_TAG = JSONParser.class.getSimpleName();

    /**
     * Gibt an ob die Suche erfolgreich war
     */
    private static String response = "False";

    /**
     * Schnittstelle zur API
     */
    //&language=de nach ba7ac ums auf Deutsch umzustellen
    private static final String address = "https://api.themoviedb.org/3/search/movie?api_key=3035bb2a900b47bd4e762289f22ba7ac&query=";

    /**
     * Vervollstaendigt die URL mit einem gewuenschten Suchbegriff.
     *
     * @param s Der Name des gesuchten Films
     * @return Die komplettierte URL
     */
    private static String searchTerm(String s){
        s = s.replace(" ","+");
        return (address + s);
    }

    /**
     * Liefert ein {@link JSONArray} mit den Informationen zu den gesuchten Filmen.
     *
     * @param url URL als Schnittstelle zur API
     * @return Das JSONObjekt mit den Informationen zum Film
     * @throws IOException
     * @throws JSONException
     */
    private static JSONArray getJSONFromUrl(String url)
            throws IOException, JSONException{

        InputStream is = new URL(url).openStream();


        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int s;
            while((s = reader.read()) != -1){
                sb.append((char) s);
            }
            String jsonContent = sb.toString();

            JSONObject jObjTmp = new JSONObject(jsonContent);

            if(jObjTmp.getInt("total_results") > 0) {
                response = "True";
            }

            Log.d(LOG_TAG, "Response JSONObject erhalten");

            return jObjTmp.getJSONArray("results");

        }finally {
            is.close();
        }
    }

    /**
     * Liefert eine {@link ArrayList} von {@link MovieDataObject} mit den jeweiligen Informationen zu einem Film.
     *
     * @param url URL als Schnittstelle zur API
     * @return Das MovieDataObject zum Film
     */
    static ArrayList<MovieDataObject> getMovieDataObjectFromURL(String[] url){
        ArrayList<MovieDataObject> movies = new ArrayList<>();
        JSONArray json;

        Log.d(LOG_TAG, "Request JSONObject angefordert");

        try {
            json = getJSONFromUrl(searchTerm(url[0]));

            for(int i = 0; i < json.length(); i++){
                JSONObject o = json.getJSONObject(i);

                InputStream is = new URL("https://api.themoviedb.org/3/movie/"+ o.getInt("id") + "?api_key=3035bb2a900b47bd4e762289f22ba7ac").openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int k;
                while((k = reader.read()) != -1){
                    sb.append((char) k);
                }

                String jsonContent = sb.toString();

                JSONObject jObj = new JSONObject(jsonContent);

                MovieDataObject movie = new MovieDataObject();

                movie.setResponse(response);
                movie.setTitle(jObj.get("title").toString());
                movie.setRuntime(jObj.get("runtime").toString() + " minutes");
                movie.setYear(jObj.get("release_date").toString());

                JSONArray genre = jObj.getJSONArray("genres");
                movie.setGenre(genreParser(genre));


                movie.setPlot(jObj.get("overview").toString());
                movie.setPoster("https://image.tmdb.org/t/p/w185" + jObj.get("poster_path").toString());
                movie.setLanguage(jObj.get("original_language").toString());
                movie.setStatus("1");

                movies.add(movie);
            }

            Log.d(LOG_TAG, "Film wird erstellt");

            Log.d(LOG_TAG, "Film erstellt / Anzahl: " + movies.size());
        }catch(IOException e){
            e.printStackTrace();
        }catch (JSONException je){
            je.printStackTrace();
        }
        return movies;
    }

    /**
     * Schreibt Informationen aus einem JSONArray in eine Stringvariable.
     *
     * @param genres JSONArray mit den Genre-Informationen zu einem Film
     * @return Der fertige String mit Genreangaben zum Film
     * @throws JSONException
     */
    private static String genreParser(JSONArray genres) throws JSONException{
        StringBuilder sb = new StringBuilder();
        JSONObject o;

        for(int i = 0; i < genres.length(); i++){
            o = genres.getJSONObject(i);
            sb.append(o.get("name").toString());
            if(i < genres.length() -1){
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
