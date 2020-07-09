package com.example.bemyguide.models;


import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Ville extends Model implements Serializable {
    private int id;
    private String nom;
    private String photo;
    private String description;
    private int likes;
    private int visits;
    private int places;
    private int sync;
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public Ville(){

    }

    public Ville(Cursor cursor){
        this.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        this.nom = cursor.getString(cursor.getColumnIndex("nom"));
        this.photo = cursor.getString(cursor.getColumnIndex("photo"));
        this.description = cursor.getString(cursor.getColumnIndex("description"));
        this.sync =cursor.getInt(cursor.getColumnIndex("sync"));
    }

    public Ville(int id, String nom, String photo, String description,int sync) {
        this.id = id;
        this.nom = nom;
        this.photo = photo;
        this.description = description;
        this.sync = sync;
        this.likes = 0;
        this.visits = 0;
        this.places = 0;
    }

    public Ville(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.likes = 0;
        this.visits = 0;
        this.places = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", photo='" + photo + '\'' +
                ", description='" + description + '\'' +
                ", sync=" + sync +
                '}';
    }

    public ContentValues setContentValue(ContentValues c){
        c.put("nom",this.nom);
        c.put("photo",this.photo);
        c.put("description",this.description);
        c.put("sync",this.sync);
        return c;
    }

    public static ArrayList<Model> parseData(JsonArray datas){
        ArrayList<Model> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            JsonObject data = datas.get(i).getAsJsonObject();
            list.add(new Ville(data.get("id").getAsInt(), data.get("nom").getAsString(),  data.get("photo").getAsString(), data.get("description").getAsString(), data.get("sync").getAsInt()));
        }

        return list;
    }

    public String getTabeName(){
        return "villes";
    }
}
