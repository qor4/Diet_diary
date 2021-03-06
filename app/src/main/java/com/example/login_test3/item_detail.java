package com.example.login_test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class item_detail extends AppCompatActivity {

    DatabaseReference mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String image = intent.getStringExtra("imageUrl");
        String uid = intent.getStringExtra("uid");
        String time = intent.getStringExtra("time");
        String date = intent.getStringExtra("date");
        String nikname = intent.getStringExtra("name");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String getuid = user.getUid();


        Log.v("test", "getuid" +getuid);
        Log.v("test", "uid" +uid);


        TextView detail_title = findViewById(R.id.detail_title);
        TextView detail_content = findViewById(R.id.detail_content);
        TextView detail_email= findViewById(R.id.email);
        TextView detail_date= findViewById(R.id.date);
        ImageView detail_image = findViewById(R.id.detail_image);
        Button detail_remove = findViewById(R.id.detail_remove);
        Button detail_modify = findViewById(R.id.detail_modify);
        Button detail_heart = findViewById(R.id.detail_heart);
        Button detail_heart_delete = findViewById(R.id.detail_heart_delete);


        mdatabase = FirebaseDatabase.getInstance().getReference().child("UserHeart");

        Glide.with(this).load(image).into(detail_image);
        detail_title.setText(title);
        detail_content.setText(content);
        detail_date.setText(date);
        detail_email.setText(nikname);

        detail_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post(title, content, date, time, image, nikname, uid);
                mdatabase.child(getuid).child(date+"_"+time+"_"+getuid).setValue(post);
                detail_heart.setVisibility(View.GONE);
                detail_heart_delete.setVisibility(View.VISIBLE);
            }
        });

        DatabaseReference mdatabase_del = FirebaseDatabase.getInstance().getReference().child("UserHeart").child(getuid).child(date+"_"+time+"_"+getuid);

        detail_heart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdatabase.removeValue();
                detail_heart_delete.setVisibility(View.GONE);
                detail_heart.setVisibility(View.VISIBLE);
            }
        });


        if (getuid.equals(uid)){
            detail_modify.setVisibility(View.VISIBLE);
            detail_remove.setVisibility(View.VISIBLE);
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            FirebaseStorage mStorage = FirebaseStorage.getInstance();

            detail_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.getReference().child("Post/"+date+"_"+time+"_"+uid).removeValue();
                    mDatabase.getReference().child("PostUser").child(uid+"/"+date+"_"+time).removeValue();
                    Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
            });
            detail_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), item_modify.class);
                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    intent.putExtra("uid", uid);
                    intent.putExtra("imageUrl", image);
                    intent.putExtra("time", time);
                    intent.putExtra("date", date);
                    intent.putExtra("name", nikname);
                    startActivity(intent);
                }
            });
        }


    }
}