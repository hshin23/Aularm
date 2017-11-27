package com.a4dn.aularm.aularm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static android.content.ContentValues.TAG;

class FirebaseHelper {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;

    FirebaseHelper() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        this.mDatabase = FirebaseDatabase.getInstance();
    }

    void addListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    void removeListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

    void signOut(@NonNull Context context) {
        if (mAuth.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut((FragmentActivity) context)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
        }
    }

    boolean isSignedIn() {
        if (mAuth.getCurrentUser() == null) {
            return false;
        } else {
            return true;
        }
    }

    FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

    void write(String uid, String inner, String msg) {
        this.mDatabase.getReference(uid).setValue(msg);
    }
    //mDatabase.child("users").child(userId).child("username").setValue(name);


    DatabaseReference read(String uid) {
        DatabaseReference ref = this.mDatabase.getReference(uid);

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError e) {
                Log.w(TAG, "Failed to read value.", e.toException());
            }
        });

        return ref;
    }

}
