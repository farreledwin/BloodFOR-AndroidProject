package edu.bluejack19_1.BloodFOR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordActivity extends AppCompatActivity {
    private EditText oldpassText;
    private EditText newpassText;
    public String email,password;
    public Button changepassbtn;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        init();
        check();
    }

    private void init(){
        changepassbtn = findViewById(R.id.changePassBtn);
        oldpassText = findViewById(R.id.oldPassTxt);
        newpassText = findViewById(R.id.newpassTxt);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        email = extras.getString("email");

        password = extras.getString("password");
        Log.d("email",email);
        Log.d("pass",password);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void check(){
        if (!email.isEmpty() && !password.isEmpty()) {
            final AuthCredential credential = EmailAuthProvider
                    .getCredential(email.trim(), password.trim());
            if (credential == null) {
                System.out.println("NULL");
            } else {
                changepassbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(oldpassText.getText().toString().equals(password)) {

                                    if (task.isSuccessful()) {

                                        user.updatePassword(newpassText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(PasswordActivity.this, "Update Password Success", Toast.LENGTH_LONG).show();

                                                } else {
                                                    Toast.makeText(PasswordActivity.this, "Update Password Failed", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(PasswordActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(PasswordActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                    Log.d("testttt",password);
                                }
                            }
                        });
                    }
                });

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setIntent = new Intent(PasswordActivity.this, MainActivity.class);
        String uid = MainActivity.uid;
        String email = MainActivity.email;
        Boolean cek = MainActivity.cekGoogle;
        setIntent.putExtra("uid",uid);
        setIntent.putExtra("email",email);
        setIntent.putExtra("cekGoogle",cek);
        startActivity(setIntent);
    }
}
