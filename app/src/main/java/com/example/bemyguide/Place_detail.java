package com.example.bemyguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bemyguide.adapters.CommentAdapter;
import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.controllers.SyncroTask;
import com.example.bemyguide.models.Comment;
import com.example.bemyguide.models.Photo;
import com.example.bemyguide.models.Place;
import com.example.bemyguide.models.Reaction;

import java.util.ArrayList;
import java.util.List;

public class Place_detail extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater m = new MenuInflater(this);
        m.inflate(R.menu.share_menu,menu);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.m_logout ){
            Intent login = new Intent(getApplicationContext(),Login.class);
            login.putExtra(Login.LOGIN_TYPE,"logout" );
            startActivity(login);
        }

        if(item.getItemId() == R.id.m_syncro ){
            SyncroTask sc= new SyncroTask(getApplicationContext());
            sc.execute(this.getApplicationContext().getString(R.string.url_all));
        }

        if(item.getItemId() == R.id.m_profile ){
            Intent profile = new Intent(getApplicationContext(), Profile.class);
            profile.putExtra(Profile.USER_TYPE,"profile" );
            startActivity(profile);
        }
        return super.onOptionsItemSelected(item);
    }
    TextView placeText;
    TextView p_userText ;
    TextView p_description;
    TextView p_VilleText ;
    TextView p_visitsCount ;
    Button p_visits ;
    TextView p_likesCount ;
    Button p_likes ;
    TextView p_addressText ;
    //TextView my_userText ;
    //EditText my_commentText ;
    Button p_addressButton ;
    Button save_comment ;
    int id;

    private ListView comment_list;
    private ViewGroup photo_list;
    private SqlManager myDb;
    public SessionManager mysession;
    List<Comment> comments;
    List<Photo> photos;
    Comment myComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        placeText = findViewById(R.id.placeText);
         p_userText = findViewById(R.id.p_userText);
         p_description =  findViewById(R.id.p_description);
         p_VilleText =  findViewById(R.id.p_VilleText);
         p_visitsCount =  findViewById(R.id.p_visitsCount);
         p_visits =  findViewById(R.id.p_visits);
         p_likesCount =  findViewById(R.id.p_likesCount);
         p_likes =  findViewById(R.id.p_likes);
         p_addressText =  findViewById(R.id.p_addressText);
         p_addressButton =  findViewById(R.id.p_addressButton);
      /*   my_userText = findViewById(R.id.my_userText);
        my_commentText = findViewById(R.id.my_commentText);*/
        save_comment = findViewById(R.id.save_comment);

        myDb = new SqlManager(getApplicationContext());
        mysession = new SessionManager(getApplicationContext());

       // my_userText.setText(mysession.getUserEmail());


        Intent in = getIntent();
        id = in.getIntExtra("place_id",0);



        final Place place = myDb.getPlace(id,mysession.getUserId());

        placeText.setText(place.getNom());
        p_addressText.setText(place.getAdress());
        p_userText.setText(place.getOwner().getNom() +" "+ place.getOwner().getPrenom());
        p_description.setText(place.getDescription());
        p_VilleText.setText(myDb.getVille(place.getVille_id(),mysession.getUserId()).getNom());

        p_visitsCount.setText(place.getVisits()+"V");
        p_likesCount.setText(place.getLikes()+"L");

        if(place.isVisit_by_me()){

            p_visits.setBackgroundResource(android.R.drawable.checkbox_on_background);
        }else{
            p_visits.setBackgroundResource(android.R.drawable.checkbox_off_background);
        }
        if(place.isLike_by_me()){
            p_likes.setBackgroundResource(R.drawable.like_on);
        }else{
            p_likes.setBackgroundResource(R.drawable.like_off);
        }

        p_visits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reaction r = myDb.getReaction(mysession.getUserId(), place.getId(), "visit");
                if (r == null) {
                    r = new Reaction(mysession.getUserId(), place.getId(), "visit", 1);
                    myDb.addModel(r);
                    place.setVisit_by_me(true);
                    place.setVisits(place.getVisits()+1);
                    p_visitsCount.setText(place.getVisits()+"V");
                    v.setBackgroundResource(android.R.drawable.checkbox_on_background);

                } else {
                    if (r.getSync() == 2) {
                        r.setSync(1);
                        myDb.editModel(r);
                        place.setVisit_by_me(true);
                        place.setVisits(place.getVisits()+1);
                        p_visitsCount.setText(place.getVisits()+"V");

                        v.setBackgroundResource(android.R.drawable.checkbox_on_background);

                    }else{
                        r.setSync(2);
                        myDb.editModel(r);
                        place.setVisit_by_me(false);
                        place.setVisits(place.getVisits()-1);
                        p_visitsCount.setText(place.getVisits()+"V");

                        v.setBackgroundResource(android.R.drawable.checkbox_off_background);

                    }
                }

            }
        });

        p_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reaction r = myDb.getReaction(mysession.getUserId(), place.getId(), "like");
                if (r == null) {
                    r = new Reaction(mysession.getUserId(), place.getId(), "like", 1);
                    myDb.addModel(r);
                    place.setLike_by_me(true);
                    place.setLikes(place.getLikes()+1);
                    p_likesCount.setText(place.getLikes()+"L");
                    v.setBackgroundResource(R.drawable.like_on);

                } else {
                    if (r.getSync() == 2) {
                        r.setSync(1);
                        myDb.editModel(r);
                        place.setLike_by_me(true);
                        place.setLikes(place.getLikes()+1);
                        p_likesCount.setText(place.getLikes()+"L");
                        v.setBackgroundResource(R.drawable.like_on);

                    }else{
                        r.setSync(2);
                        myDb.editModel(r);
                        place.setLike_by_me(false);
                        place.setLikes(place.getLikes()-1);
                        p_likesCount.setText(place.getLikes()+"L");
                        v.setBackgroundResource(R.drawable.like_off);
                    }
                }

            }
        });



        p_userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(getApplicationContext(), Profile.class);
                user.putExtra(Profile.USER_TYPE,"user" );
                user.putExtra("user_id",place.getUser_id());
                startActivity(user);
            }
        });
        p_addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent map = new Intent(getApplicationContext(),MyMap.class);
                map.putExtra(MyMap.MAP_TYPE,"one");
                map.putExtra("place_id",place.getId());
                startActivity(map);
            }
        });

        comments = myDb.getComments(id);

        comment_list = findViewById(R.id.p_commentList);
      final   CommentAdapter commentAdapter = new CommentAdapter(this,R.layout.comments_row,comments,comment_list);
        comment_list.setAdapter(commentAdapter);

        photos = myDb.getPhotos(id);

        photo_list = findViewById(R.id.p_images_scroll);
        int [] icons = {R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4, R.id.icon5};
        ArrayList<ImageView> imageViews = new ArrayList<>();


        for (int i = 0 ; i< icons.length;i++){
            final int pos = i;
             findViewById(icons[i]).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     final AlertDialog dialog = new AlertDialog.Builder(Place_detail.this).create();
                     dialog.setTitle("Agrandir");
                     final ImageView img = new ImageView(getApplicationContext());
                     img.setBackgroundResource(Place_detail.this.getResources().getIdentifier(photos.get(pos).getPhoto().toLowerCase(), "drawable", Place_detail.this.getPackageName()));
                     LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                             LinearLayout.LayoutParams.MATCH_PARENT,
                             LinearLayout.LayoutParams.MATCH_PARENT);
                     img.setLayoutParams(lp);
                     dialog.setView(img); // uncomment this line


                     dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"fermer", new DialogInterface.OnClickListener() {

                         @Override
                         public void onClick(DialogInterface arg0, int arg1) {
                             dialog.dismiss();

                         }
                     });
                     dialog.show();
                 }
             });
            findViewById(icons[i]).setBackgroundResource(this.getResources().getIdentifier(photos.get(i).getPhoto().toLowerCase(), "drawable", this.getPackageName()));
        }


        save_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog dialog = new AlertDialog.Builder(Place_detail.this).create();
                dialog.setTitle("ajouter commentaire");
                final EditText input = new EditText(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                dialog.setView(input); // uncomment this line
                dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Oui", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        myComment = new Comment();
                        myComment.setUser_id(mysession.getUserId());
                        myComment.setOwner(myDb.getUser(mysession.getUserId()));
                        myComment.setPlace_id(id);
                        myComment.setText(input.getText().toString());
                        myComment.setSync(1);

                        myDb.addModel(myComment);
                        comments.add(myComment);
                        commentAdapter.notifyDataSetChanged();
                    }
                });

                dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"annuler", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dialog.dismiss();

                    }
                });
                dialog.show();



            }
        });






    }
}
