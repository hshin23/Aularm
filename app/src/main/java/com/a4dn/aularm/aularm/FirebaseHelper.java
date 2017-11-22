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

import static android.content.ContentValues.TAG;

class FirebaseHelper {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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

    void signOut(@NonNull Context context) {
        AuthUI.getInstance()
              .signOut((FragmentActivity) context)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                  public void onComplete(@NonNull Task<Void> task) {
                      // ...
                  }
              });
    }

    boolean isSignedIn() {
        if (this.mAuth.getCurrentUser() != null) {
            return true;
        } else {
            return false;
        }
    }
}
