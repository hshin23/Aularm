package com.a4dn.aularm.aularm;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * Attempt at making the class be parcelable, which failed...
 */
class FirebaseHelper implements Parcelable {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    boolean firstTime;
    String prev_alarm;

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

    void write(String uid, String keyString, String msg) {
        String key = this.mDatabase.getReference().child(uid).getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        System.out.println("Writing to Firebase: " + keyString + " = " + msg);
        childUpdates.put("/users/" + uid + "/" + keyString, msg);

        mDatabase.getReference().updateChildren(childUpdates);
    }

    void readAlarm() {
        mDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                prev_alarm = (String) dataSnapshot.getValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                prev_alarm = (String) dataSnapshot.getValue();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                prev_alarm = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void isFirstTimeUser() {
        DatabaseReference ref = mDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        final boolean[] val = new boolean[1];
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("firsttime").getValue().equals("false")) {
                    isFirstTimeUser(false);
                } else {
                    isFirstTimeUser(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void isFirstTimeUser(Boolean val) {
        firstTime = val;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable((Parcelable) mAuth, i);
        parcel.writeParcelable((Parcelable) mAuthListener, i);
        parcel.writeParcelable((Parcelable) mDatabase, i);
        parcel.writeString(prev_alarm);
    }

    private FirebaseHelper(Parcel in) {
        mAuth = (FirebaseAuth) in.readParcelable(FirebaseAuth.class.getClassLoader());
        mAuthListener = (FirebaseAuth.AuthStateListener) in.readParcelable(FirebaseAuth.AuthStateListener.class.getClassLoader());
        mDatabase = (FirebaseDatabase) in.readParcelable(FirebaseDatabase.class.getClassLoader());
        prev_alarm = in.readString();
    }

    public static final Parcelable.Creator<FirebaseHelper> CREATOR = new Creator<FirebaseHelper>() {
        @Override
        public FirebaseHelper createFromParcel(Parcel parcel) {
            return new FirebaseHelper(parcel);
        }

        @Override
        public FirebaseHelper[] newArray(int i) {
            return new FirebaseHelper[0];
        }
    };
}
