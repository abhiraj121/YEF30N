package com.YEF.yefApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    EditText editText1, editText2;
    Button button;
    TextView textView1, textView2, forgot_password;
    ImageView imageView1, imageView2;
    Intent i;
    private FirebaseAuth firebaseAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        button = view.findViewById(R.id.btnPasswordReset);
        i = new Intent(getActivity(), forgot_password.class);
        forgot_password = view.findViewById(R.id.txtForgotPassword);
        editText1 = view.findViewById(R.id.etPasswordEmail);
        editText2 = view.findViewById(R.id.etPassword);
        textView1 = view.findViewById(R.id.txtForgotPassword);
        textView2 = view.findViewById(R.id.txtSignUp);
        final DrawerLayout drawer = getActivity().findViewById(R.id.fbicon);
        NavigationView nav = getActivity().findViewById(R.id.nav_view);
        final Menu nav_Menu = nav.getMenu();
        firebaseAuth = FirebaseAuth.getInstance();
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                RegisterFragment l = new RegisterFragment();
                Bundle arguments = new Bundle();
                arguments.putString("number", "");
                arguments.putString("email", "");
                arguments.putString("name", "");
                arguments.putString("pswrd", "");
                arguments.putString("confrm", "");
                arguments.putString("v", "");
                l.setArguments(arguments);
                ft.add(R.id.ma, l);
                ft.addToBackStack(null);
                ft.commit();
            }

        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(i);


            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                String email = editText1.getText().toString().trim();
                String password = editText2.getText().toString().trim();
                if ((TextUtils.isEmpty(email)) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "You should enter all the fields properly", Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final FirebaseUser users = firebaseAuth.getCurrentUser();
                                    if (users.isEmailVerified()) {
                                        //startActivity(new Intent(getContext(), MainActivity2.class));
                                        final AlertDialog.Builder ad = new AlertDialog.Builder(getContext());//(getContext()).create();
                                        ad.setTitle("You are logged in");
                                        ad.setMessage("     Update your profile");
                                        ad.setIcon(R.drawable.ic_check_circle_black_24dp);
                                        ad.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent2 = new Intent(getContext(), ProfileActivity.class);
                                                startActivity(intent2);


                                            }
                                        });
                                        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                drawer.openDrawer(Gravity.LEFT);

                                            }
                                        });
                                        ad.show();

                                        nav_Menu.findItem(R.id.nav_login).setVisible(false);
                                        nav_Menu.findItem(R.id.nav_profile).setVisible(true);
                                        nav_Menu.findItem(R.id.nav_applications).setVisible(true);
                                        nav_Menu.findItem(R.id.nav_logout).setVisible(true);


                                    } else {
                                        final AlertDialog.Builder ad = new AlertDialog.Builder(getContext());//(getContext()).create();
                                        ad.setTitle("Account Not Verified");
                                        ad.setMessage("Please Verify your account using the verification link sent to your registered Email ID. Click on Resend Verification Link to get a new link.");
                                        ad.setIcon(R.drawable.ic_error_black_24dp);
                                        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        ad.setNegativeButton("Resend Verification Link", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                users.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {

                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity(), "Verification Link Sent", Toast.LENGTH_LONG).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Verification Link not Sent. Try Again!!", Toast.LENGTH_LONG).show();

                                                    }
                                                });
                                            }
                                        });
                                        ad.show();
                                        Toast.makeText(getActivity(), "Your account is not Verified.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                                //

                            }
                        });
            }
        });


        // Inflate the layout for this fragment

        return view;
    }
}




