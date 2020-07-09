package com.example.bemyguide.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Comment extends Model implements Serializable {

    private int id;
    private int user_id;
    private int place_id;
    private String text;
    private User owner;



    private int sync;

    public Comment(){

    }

    public Comment(Cursor cursor){
        this.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        this.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id")));
        this.place_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("place_id")));
        this.text = cursor.getString(cursor.getColumnIndex("text"));
        this.sync = cursor.getInt(cursor.getColumnIndex("sync"));

    }

    public Comment(int id, int user_id, int place_id, String text,int sync) {
        this.id = id;
        this.user_id = user_id;
        this.place_id = place_id;
        this.text = text;
        this.sync = sync;
    }

    public Comment(int id, int user_id, int place_id,int sync) {
        this.id = id;
        this.user_id = user_id;
        this.place_id = place_id;
        this.sync=sync;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", place_id=" + place_id +
                ", text='" + text + '\'' +
                ", sync=" + sync +
                '}';
    }

    public ContentValues setContentValue(ContentValues c){
        c.put("user_id",this.user_id);
        c.put("place_id",this.place_id);
        c.put("text",this.text);
        c.put("sync",this.sync);
        return c;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public String getTabeName(){
        return "comments";
    }

    public User getOwner() {
        return owner;
    }

    public static ArrayList<Model> parseData(JsonArray datas){
        ArrayList<Model> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            JsonObject data = datas.get(i).getAsJsonObject();
            list.add(new Comment(data.get("id").getAsInt(),  data.get("user_id").getAsInt(), data.get("place_id").getAsInt(), data.get("text").getAsString(), data.get("sync").getAsInt()));
        }

        return list;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
