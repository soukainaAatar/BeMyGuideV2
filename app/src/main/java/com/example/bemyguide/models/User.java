package com.example.bemyguide.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

public class User extends Model implements Serializable {

    private int id;
    private String name;
    private String email;
    private String password;
    private String prenom ;
    private String nom ;
    private String avatar ;
    private int sync;
    public User(){

    }

    public User(Cursor cursor){
        this.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
        this.nom = cursor.getString(cursor.getColumnIndex("nom"));
        this.name = cursor.getString(cursor.getColumnIndex("name"));
        this.email = cursor.getString(cursor.getColumnIndex("email"));
        this.prenom = cursor.getString(cursor.getColumnIndex("prenom"));
        this.avatar = cursor.getString(cursor.getColumnIndex("avatar"));
        this.sync = cursor.getInt(cursor.getColumnIndex("sync"));

    }

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User( String name, String email, String password, String prenom, String nom,int sync) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.prenom = prenom;
        this.nom = nom;
        this.sync = sync;
    }

    public User(int id, String name, String email, String password, String prenom, String nom, String avatar, int sync) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.prenom = prenom;
        this.nom = nom;
        this.sync = sync;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }



    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sync=" + sync +
                '}';
    }

    public ContentValues setContentValue(ContentValues c){
        c.put("name",this.name);
        c.put("nom",this.nom);
        c.put("email",this.email);
        c.put("prenom",this.prenom);
        c.put("avatar",this.avatar);
        c.put("password",this.password);
        c.put("sync",this.sync);

        return c;
    }

    public static ArrayList<Model> parseData(JsonArray datas){
        ArrayList<Model> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            JsonObject data = datas.get(i).getAsJsonObject();
            list.add(new User(data.get("id").getAsInt(), data.get("name").getAsString(),  data.get("email").getAsString(), data.get("password").getAsString(),  data.get("prenom").getAsString(), data.get("nom").getAsString(), data.get("avatar").getAsString(), data.get("sync").getAsInt()));
        }

        return list;


    }

    public String getTabeName(){
        return "users";
    }
}
