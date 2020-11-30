package com.YEF.yefApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class OTPFragment extends Fragment {

    public OTPFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth firebaseAuth;
    String phnm, verificationCode = "0", email, name, pswrd, confrm;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String v = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_o_t_p, container, false);
        Button verify = v.findViewById(R.id.verify_btn);
        final EditText otp = v.findViewById(R.id.otp_text_view);
        Bundle bundle = this.getArguments();
        phnm = bundle.getString("number");
        email = bundle.getString("email");
        name = bundle.getString("name");
        pswrd = bundle.getString("pswrd");
        confrm = bundle.getString("confrm");
        StartFirebaseLogin();
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth.getInstance())
                .setActivity(getActivity())
                .setPhoneNumber(phnm)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setCallbacks(mCallback)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
       // PhoneAuthProvider.getInstance().verifyPhoneNumber(phnm, 60, TimeUnit.SECONDS, getActivity(), mCallback);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String o = otp.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, o);
                signInWithPhoneAuthCredential(credential);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void StartFirebaseLogin() {
        firebaseAuth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(getActivity(), "Code sent", Toast.LENGTH_LONG).show();

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    v = "1";
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    RegisterFragment l = new RegisterFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("v", v);
                    arguments.putString("number", phnm);
                    arguments.putString("email", email);
                    arguments.putString("name", name);
                    arguments.putString("pswrd", pswrd);
                    arguments.putString("confrm", confrm);
                    l.setArguments(arguments);
                    ft.add(R.id.ma, l);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    Toast.makeText(getActivity(), "Wrong code", Toast.LENGTH_LONG).show();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    RegisterFragment l = new RegisterFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("v", v);
                    arguments.putString("email", email);
                    arguments.putString("name", name);
                    arguments.putString("pswrd", pswrd);
                    arguments.putString("number", phnm);
                    arguments.putString("confrm", confrm);
                    l.setArguments(arguments);
                    ft.add(R.id.ma, l);
                    ft.addToBackStack(null);
                    ft.commit();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        //verification failed
                    }
                }
            }
        });
    }
}
