package com.a4dn.aularm.aularm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private FirebaseHelper firebase = new FirebaseHelper();

    private static List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        // startActivity(new Intent(this, ExampleGCal.class));
        if (!firebase.isSignedIn()) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setLogo(R.drawable.logo).setTheme(R.style.GreenTheme).build(), RC_SIGN_IN);
        } else {
            showMainMenu();
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
        firebase.removeListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        firebase.signIn(requestCode, resultCode, data, RC_SIGN_IN);

        if (resultCode == RESULT_OK) {
            showMainMenu();
        } else {
            finish();
        }
    }

    protected void showMainMenu() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }
}
