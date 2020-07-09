package com.example.bemyguide.controllers;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.util.Log;

import com.example.bemyguide.models.Comment;
import com.example.bemyguide.models.Model;
import com.example.bemyguide.models.Photo;
import com.example.bemyguide.models.Place;
import com.example.bemyguide.models.Reaction;
import com.example.bemyguide.models.User;
import com.example.bemyguide.models.Ville;

import java.util.ArrayList;
import java.util.List;

public class SqlManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bemyguide.db";
    private static final int SCHEMA = 1;
    private SQLiteDatabase mDB;

    public SqlManager(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
        this.mDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  comments ( id INTEGER PRIMARY KEY AUTOINCREMENT  , place_id bigint(20) , user_id bigint(20) ,text text , sync int );");
        db.execSQL("CREATE TABLE IF NOT EXISTS photos ( id INTEGER PRIMARY KEY AUTOINCREMENT , lien varchar(1), place_id bigint(20) , sync int);");
        db.execSQL("CREATE TABLE IF NOT EXISTS places ( id INTEGER PRIMARY KEY AUTOINCREMENT, nom varchar(191), ville_id bigint(20), description text , user_id bigint(20), adress varchar(191), lat double , long double , sync int);");
        db.execSQL("CREATE TABLE IF NOT EXISTS place_user ( id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL , place_id bigint(20) , user_id bigint(20), type varchar(191)  , sync int);");
        db.execSQL("CREATE TABLE IF NOT EXISTS users ( id INTEGER PRIMARY KEY AUTOINCREMENT   , name varchar(191) , email varchar(191) UNIQUE  , password varchar(191) , prenom varchar(191) , nom varchar(191) , avatar varchar(191) , sync int );");
        db.execSQL("CREATE TABLE IF NOT EXISTS villes ( id INTEGER PRIMARY KEY AUTOINCREMENT  , nom varchar(191) , photo varchar(191), description text , sync int );");
       // db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean resetAll(){

        SQLiteDatabase db = this.getWritableDatabase();

        String[] tables = {"comments","photos","places","users","villes","place_user"};
        for (String table: tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(db);
        return true;
    }

    public Model addModel(Model m){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues c = m.setContentValue(new ContentValues());
        // Inserting Row
        db.insert(m.getTabeName(), null, c);
        db.close();
        return m;
    }

    public Model editModel(Model m){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues c = m.setContentValue(new ContentValues());
        // update Row
        db.update(m.getTabeName(), c, "id" + " = ?", new String[]{String.valueOf(m.getId())});
        db.close();
        return m;
    }

    public void deletModel(Model m){
        SQLiteDatabase db = this.getWritableDatabase();
        // delet Row
        db.delete(m.getTabeName(), "id" + " = ?", new String[]{String.valueOf(m.getId())});
        db.close();
    }

    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                "id"
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = "email" + " = ? AND sync" + " <> ?";

        // selection argument
        String[] selectionArgs = {email,String.valueOf(2)};


        Cursor cursor = db.query("users", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                "id"
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = "email" + " = ?" + " AND " + "password" + " = ? AND sync " + " <> ? ";

        // selection arguments
        String[] selectionArgs = {email, password,String.valueOf(2)};

        Cursor cursor = db.query("users", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkPlace(double latitud, double longtitud) {

        // array of columns to fetch
        String[] columns = {
                "id"
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = "lat" + " = ?" + " AND " + "long" + " = ? AND sync " + " <> ?";

        // selection arguments
        String[] selectionArgs = {String.valueOf(latitud), String.valueOf(longtitud),String.valueOf(2)};

        Cursor cursor = db.query("places", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    public boolean checkPlace(String nom) {

        // array of columns to fetch
        String[] columns = {
                "id"
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = "nom" + " = ? AND sync " +  " <> ? " ;

        // selection arguments
        String[] selectionArgs = {nom,String.valueOf(2)};


        Cursor cursor = db.query("places", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkVille(String nom) {

        // array of columns to fetch
        String[] columns = {
                "id"
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = "nom" + " =  AND sync " + " <> ? " ;

        // selection arguments
        String[] selectionArgs = {nom,String.valueOf(2)};

        Cursor cursor = db.query("villes", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public User getUser(int id) {

        String[] columns = {"*"};

        String selection = "id" + " = ?  AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(id),String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("users", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        User user = null;

        if (cursor.moveToFirst()) {

                 user = new User(cursor);

        }
        cursor.close();
        db.close();

        // return user
        return user;
    }

    public User getProfile(String email) {

        String[] columns = { "*"};

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = "email" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {email,String.valueOf(2)};


        Cursor cursor = db.query("users", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order

        User user = null;

        if (cursor.moveToFirst()) {

            user = new User(cursor);
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));

            Log.e("inside", " inside" + cursor.getString(cursor.getColumnIndex("id")) );
        }


        cursor.close();
        db.close();
        return user;


    }

    public Comment getComment(int id) {

        String[] columns = {"*"};

        String selection = "id" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(id),String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("comments", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        Comment comment = null;

        if (cursor.moveToFirst()) {

            comment = new Comment(cursor);
            comment.setOwner(this.getUser(comment.getUser_id()));



        }
        cursor.close();
        db.close();

        // return user
        return comment;
    }

    public Ville getVille(int id, int log_id) {

        String[] columns = {"*"};

        String selection = "id" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(id),String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("villes", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        Ville ville = null;
        int likes,visits = 0;

        if (cursor.moveToFirst()) {

            ville = new Ville(cursor);
            Log.e("1city",  " city in "+ ville);
            List<Place> places = this.getPlaces(ville.getId(),log_id);
            ville.setPlaces(places.size());
             likes =0;visits = 0;
            for (Place place : places) {
                likes +=  place.getLikes();
                visits += place.getVisits();
            }
            ville.setLikes(likes);
            ville.setVisits(visits);
        }
        cursor.close();
        db.close();

        // return user
        return ville;
    }
    public Photo getPhoto(int place_id) {

        String[] columns = {"*"};

        String selection = "place_id" + " = ? AND sync " +  " <> ?";

        // selection argument
        String[] selectionArgs = {String.valueOf(place_id),String.valueOf(2)};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("photos", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        Photo photo = null;

        if (cursor.moveToFirst()) {

            photo = new Photo(cursor);

        }
        cursor.close();
        db.close();

        // return user
        return photo;
    }

    public Boolean checkReaction(int user_id, int place_id, String type) {

        String[] columns = {"*"};

        String selection = "user_id" + " = ?" + " AND " + "place_id" + " = ?" + " AND "  + "type" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(user_id),String.valueOf(place_id),type,String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("place_user", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public Reaction getReaction(int user_id, int place_id, String type) {

        String[] columns = {"*"};

        String selection = "user_id" + " = ?" + " AND " + "place_id" + " = ?" + " AND "  + "type" + " = ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(user_id),String.valueOf(place_id),type};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("place_user", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        Reaction reaction = null;
        if (cursor.moveToFirst()) {

            reaction = new Reaction(cursor);

        }

        cursor.close();
        db.close();

         return  reaction;
    }

    public Place getPlace(int id, int log_id) {

        String[] columns = {"*" };

        String selection = "id" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(id),String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("places", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        Place place = null;

        if (cursor.moveToFirst()) {

            place = new Place(cursor);
            place.setLikes(this.countReaction(place.getId(),"like"));
            place.setVisits(this.countReaction(place.getId(),"visit"));
            place.setComments(this.countComments(place.getId()));
            place.setLike_by_me(this.checkReaction(log_id,place.getId(),"like"));
            place.setVisit_by_me(this.checkReaction(log_id,place.getId(),"visit"));
            place.setOwner(this.getUser(place.getUser_id()));
            place.setPicture(this.getPhoto(place.getId()));

        }
        cursor.close();
        db.close();

        // return user
        return place;
    }

    public int countReaction(int place_id, String type) {

        String[] columns = {"id"};

        String selection = "place_id" + " = ?" + " AND "  + "type" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(place_id),type,String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("place_user", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order
        int val = cursor.getCount();
        cursor.close();
        db.close();
        return val;
    }

    public int countComments(int place_id) {

        String[] columns = {"id"};

        String selection = "place_id" + " = ? AND sync " +  " <> ? " ;

        // selection argument
        String[] selectionArgs = {String.valueOf(place_id),String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("comments", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        int val = cursor.getCount();
        cursor.close();
        db.close();
        return val;
    }


    public List<Comment> getComments(int place_id) {

        String[] columns = {"*"};

        String selection = "place_id" + " = ? AND sync " +  " <> ? " ;

        // selection argument
        String[] selectionArgs = {String.valueOf(place_id),String.valueOf(2)};
        String sortOrder =
                "id" + " ASC";
        List<Comment> comments = new ArrayList<Comment>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("comments", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        if (cursor.moveToFirst()) {
            do {
               Comment comment = new Comment(cursor);
                comment.setOwner(this.getUser(comment.getUser_id()));
                comments.add(comment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return user list
        return comments;
    }




    public List<Photo> getPhotos(int place_id) {

        String[] columns = {"*"};

        String selection = "place_id" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(place_id),String.valueOf(2)};
        String sortOrder =
                "id" + " ASC";
        List<Photo> photos = new ArrayList<Photo>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("photos", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        if (cursor.moveToFirst()) {
            do{
            photos.add(new Photo(cursor));
             } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user
        return photos;
    }

    public List<Place> getPlaces(int ville_id, int log_id) {

        String[] columns = {"*" };
        String selection = "ville_id" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(ville_id),String.valueOf(2)};
        String sortOrder =
                "id" + " ASC";
        List<Place> places = new ArrayList<Place>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("places", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        Log.e("curore",  "p "+cursor.getCount());


        if (cursor.moveToFirst()) {
            do{


                Place  place = new Place(cursor);
                place.setLikes(this.countReaction(place.getId(),"like"));
                place.setVisits(this.countReaction(place.getId(),"visit"));

               place.setComments(this.countComments(place.getId()));

                 place.setLike_by_me(this.checkReaction(log_id,place.getId(),"like"));
                place.setVisit_by_me(this.checkReaction(log_id,place.getId(),"visit"));

                place.setOwner(this.getUser(place.getUser_id()));

                place.setPicture(this.getPhoto(place.getId()));

                places.add(place);
            }while (cursor.moveToNext());


        }
        cursor.close();
        db.close();

        // return user
        return places;
    }

    public List<Place> getMyPlaces(int log_id) {

        String[] columns = {"*" };

        String selection = "user_id" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(log_id),String.valueOf(2)};
        String sortOrder =
                "id" + " ASC";
        List<Place> places = new ArrayList<Place>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("places", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order



        if (cursor.moveToFirst()) {
            do{
                Place  place = new Place(cursor);
                place.setLikes(this.countReaction(place.getId(),"like"));
                place.setVisits(this.countReaction(place.getId(),"visit"));
                place.setComments(this.countComments(place.getId()));
                place.setLike_by_me(this.checkReaction(log_id,place.getId(),"like"));
                place.setVisit_by_me(this.checkReaction(log_id,place.getId(),"visit"));
                place.setOwner(this.getUser(place.getUser_id()));
                place.setPicture(this.getPhoto(place.getId()));
                places.add(place);
            }while (cursor.moveToNext());


        }
        cursor.close();
        db.close();

        // return user
        return places;
    }


    public List<Place> getAllPlaces(int log_id) {

        String[] columns = {"*" };

        String selection = "sync " +  " <> ? ";
        String[] selectionArgs = {String.valueOf(2)};

        String sortOrder =
                "id" + " ASC";
        List<Place> places = new ArrayList<Place>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("places", //Table to query
                columns,
                selection,
                selectionArgs,
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order



        if (cursor.moveToFirst()) {
            do{
                Place  place = new Place(cursor);
                place.setLikes(this.countReaction(place.getId(),"like"));
                place.setVisits(this.countReaction(place.getId(),"visit"));
                place.setComments(this.countComments(place.getId()));
                place.setLike_by_me(this.checkReaction(log_id,place.getId(),"like"));
                place.setVisit_by_me(this.checkReaction(log_id,place.getId(),"visit"));
                place.setOwner(this.getUser(place.getUser_id()));
                place.setPicture(this.getPhoto(place.getId()));
                places.add(place);
            }while (cursor.moveToNext());


        }
        cursor.close();
        db.close();

        // return user
        return places;
    }


    public List<Ville> getVilles( int log_id) {

        String[] columns = {"*"};

        String selection = "sync " +  " <> ? ";
        String[] selectionArgs = {String.valueOf(2)};

        String sortOrder =
                "id" + " ASC";
        List<Ville> villes = new ArrayList<Ville>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("villes", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        int likes,visits = 0;


        if (cursor.moveToFirst()) {
            do{

                Ville ville = new Ville(cursor);
                Log.e("curore",  "v "+ville.toString());
               List<Place> places = this.getPlaces(ville.getId(),log_id);
                Log.e("curore",  "v "+places.toString());
                ville.setPlaces(places.size());
                likes =0;visits = 0;
                for (Place place : places) {
                    likes +=  place.getLikes();
                    visits += place.getVisits();
                }
                ville.setLikes(likes);
                ville.setVisits(visits);
                villes.add(ville);
            }while (cursor.moveToNext());

        }
        cursor.close();
        db.close();

        // return user
        return villes;



    }

    public List<Model> getNonSyncro(String tabnName) {

        String[] columns = {"*"};

        String selection = "sync" + " <> ?";

        // selection argument
        String[] selectionArgs = {String.valueOf(0)};

        String sortOrder =
                "id" + " ASC";
        List<Model> objs = new ArrayList<Model>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tabnName, //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        if (cursor.moveToFirst()) {


            do{
                switch (tabnName){

                    case "users" : objs.add(new User(cursor)); break;
                    case "villes" : objs.add(new Ville(cursor)); break;
                    case "places" : objs.add(new Place(cursor)); break;
                    case "photos" : objs.add(new Photo(cursor)); break;
                    case "comments" : objs.add(new Comment(cursor)); break;
                    case "place_user" : objs.add(new Reaction(cursor)); break;
                }
            }while (cursor.moveToNext());

        }
        cursor.close();
        db.close();

        // return model
        return objs;



    }


    public String countUserComments(int user_id) {

        String[] columns = {"id"};

        String selection = "user_id" + " = ? AND sync " +  " <> ? " ;

        // selection argument
        String[] selectionArgs = {String.valueOf(user_id),String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("comments", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        int var = cursor.getCount();
        cursor.close();
        db.close();
        return String.valueOf(var);
        // return user list
    }

    public String countUserReaction(int user_id, String type) {

        String[] columns = {"id"};

        String selection = "user_id" + " = ?" + " AND "  + "type" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(user_id),type,String.valueOf(2)};


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("place_user", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order
        int var = cursor.getCount();
        cursor.close();
        db.close();
        return String.valueOf(var);
    }


    public String countMyPlaces(int log_id) {

        String[] columns = {"id" };

        String selection = "user_id" + " = ? AND sync " +  " <> ? ";

        // selection argument
        String[] selectionArgs = {String.valueOf(log_id),String.valueOf(2)};
        String sortOrder =
                "id" + " ASC";


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("places", //Table to query
                columns,    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order



        int var = cursor.getCount();
        cursor.close();
        db.close();
        return String.valueOf(var);
    }










}