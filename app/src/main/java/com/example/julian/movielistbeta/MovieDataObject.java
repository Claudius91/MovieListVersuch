package com.example.julian.movielistbeta;

import java.io.Serializable;

/**
 * Dient der Verwaltung aller Informationen zu einem Film
 *
 * Created by Julian on 17.05.2017.
 */
class MovieDataObject  implements Serializable{


    public int id;

    private String runtime;
    private String response;
    private String title;
    private String year;
    private String genre;
    private String plot;
    private String poster;
    private String language;
    private String status;

    /**
     * Erstellt ein MovieDataObject mit den Informationen zu einem Film
     *
     * @param id Die ID des Films in der Datenbank
     * @param runtime Die Laufzeit
     * @param title Der Titel
     * @param year  Das Datum der Veröffentlichung
     * @param genre Die Genreangaben
     * @param plot Die Zusammenfassung
     * @param poster Der Link zum Poster
     * @param language Die gesprochene Originalsprache
     * @param status Der Status
     */
    MovieDataObject(int id, String runtime, String title, String year, String genre, String plot,
                           String poster, String language, String status){
        this.runtime = runtime;
        this.title = title;
        this.id = id;
        this.year = year;
        this.genre = genre;
        this.plot = plot;
        this.poster = poster;
        this.language = language;
        this.status = status;
    }

    /**
     * Erstellt ein MovieDataObject mit Default-Informationen zu einem Film
     */
    MovieDataObject(){
        this.response = "False";
        this.runtime = "runtime";
        this.title = "title";
        this.year = "year";
        this.genre = "genre";
        this.plot = "plot";
        this.poster = "poster";
        this.language = "language";
        this.status = "status";
    }

    /**
     *
     * @return War der Request erfolgreich
     */
    String getResponse() {return response;}

    /**
     *
     * @param response War der Request erfolgreich
     */
    void setResponse(String response) {this.response = response;}

    /**
     *
     * @return ID des Films in der Datenbank
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id ID des Films in der Datenbank
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return Die Laufzeit des Films
     */
    String getRuntime() {
        return runtime;
    }

    /**
     *
     * @param runtime Die Laufzeit des Films
     */
    void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    /**
     *
     * @return Der Titel des Films
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title Der Titel des Films
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return Datum der Veroeffentlichung des Films
     */
    String getYear() {
        return year;
    }

    /**
     *
     * @param year Datum der Veroeffentlichung des Films
     */
    void setYear(String year) {
        this.year = year;
    }

    /**
     *
     * @return Die Genre des Films
     */
    String getGenre() {
        return genre;
    }

    /**
     *
     * @param genre Die Genre des Films
     */
    void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     *
     * @return Die Zusammenfassung des Films
     */
    String getPlot() {
        return plot;
    }

    /**
     *
     * @param plot Die Zusammenfassung des Films
     */
    void setPlot(String plot) {
        this.plot = plot;
    }

    /**
     *
     * @return Der Link zum Poster des Films
     */
    String getPoster() {
        return poster;
    }

    /**
     *
     * @param poster Der Link zum Poster des Films
     */
    void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     *
     * @return Die gesprochene Originalsprache des Films
     */
    String getLanguage() {
        return language;
    }

    /**
     *
     * @param language Die gesprochene Originalsprache des Films
     */
    void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @return Der Status des Films
     */
    String getStatus() {
        return status;
    }

    /**
     *
     * @param status Der Status des Films
     */
    void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return Alle Informationen zum Film
     */
    @Override
    public String toString(){
        return title + " " + poster + " " + runtime + " " + year + " " +
                genre + " " + plot + " " + language + " " + status + " " + id;
    }
}
