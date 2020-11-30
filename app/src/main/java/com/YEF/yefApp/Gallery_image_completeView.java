package com.YEF.yefApp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Gallery_image_completeView extends AppCompatActivity {
   ImageView imageView;
    private Toolbar toolbar;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_complete_view);
        imageView=findViewById(R.id.completeviewpic);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ImageView");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ref= FirebaseDatabase.getInstance().getReference().child("Gallery");
        String Imageid=getIntent().getStringExtra("Imageid");
        ref.child(Imageid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String image=snapshot.child("image").getValue().toString();
                    Glide.with(getApplicationContext())
                            .load(image).placeholder(R.mipmap.ic_launcher).fitCenter().centerCrop()
                            .into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            Gallery_image_completeView.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}