package com.YEF.yefApp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    EditText etEmail, etNumber, etName, etPassword, ConfirmPassword, otp;
    Button signup, generate, verify;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //  DatabaseReference dbsignup;
    String phnm;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_register, container, false);
        TextView login = v.findViewById(R.id.login);
        progressBar = v.findViewById(R.id.progressBar);
        etEmail = v.findViewById(R.id.etPasswordEmail);
        etNumber = v.findViewById(R.id.etNumber);
        etName = v.findViewById(R.id.etName);
        etPassword = v.findViewById(R.id.etPassword);
        ConfirmPassword = v.findViewById(R.id.ConfirmPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        signup = v.findViewById(R.id.signup);
        generate = v.findViewById(R.id.generate);
        signup.setEnabled(false);
//        signup.setBackgroundResource(R.drawable.grey);
        signup.setBackgroundColor(Color.parseColor("#878787"));

        Bundle bundle = this.getArguments();
        phnm = bundle.getString("number");
        String email = bundle.getString("email");
        String name = bundle.getString("name");
        String pswrd = bundle.getString("pswrd");
        String confrm = bundle.getString("confrm");
        final String ver = bundle.getString("v");
        etEmail.setText(email);
        etNumber.setText(phnm);
        etName.setText(name);
        etPassword.setText(pswrd);
        ConfirmPassword.setText(confrm);
        if (ver.equals("1")) {
            generate.setText("Verified");
            signup.setEnabled(true);
            generate.setBackgroundColor(Color.parseColor("#028E08"));
            generate.setTextColor(Color.WHITE);
            generate.setEnabled(false);
            signup.setBackgroundColor(Color.parseColor("#000102"));
//            signup.setBackgroundResource(R.drawable.button);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                LoginFragment l = new LoginFragment();
                ft.add(R.id.ma, l);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phnm = "+91" + etNumber.getText().toString().trim();
                //  PhoneAuthProvider.getInstance().verifyPhoneNumber(phnm,60, TimeUnit.SECONDS,getActivity(),mCallback);
                if (phnm.length() == 13) {
                    FragmentManager fm1 = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    OTPFragment l1 = new OTPFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("number", phnm);
                    arguments.putString("email", etEmail.getText().toString());
                    arguments.putString("name", etName.getText().toString());
                    arguments.putString("pswrd", etPassword.getText().toString());
                    arguments.putString("confrm", ConfirmPassword.getText().toString());
                    l1.setArguments(arguments);
                    ft1.add(R.id.ma, l1);
                    ft1.commit();
                } else {
                    Toast.makeText(getActivity(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Inflate the layout for this fragment
        return v;
    }


    private void signup() {
        final String email = etEmail.getText().toString().trim();
        final String number = etNumber.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        String confirmpassword = ConfirmPassword.getText().toString().trim();
        if ((TextUtils.isEmpty(email)) || TextUtils.isEmpty(password) || TextUtils.isEmpty(number)) {
            Toast.makeText(getActivity(), "You should enter all the fields properly", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password is too Short", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (password.equals(confirmpassword)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)

                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                //send verification link
                                FirebaseUser users = firebaseAuth.getCurrentUser();
                                users.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Full Name", name);
                                        user.put("Email Address", email);
                                        user.put("Mobile Number", number);
                                        user.put("Password", password);
                                        db.collection("users")
                                                .document(email)
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseAuth.getInstance().signOut();
                                                        AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                                                        ad.setTitle("Registration Done");
                                                        ad.setMessage("Please Verify your account using the verification link sent to your registered Email ID, else you won't be able to Login.");
                                                        ad.setIcon(R.drawable.ic_check_circle_black_24dp);
                                                        ad.setButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                            }
                                                        });
                                                        ad.show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Registration Failed. Please try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


//                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                                    @Override
//                                                    public void onSuccess(DocumentReference documentReference) {

//                                                        FirebaseAuth.getInstance().signOut();
//                                                        AlertDialog ad = new AlertDialog.Builder(getContext()).create();
//                                                        ad.setTitle("Registration Done");
//                                                        ad.setMessage("Please Verify your account using the verification link sent to your registered Email ID, else you won't be able to Login.");
//                                                        ad.setIcon(R.drawable.ic_check_circle_black_24dp);
//                                                        ad.setButton("OK", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                            }
//                                                        });
//                                                        ad.show();

//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Toast.makeText(getActivity(), "Registration Failed. Please try again", Toast.LENGTH_SHORT).show();
//
//                                                    }
//                                                });


                                        //startActivity(new Intent(getContext(),LoginFragment.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("tag", "OnFailure: EMail not sent" + e.getMessage());
                                    }
                                });


//                                    startActivity(new Intent(getContext(),RegisterFragment.class));
                                //Toast.makeText(getActivity(),"Registraion completed",Toast.LENGTH_LONG).show();
                                //return;
                            } else {
                                Toast.makeText(getActivity(), "Authentication Failed (Try with different Mail ID)", Toast.LENGTH_LONG).show();
                                return;
                            }

                            // ...
                        }
                    });


        } else {
            Toast.makeText(getActivity(), "Password Doesn't Match", Toast.LENGTH_LONG).show();
        }


    }
}










