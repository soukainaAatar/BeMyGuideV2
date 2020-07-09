package com.example.bemyguide.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Place extends Model implements Serializable {
    private int id;
    private String nom;
    private int ville_id;
    private String description;
    private int user_id;
    private String adress;
    private Double latitud;
    private Double longtitud;
    private int likes;
    private int visits;
    private int comments;
    private int sync;
    private boolean like_by_me;
    private boolean visit_by_me;
    private User owner;
    private Photo picture;


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Photo getPicture() {
        return picture;
    }

    public void setPicture(Photo picture) {
        this.picture = picture;
    }





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

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Place(){

    }

    public Place(Cursor cursor){
        this.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        this.ville_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ville_id")));
        this.nom = cursor.getString(cursor.getColumnIndex("nom"));
        this.description = cursor.getString(cursor.getColumnIndex("description"));
        this.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id")));
        this.adress =  cursor.getString(cursor.getColumnIndex("adress"));
        this.latitud = cursor.getDouble(cursor.getColumnIndex("lat"));
        this.longtitud =  cursor.getDouble(cursor.getColumnIndex("long"));
        this.sync = cursor.getInt(cursor.getColumnIndex("sync"));

    }

    public Place(int id, String nom, int ville_id, String description, int user_id, String adress, Double latitud, Double longtitud,int sync) {
        this.id = id;
        this.nom = nom;
        this.ville_id = ville_id;
        this.description = description;
        this.user_id = user_id;
        this.adress = adress;
        this.latitud = latitud;
        this.longtitud = longtitud;
        this.likes = 0;
        this.visits = 0;
        this.comments = 0;
        this.sync = sync;
    }

    public Place(int id, String nom, int ville_id, int user_id, Double latitud, Double longtitud) {
        this.id = id;
        this.nom = nom;
        this.ville_id = ville_id;
        this.user_id = user_id;
        this.latitud = latitud;
        this.longtitud = longtitud;
        this.likes = 0;
        this.visits = 0;
        this.comments = 0;
        this.sync = 1;
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

    public int getVille_id() {
        return ville_id;
    }

    public void setVille_id(int ville_id) {
        this.ville_id = ville_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongtitud() {
        return longtitud;
    }

    public void setLongtitud(Double longtitud) {
        this.longtitud = longtitud;
    }

    public boolean isLike_by_me() {
        return like_by_me;
    }

    public void setLike_by_me(boolean like_by_me) {
        this.like_by_me = like_by_me;
    }

    public boolean isVisit_by_me() {
        return visit_by_me;
    }

    public void setVisit_by_me(boolean visit_by_me) {
        this.visit_by_me = visit_by_me;
    }


    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", ville_id=" + ville_id +
                ", description='" + description + '\'' +
                ", user_id=" + user_id +
                ", adress='" + adress + '\'' +
                ", latitud=" + latitud +
                ", longtitud=" + longtitud +

                ", sync=" + sync+
                '}';
    }

    public ContentValues setContentValue(ContentValues c){
        c.put("user_id",this.user_id);
        c.put("nom",this.nom);
        c.put("ville_id",this.ville_id);
        c.put("description",this.description);
        c.put("adress",this.adress);
        c.put("lat",this.latitud);
        c.put("long",this.longtitud);
        c.put("sync",this.sync);

        return c;
    }

    public static ArrayList<Model> parseData(JsonArray datas){
        ArrayList<Model> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            JsonObject data = datas.get(i).getAsJsonObject();
            list.add(new Place(data.get("id").getAsInt(), data.get("nom").getAsString(),  data.get("ville_id").getAsInt(), data.get("description").getAsString(),  data.get("user_id").getAsInt(), data.get("adress").getAsString(), data.get("lat").getAsDouble(), data.get("long").getAsDouble(), data.get("sync").getAsInt()));
        }

        return list;

    }


    public String getTabeName(){
        return "places";
    }
}
