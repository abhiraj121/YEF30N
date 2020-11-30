package com.YEF.yefApp;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

public class Myprofile extends NavigationbarActivity {
   // DrawerLayout drawer=findViewById(R.id.fbicon);
   //DrawerLayout drawer = findViewById(R.id.fbicon);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        DrawerLayout drawer=findViewById(R.id.fbicon);
    }
}