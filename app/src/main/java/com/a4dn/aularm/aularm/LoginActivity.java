package com.a4dn.aularm.aularm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
    private static final int RC_SIGN_IN = 123;
    private final String TAG = "LOGIN_ACTIVITY";

    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    // if already signed in
                    checkIfFirstAndSetOption();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseAuth.getCurrentUser().getUid());
                } else {
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setLogo(R.drawable.logo).setTheme(R.style.GreenTheme).build(), RC_SIGN_IN);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "HERE");
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        signIn(requestCode, resultCode, data, RC_SIGN_IN);
        if (resultCode == RESULT_OK) {
            checkIfFirstAndSetOption();
        } else {
            finish();
        }
    }

    /**
     * Set up listeners  for firebase and initialize firebase objects
     */
    private void setupFirebase() {
    }

    protected void showMainMenu() {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("uid", mAuth.getCurrentUser().getUid());
        intent.putExtra("page", page);
        startActivity(intent);
        finish();
    }

    private boolean isSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    void signIn(int requestCode, int resultCode, Intent data, int RC_SIGN_IN) {
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "onAuthStateStatic:signed_in:" + mAuth.getCurrentUser().getUid());
            return;
        }

        if (requestCode ==  RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode != ResultCodes.OK) {
                Log.d(TAG, "Failed to sign in:" + Integer.toString(resultCode));
            }
        }
    }

    void setHistory(String val) {
        if (val == null) {
            return ;
        } else {
            if (val.equals("false")) {
                this.page = 1;
            } else {
                this.page = 2;
            }
            if (!isSignedIn()) {
            } else {
                showMainMenu();
            }
        }
    }

    private void checkIfFirstAndSetOption() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, dataSnapshot.toString());
                String firstTime = dataSnapshot.child("firsttime").getValue().toString();
                setHistory(firstTime);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "error:", databaseError.toException());
            }
        });
    }
}
