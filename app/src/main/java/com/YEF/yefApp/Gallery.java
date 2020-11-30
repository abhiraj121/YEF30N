package com.YEF.yefApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Gallery extends AppCompatActivity {
   GridLayoutManager gridLayoutManager;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<Member, GalleryHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Member> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        gridLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView = findViewById(R.id.recycler_view);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Gallery");
        showData();
    }

    private void showData() {
        options = new FirebaseRecyclerOptions.Builder<Member>().setQuery(mDatabaseReference, Member.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Member, GalleryHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GalleryHolder holder, int position, @NonNull Member model) {
                holder.setDetails(getApplicationContext(), model.getImage());
            }

            @NonNull
            @Override
            public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.image,parent,false);
                GalleryHolder viewHolder= new GalleryHolder(itemView);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(Gallery.this, Gallery_image_completeView.class);
                        intent.putExtra("Imageid", getRef(position).getKey());
                        startActivity(intent);
                    Toast.makeText(Gallery.this,"hello",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    Toast.makeText(Gallery.this,"Long Click",Toast.LENGTH_SHORT);
                    }
                });
                return viewHolder;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    protected void onStart() {

        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}
