package com.YEF.yefApp;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.os.PersistableBundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class Socialcause extends AppCompatActivity {
    LinearLayoutManager mLinearLayoutManager;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<Member, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Member> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socialcause);
        mLinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView = findViewById(R.id.recycler_view);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Data");
        showData();
    }

    private void showData() {
        options = new FirebaseRecyclerOptions.Builder<Member>().setQuery(mDatabaseReference, Member.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Member, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {
                holder.setDetails(getApplicationContext(), model.getTitle(), model.getImage(), model.getCity(), model.getStory());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.socialcause_view,parent,false);
                ViewHolder viewHolder=new ViewHolder(itemView);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(Socialcause.this,"hello",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(Socialcause.this,"Long Click",Toast.LENGTH_SHORT);
                    }
                });
                return viewHolder;
            }
        };
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
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
