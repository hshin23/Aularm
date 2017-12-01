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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected void showMainMenu(int pageIndex) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("uid", mAuth.getCurrentUser().getUid());
        intent.putExtra("page", pageIndex);
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

    private void checkIfFirstAndSetOption() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "Current User: " + FirebaseAuth.getInstance().getUid());

                // Checks whether a user is in database or not
                if (dataSnapshot.getChildrenCount() == 0) {
                    Log.i(TAG, "First time user " + FirebaseAuth.getInstance().getUid());
                    String uid = FirebaseAuth.getInstance().getUid();
                    writeGeneral(FirebaseAuth.getInstance().getUid(), "firsttime", "true");
                    writeGeneral(uid, "alarm", "");
                    writeGeneral(uid, "avg_sleep_time", "");
                    writeGeneral(uid, "avg_time_to_sleep", "");
                    writeGeneral(uid, "avg_wake_up_time", "");
                    writeGeneral(uid, "suggested_time", "");
                    showMainMenu(2);
                } else {
                    Log.i(TAG, "Non-first time user " + FirebaseAuth.getInstance().getUid());
                    showMainMenu(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "error:", databaseError.toException());
            }
        });
    }

    void writeGeneral(String uid, String keyString, String msg) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + uid + "/general/" + keyString, msg);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
    }

    void writeTodo(String uid, String keyString, String msg) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + uid + "/todo/" + keyString, msg);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
    }

}
