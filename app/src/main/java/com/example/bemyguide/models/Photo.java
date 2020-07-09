package com.example.bemyguide.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo extends Model implements Serializable {

    private int id;
    private int place_id;
    private String photo;
    private int sync;
    public Photo(){

    }

    public Photo(Cursor cursor){
        this.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        this.place_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("place_id")));
        this.photo = cursor.getString(cursor.getColumnIndex("lien"));
        this.sync = cursor.getInt(cursor.getColumnIndex("sync"));


    }

    public Photo(int id, int place_id, String photo,int sync) {
        this.id = id;
        this.place_id = place_id;
        this.photo = photo;
        this.sync = sync;
    }

    public Photo(int id, int place_id) {
        this.id = id;
        this.place_id = place_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ContentValues setContentValue(ContentValues c){

        c.put("lien",this.photo);
        c.put("place_id",this.place_id);
        c.put("sync",this.sync);

        return c;
    }

    public static ArrayList<Model> parseData(JsonArray datas){
        ArrayList<Model> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            JsonObject data = datas.get(i).getAsJsonObject();
            list.add(new Photo(data.get("id").getAsInt(), data.get("place_id").getAsInt(), data.get("lien").getAsString(), data.get("sync").getAsInt()));
        }

        return list;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", place_id=" + place_id +
                ", photo='" + photo + '\'' +
                ", sync=" + sync +
                '}';
    }

    public String getTabeName(){
        return "photos";
    }
}
