package com.example.bemyguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.bemyguide.Profile;
import com.example.bemyguide.R;
import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.models.Comment;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {
    Context context;
    private SqlManager myDb;
    public SessionManager mysession;
    List<Comment> comments;
    ListView comment_list;
    public CommentAdapter(Context context, int layoutToBeInflated, List<Comment> comments, ListView  comment_list) {
        super(context, layoutToBeInflated, comments);
        this.context = context;

        this.comments = comments;
        this.myDb =  new SqlManager(context);
        this.comment_list = comment_list;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.comments_row, null);

        TextView c_userText = row.findViewById(R.id.c_userText);
        TextView c_commentText =  row.findViewById(R.id.c_commentText);

        c_userText.setText(comments.get(position).getOwner().getNom()+" "+ comments.get(position).getOwner().getPrenom());

        c_userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(context.getApplicationContext(), Profile.class);
                user.putExtra(Profile.USER_TYPE,"user" );
                user.putExtra("user_id",comments.get(position).getUser_id());
                context.startActivity(user);
            }
        });
        c_commentText.setText(comments.get(position).getText());
        c_commentText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setTitle("supprimer commentaire");
                dialog.setMessage("voulez vous supprimer ce commentaire");
                dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Oui", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        comments.get(position).setSync(2);
                        myDb.editModel(comments.get(position));
                        comments.remove(position);
                        dialog.dismiss();
                        comment_list.invalidateViews();


                    }
                });

                dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Non", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dialog.dismiss();
                        comment_list.invalidateViews();
                    }
                });
                dialog.show();
                return true;
            }
        });

        return (row);
    }

}
