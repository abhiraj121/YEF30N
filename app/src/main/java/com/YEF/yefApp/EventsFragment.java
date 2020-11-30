package com.YEF.yefApp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class EventsFragment extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView gallery;
    private ImageView webinar;
    private ImageView socialcause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);
        toolbar = findViewById(R.id.toolbar);
        gallery=findViewById(R.id.gallerypic);
        webinar=findViewById(R.id.webinarpic);
        socialcause=findViewById(R.id.socialcausepic);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(EventsFragment.this,Gallery.class);
                startActivity(intent5);
            }
        });
        webinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(EventsFragment.this,WebinarListActivity.class);
                startActivity(intent5);
            }
        });

        socialcause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(EventsFragment.this,Socialcause.class);
                startActivity(intent5);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            EventsFragment.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}