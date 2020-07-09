package com.example.bemyguide.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Reaction extends Model implements Serializable {

    private int id;
    private int user_id;
    private int place_id;
    private String type;
    private int sync;
    public Reaction(){

    }

    public Reaction(Cursor cursor){
        this.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        this.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id")));
        this.place_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("place_id")));
        this.type = cursor.getString(cursor.getColumnIndex("type"));
        this.sync = cursor.getInt(cursor.getColumnIndex("sync"));

    }

    public Reaction( int user_id, int place_id, String type,int sync) {
        this.user_id = user_id;
        this.place_id = place_id;
        this.type = type;
        this.sync = sync;
    }


    public Reaction(int id, int user_id, int place_id, String type,int sync) {
        this.id = id;
        this.user_id = user_id;
        this.place_id = place_id;
        this.type = type;
        this.sync = sync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    @Override
    public String toString() {
        return "Reaction{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", place_id=" + place_id +
                ", type='" + type + '\'' +
                ", sync=" + sync +
                '}';
    }

    public ContentValues setContentValue(ContentValues c){
        c.put("user_id",this.user_id);
        c.put("place_id",this.place_id);
        c.put("type",this.type);
        c.put("sync",this.sync);
        return c;
    }

    public static ArrayList<Model> parseData(JsonArray datas){
        ArrayList<Model> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            JsonObject data = datas.get(i).getAsJsonObject();
            list.add(new Reaction(data.get("id").getAsInt(),  data.get("user_id").getAsInt(), data.get("place_id").getAsInt(), data.get("type").getAsString(), data.get("sync").getAsInt()));
        }

        return list;
    }
    public String getTabeName(){
        return "place_user";
    }
}
