package edu.bluejack19_1.BloodFOR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpamobile.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import edu.bluejack19_1.BloodFOR.Model.User;

import static edu.bluejack19_1.BloodFOR.RegisterActivity.isValidEmail;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private GoogleSignInAccount acct;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton facebook;
    private Boolean saveLogin;
    private FirebaseAuth auth;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private CheckBox checkBoxLogin;
    private SignInButton google;
    private Button loginBtn, googleBtn, facebookBtn;
    private EditText emailLogin, passwordLogin;
    private TextView registerBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;
    public  static String pass;
    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        facebook();
        google();
        login();
        register();
//        setGooglePlusButtonText(google, "Login with Google");
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebook.performClick();

            }
        });
//        googleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                google.performClick();
//            }
//        });
    }
//    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
//        for (int i = 0; i < signInButton.getChildCount(); i++) {
//            View v = signInButton.getChildAt(i);
//
//            if (v instanceof TextView) {
//                TextView tv = (TextView) v;
//                tv.setText(buttonText);
//                tv.setTextColor(getResources().getColor(R.color.white));
//                tv.setBackground(getResources().getDrawable(
//                        R.drawable.button3));
//                tv.setSingleLine(true);
//                tv.setTextSize(17);
//                return;
//            }
//        }
//    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                acct = account;
            } catch (ApiException e) {
                Log.w("Baki", "Google sign in failed", e);
            }
        }
    }

    private void google(){
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                if (acct != null) {
                    final String personName = acct.getDisplayName();
                    final String personFirstName = acct.getGivenName();
                    final String personLastName = acct.getFamilyName();
                    final String personEmail = acct.getEmail();
                    final String personId = acct.getId();
                    final Uri personPhoto = acct.getPhotoUrl();
                    final String personGender = "Male";
                    final String password = "farrel";
                    final String bloodType = "A";
                    final String role = "Member";
                    DatabaseReference refs = FirebaseDatabase.getInstance().getReference().child("User");
                    refs.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean found = false;
                            String uid = "";
                            for(DataSnapshot d : dataSnapshot.getChildren()){
                                if(personEmail.equals(d.child("email").getValue())){
                                    found = true;
                                    uid = d.getKey();
                                }
                            }
                            if(!found){
                                User newUser = new User(""+personPhoto, personFirstName, personLastName, personEmail, personGender, bloodType, role);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").push();
                                uid = ref.getKey();
                                ref.child("email").setValue(newUser.getEmail());
                                ref.child("firstName").setValue(newUser.getFirstName());
                                ref.child("lastName").setValue(newUser.getLastName());
                                ref.child("gender").setValue(newUser.getGender());
                                ref.child("profilePicture").setValue(newUser.getProfilePicture());
                                ref.child("bloodType").setValue(newUser.getBloodType());
                                ref.child("role").setValue(newUser.getRole());
                                Log.d("baki", newUser.getBloodType());
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("email", personEmail);
                            bundle.putString("pass", password);
                            Toast.makeText(LoginActivity.this,"Success Login",Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                            myIntent.putExtra("uid", uid);
                            myIntent.putExtra("cekGoogle", true);
                            myIntent.putExtra("cekFb", false);
                            myIntent.putExtra("email",personEmail);
                            startActivity(myIntent);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void facebook(){
        facebook.setReadPermissions("email");
        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
//                final Profile profile = Profile.getCurrentProfile();
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.v("facebook - profile", currentProfile.getFirstName());
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String personEmail = object.getString("email");
                                    Profile profile = Profile.getCurrentProfile();
                                    setEmail(profile, personEmail);
                                    Toast.makeText(getApplicationContext(), "Hi, " + object.getString("name"), Toast.LENGTH_LONG).show();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "Cancel Login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEmail(Profile profile, String email) {
        final String personEmail =  email;
        final String personName = profile.getFirstName() + " " +  profile.getMiddleName();
        final String personLastName = profile.getLastName();
        final String personPhoto = profile.getProfilePictureUri(100,100).toString();
        final String personGender = "Male";
        final String password = "farrel";
        final String bloodType = "A";
        final String role = "Member";
        Log.d("Baki", email.toString());
        dbRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference refs = FirebaseDatabase.getInstance().getReference().child("User");
        refs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                String uid = "";
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(personEmail.equals(d.child("email").getValue())){
                        found = true;
                        uid = d.getKey();
                    }
                }
                if(!found){
                    User newUser = new User(""+personPhoto, personName, personLastName, personEmail, personGender, bloodType, role);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").push();
                    uid = ref.getKey();
                    ref.child("email").setValue(newUser.getEmail());
                    ref.child("firstName").setValue(newUser.getFirstName());
                    ref.child("lastName").setValue(newUser.getLastName());
                    ref.child("gender").setValue(newUser.getGender());
                    ref.child("profilePicture").setValue(newUser.getProfilePicture());
                    ref.child("bloodType").setValue(newUser.getBloodType());
                    ref.child("role").setValue(newUser.getRole());
                }
                Bundle bundle = new Bundle();
                bundle.putString("email", personEmail);
                bundle.putString("pass", password);
                Toast.makeText(LoginActivity.this,"Success Login",Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                myIntent.putExtra("uid", uid);
                myIntent.putExtra("cekGoogle", false);
                myIntent.putExtra("cekFb", true);
                myIntent.putExtra("email",personEmail);
                startActivity(myIntent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void login() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = emailLogin.getText().toString().trim();
                final String password = passwordLogin.getText().toString().trim();

                 if (email.isEmpty()) {
                     emailLogin.setError("Email must be filled");
                }
                else if (!isValidEmail(email)) {
                     emailLogin.setError("Email format doesn't correct");
                }
                else if (password.isEmpty()) {
                     passwordLogin.setError("Password must be filled");
                }
                else if (password.length() < 6) {
                     passwordLogin.setError("Password length less than 6 character");
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,
                                                "Login Failed " + task.getException().getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    } else {
                                        if (checkBoxLogin.isChecked()) {
                                            loginPrefsEditor.putBoolean("saveLogin",true);
                                            loginPrefsEditor.putString("email",email);
                                            loginPrefsEditor.putString("password",password);
                                            loginPrefsEditor.commit();
                                        }
                                        else {
                                            loginPrefsEditor.clear();
                                            loginPrefsEditor.commit();
                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putString("email", email);
                                        pass = password;
                                        Toast.makeText(LoginActivity.this,"Success Login",Toast.LENGTH_LONG).show();
                                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        myIntent.putExtra("email",email);
                                        myIntent.putExtra("cekGoogle", false);
                                        myIntent.putExtra("cekFb", false);
                                        myIntent.putExtra("uid",task.getResult().getUser().getUid());

                                        startActivity(myIntent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void  register(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
    private void init(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("257757413925-q7rvscdsk2qc8q8vob81gmqhv9cnvk4s.apps.googleusercontent.com")
                .requestEmail()
                .build();
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google = findViewById(R.id.google_button);
        facebook = findViewById(R.id.facebook_button);
        loginBtn = findViewById(R.id.login_button);
        registerBtn = findViewById(R.id.register_text);
        facebookBtn = findViewById(R.id.facebook_buttons);
//        googleBtn = findViewById(R.id.google_buttons);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        facebook.setReadPermissions("public_profile");
        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        auth = FirebaseAuth.getInstance();
        checkBoxLogin = findViewById(R.id.check_box_login);
        loginPreferences = getSharedPreferences("loginPrefs",MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            emailLogin.setText(loginPreferences.getString("email",""));
            passwordLogin.setText(loginPreferences.getString("password",""));
            checkBoxLogin.setChecked(true);
        }

    }
}
