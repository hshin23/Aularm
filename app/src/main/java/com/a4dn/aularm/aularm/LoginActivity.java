package com.a4dn.aularm.aularm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static final FirebaseHelper firebase = new FirebaseHelper();

    private static List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            if (!firebase.isSignedIn()) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setLogo(R.drawable.logo).setTheme(R.style.GreenTheme).build(), RC_SIGN_IN);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.signOut(this);
        firebase.removeListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("request code: " + requestCode);
        System.out.println("result  code: " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        firebase.signIn(requestCode, resultCode, data, RC_SIGN_IN);
    }
}
