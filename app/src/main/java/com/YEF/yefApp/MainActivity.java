package com.YEF.yefApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DatabaseReference dbsignup;
    private static final int SPLASH_SCREEN = 5000;
    //variables
    DrawerLayout drawer;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView youth, empowerment, foundation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isNetworkStatusAvialable(this)) {

            setContentView(R.layout.nav_activity_main);
//            final Button button=findViewById(R.id.button);

            dbsignup = FirebaseDatabase.getInstance().getReference("signups");
            drawer = findViewById(R.id.fbicon);
            NavigationView nav = findViewById(R.id.nav_view);
            final Menu nav_Menu = nav.getMenu();
            FirebaseAuth firebaseAuth;
            FirebaseAuth.AuthStateListener mAuthListener;
            firebaseAuth = FirebaseAuth.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null || (!user.isEmailVerified())) {
                nav_Menu.findItem(R.id.nav_profile).setVisible(false);
                nav_Menu.findItem(R.id.nav_applications).setVisible(false);
                nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            } else if (user != null && user.isEmailVerified()) {
                nav_Menu.findItem(R.id.nav_login).setVisible(false);
                nav_Menu.findItem(R.id.nav_profile).setVisible(true);
                nav_Menu.findItem(R.id.nav_applications).setVisible(true);
                nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            }


            //Animations
            topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
            bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

            image = findViewById(R.id.imageView);
            youth = findViewById(R.id.textView);
            empowerment = findViewById(R.id.textView2);
            foundation = findViewById(R.id.textView3);
            ImageButton fb = findViewById(R.id.fb);
            ImageButton insta = findViewById(R.id.insta);
            ImageButton linkedin = findViewById(R.id.linkedin);
            ImageButton website = findViewById(R.id.yefnav);
            ImageButton utube = findViewById(R.id.youtube);
            image.setAnimation(bottomAnim);
            youth.setAnimation(bottomAnim);
            empowerment.setAnimation(bottomAnim);
            final AppBarLayout temp = findViewById(R.id.toolbarMain);
            final Toolbar temp2 = findViewById(R.id.app_bar);
            foundation.setAnimation(bottomAnim);


//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//            getSupportActionBar().setHomeButtonEnabled(false);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, temp2, R.string.open, R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            temp2.getNavigationIcon().setTint(Color.parseColor("#00000000"));

            nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_login:
//                            toolbarMain
//                            temp.setVisibility(View.GONE);
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            LoginFragment l = new LoginFragment();
                            ft.replace(R.id.ma, l);
                            ft.addToBackStack(null);
                            ft.commit();
                            drawer.closeDrawer(Gravity.LEFT);
                            break;
                        case R.id.nav_profile:
                            Intent intent9 = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent9);
                            break;
                        case R.id.nav_internship:
//                            temp.setVisibility(View.GONE);
                            FragmentManager fm4 = getSupportFragmentManager();
                            FragmentTransaction ft4 = fm4.beginTransaction();
                            InternshipOpportunity l4 = new InternshipOpportunity();
                            ft4.replace(R.id.ma, l4);
                            ft4.addToBackStack(null);
                            ft4.commit();
                            drawer.closeDrawer(Gravity.LEFT);
                            break;
                        case R.id.nav_applications:
//                            temp.setVisibility(View.GONE);
                            FragmentManager fm5 = getSupportFragmentManager();
                            FragmentTransaction ft5 = fm5.beginTransaction();
                            AppliedInternships l5 = new AppliedInternships();
                            ft5.replace(R.id.ma, l5);
                            ft5.addToBackStack(null);
                            ft5.commit();
                            drawer.closeDrawer(Gravity.LEFT);
                            break;
                        case R.id.nav_aboutus:
//                            temp.setVisibility(View.GONE);
                            FragmentManager fm1 = getSupportFragmentManager();
                            FragmentTransaction ft1 = fm1.beginTransaction();
                            AboutUsFragment l1 = new AboutUsFragment();
                            ft1.replace(R.id.ma, l1);
                            ft1.addToBackStack(null);
                            ft1.commit();
                            drawer.closeDrawer(Gravity.LEFT);
                            break;
                        case R.id.nav_aboutfounder:
//                            temp.setVisibility(View.GONE);
                            FragmentManager fm2 = getSupportFragmentManager();
                            FragmentTransaction ft2 = fm2.beginTransaction();
                            FounderFragment l2 = new FounderFragment();
                            ft2.replace(R.id.ma, l2);
                            ft2.addToBackStack(null);
                            ft2.commit();
                            drawer.closeDrawer(Gravity.LEFT);
                            break;
                        case R.id.nav_events:
                            Intent intent7 = new Intent(MainActivity.this, EventsFragment.class);
                            startActivity(intent7);
                            break;
                        case R.id.nav_donation:
//                            temp.setVisibility(View.GONE);
                            String link = "https://pages.razorpay.com/DailyWageWorkers";
//                            String link = "https://pages.razorpay.com/pl_EpJyCZ6weWUoJq/view";
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            startActivity(browserIntent);
//                            FragmentManager fm6 = getSupportFragmentManager();
//                            FragmentTransaction ft6 = fm6.beginTransaction();
//                            DonationsFragment l6 = new DonationsFragment();
//                            ft6.replace(R.id.ma, l6);
//                            ft6.addToBackStack(null);
//                            ft6.commit();
                            drawer.closeDrawer(Gravity.LEFT);
                            break;
                        case R.id.nav_contactus:
//                            temp.setVisibility(View.GONE);
                            FragmentManager fm3 = getSupportFragmentManager();
                            FragmentTransaction ft3 = fm3.beginTransaction();
                            ContactUs l3 = new ContactUs();
                            ft3.replace(R.id.ma, l3);
                            ft3.addToBackStack(null);
                            ft3.commit();
                            drawer.closeDrawer(Gravity.LEFT);
                            break;
                        case R.id.nav_locateus:
                            float latitude = 28.688768f;
                            float longitude = 77.1303528f;
                            String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
//                            String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + "(" + "YEF" + ")";
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                    strUri
                            ));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Only if initiating from a Broadcast Receiver
                            String mapsPackageName = "com.google.android.apps.maps";
                            if (isPackageExisted(getApplicationContext(), mapsPackageName)) {
                                i.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity");
                                i.setPackage(mapsPackageName);
                            }
                            startActivity(i);
                            break;
                        case R.id.nav_logout:
                            logout();
                            break;
                    }
                    return true;
                }
            });

            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.facebook.com/yefindia.org";
                    Intent p = new Intent(Intent.ACTION_VIEW);
                    p.setData(Uri.parse(url));
                    startActivity(p);

                }
            });
            insta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.instagram.com/yef_india";
                    Intent p = new Intent(Intent.ACTION_VIEW);
                    p.setData(Uri.parse(url));
                    startActivity(p);

                }
            });
            linkedin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.linkedin.com/company/yefindiango/";
                    Intent p = new Intent(Intent.ACTION_VIEW);
                    p.setData(Uri.parse(url));
                    startActivity(p);

                }
            });
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.yefindia.org";
                    Intent p = new Intent(Intent.ACTION_VIEW);
                    p.setData(Uri.parse(url));
                    startActivity(p);

                }
            });
            utube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.youtube.com/channel/UCjThVKmNlxdGKZ37dS6PmVQ/";
                    Intent p = new Intent(Intent.ACTION_VIEW);
                    p.setData(Uri.parse(url));
                    startActivity(p);
                }
            });
            if (user == null) {
                new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        drawer.openDrawer(Gravity.LEFT);
                    }
                }.start();
            } else if (user != null && user.isEmailVerified()) {
                new CountDownTimer(4000, 1000) {


                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        openapp(temp);
                    }
                }.start();
            }
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);//(getContext()).create();
            ad.setTitle(" No Internet");
            ad.setMessage(" Connect your device to an active internet connection and Restart the app");
            ad.setIcon(R.drawable.ic_error_black_24dp);
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            ad.show();
        }
    }

    private boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null) {
                return netInfos.isConnected();
            }
        }
        return false;
    }

    private void openapp(AppBarLayout temp) {
//        temp.setVisibility(View.GONE);
        FragmentManager fm11 = getSupportFragmentManager();
        FragmentTransaction ft11 = fm11.beginTransaction();
        AppliedInternships l11 = new AppliedInternships();
        ft11.replace(R.id.ma, l11);
        ft11.addToBackStack(null);
        ft11.commitAllowingStateLoss();

    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);//(getContext()).create();
        ad.setTitle(" LogOut");
        ad.setMessage("     Do you want to logout?");
        ad.setIcon(R.drawable.ic_do_not_disturb_on_black_24dp);
        ad.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent8 = new Intent(MainActivity.this, MainActivity.class);
                intent8.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent8.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent8);
            }
        });
        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        ad.show();
    }

    private static boolean isPackageExisted(Context context, String targetPackage) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

}


