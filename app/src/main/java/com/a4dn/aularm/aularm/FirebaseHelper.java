package com.a4dn.aularm.aularm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import static android.content.ContentValues.TAG;

class FirebaseHelper {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mDatabase;

    private boolean signedIn;
    private FirebaseUser currentUser;


    FirebaseHelper() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    signedIn = true;
                    currentUser = user;
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    signedIn = false;
                    currentUser = null;
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

    /*
    void signIn(int requestCode, int resultCode, Intent data, int RC_SIGN_IN) {
        if (signedIn) {
            Log.d(TAG, "onAuthStateStatic:signed_in:" + this.currentUser.getUid());
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
        if (signedIn) {
            AuthUI.getInstance()
                    .signOut((FragmentActivity) context)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
        }
    }
    */

    boolean isSignedIn() {
        return signedIn;
    }

    FirebaseUser getUser() {
        return currentUser;
    }

    void write(String uid, String msg) {
        this.mDatabase.getReference(uid).setValue(msg);
    }

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

    /*
     * Methods that are prefixed with __ are unused example methods for reference.
     */
    /*
    void __signIn(int requestCode, int resultCode, Intent data, int RC_SIGN_IN) {
        if (this.mAuth.getCurrentUser() != null) {
            Log.d(TAG, "onAuthStateStatic:signed_in:" + mAuth.getCurrentUser().getUid());
        } else if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Log.d(TAG, "Failed to sign in:" + Integer.toString(resultCode));
            }
        }
    }
    */
}
