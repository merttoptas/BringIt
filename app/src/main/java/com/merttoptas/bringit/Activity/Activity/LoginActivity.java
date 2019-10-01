package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.merttoptas.bringit.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ProgressBar loadingProgress;
    SignInButton signInButton;
    LoginButton LoginButton;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "FACELOG";
    private CallbackManager mCallbackManager;
    TextView tvLoginText;
    Typeface typeface;
    DatabaseReference ref;
    FirebaseUser currentUser;
    Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvLoginText = findViewById(R.id.tvLoginText);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Billabong.ttf");
        tvLoginText.setTypeface(typeface);

        mAuth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.SignInButton);
        loadingProgress = findViewById(R.id.progressBar);
        LoginButton = findViewById(R.id.login_button);

        loadingProgress.setVisibility(View.INVISIBLE);

        mCallbackManager = CallbackManager.Factory.create();

        googleFacebookSign();

    }

    public void googleFacebookSign(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("669520976677-3sv6tls1nvl01d0v5b9utbd2af51bjng.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

        //Facebook Login
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton.setReadPermissions("email", "public_profile");

        LoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("fbAccess","fbAccess:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                loadingProgress.setVisibility(View.VISIBLE);
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
                Intent accountIntent = new Intent(getApplicationContext(), SliderActivity.class);
                accountIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                accountIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                accountIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(accountIntent);
                overridePendingTransition (0, 0);


            }
            @Override
            public void onCancel() {
                Log.d("fbCancel", "facebook:onCancel");
                Toast.makeText(LoginActivity.this, "Giriş İptal Edildi", Toast.LENGTH_SHORT).show();
                loadingProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("fbError", "facebook:onError");
                Toast.makeText(LoginActivity.this, "Giriş Hatalı", Toast.LENGTH_SHORT).show();
                loadingProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            updateUI(currentUser);
        }
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void updateUI(FirebaseUser user) {

        if(user !=null){
            String username = user.getDisplayName();
            String userid = user.getUid();
            Uri photoURL = user.getPhotoUrl();


            ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);
            Log.d("photoURL", "photoURL:" + photoURL);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", userid);
            hashMap.put("username", username);

            for (UserInfo profile : user.getProviderData()){
                Uri photoUrl = profile.getPhotoUrl();
                String photoURlL = photoUrl.toString();
                hashMap.put("imageURL", photoURlL);
                Log.d("photoURL", "photoURL:" + photoURlL);
            }
            if ( currentUser == null){

                ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent accountIntent = new Intent(getApplicationContext(), SliderActivity.class);
                            accountIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            accountIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            accountIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(accountIntent);
                            overridePendingTransition (0, 0);
                            finish();
                        }
                    }
                });
            }
            else{
                Intent accountIntent = new Intent(getApplicationContext(), SliderActivity.class);
                accountIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                accountIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                accountIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(accountIntent);
                overridePendingTransition (0, 0);
                finish();

            }

            Toast.makeText(LoginActivity.this, getResources().getString(R.string.kullanici_adi) + username,  Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                loadingProgress.setVisibility(View.VISIBLE);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);

            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            user.getPhotoUrl();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("handlefb", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d("fbsSign", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            user.getPhotoUrl();
                            updateUI(user);
                        } else {
                            Log.w("fbSignC", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });

    }


}
