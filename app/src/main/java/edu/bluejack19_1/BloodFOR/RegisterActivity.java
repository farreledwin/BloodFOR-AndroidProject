package edu.bluejack19_1.BloodFOR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import edu.bluejack19_1.BloodFOR.Model.User;

import com.example.tpamobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameRegister, lastNameRegister, emailRegister, passwordRegister, confirmPasswordRegister;
    private RadioButton maleRegister,femaleRegister;
    private Button registerBtn;
    private DatabaseReference dbRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String firstName,lastName, email, password, confirmPassword, gender, profilePicture, bloodType;
    private TextView loginText;
    private RadioButton a,b,o,ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        register();
        login();
    }
    private void login(){
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }
    private void  init(){
        firstNameRegister = findViewById(R.id.first_name_register);
        lastNameRegister = findViewById(R.id.last_name_register);
        emailRegister = findViewById(R.id.email_register);
        passwordRegister = findViewById(R.id.password_register);
        confirmPasswordRegister = findViewById(R.id.confirm_password_register);
        maleRegister = findViewById(R.id.radio_male);
        femaleRegister = findViewById(R.id.radio_female);
        registerBtn = findViewById(R.id.register_button);
        loginText = findViewById(R.id.login_text);
        a = findViewById(R.id.type_a);
        b = findViewById(R.id.type_b);
        ab = findViewById(R.id.type_ab);
        o = findViewById(R.id.type_o);
    }
    private void  register(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePicture = "gs://donordarahtpa.appspot.com/images/default.jpg";
                firstName = firstNameRegister.getText().toString();
                lastName = lastNameRegister.getText().toString();
                email = emailRegister.getText().toString();
                password = passwordRegister.getText().toString();
                confirmPassword = confirmPasswordRegister.getText().toString();
                dbRef = firebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();


                if(firstName.isEmpty()){
                    firstNameRegister.setError("First Name must be filled");
                    firstNameRegister.requestFocus();
                }
                else  if(email.isEmpty()){
                    emailRegister.setError("Email must be filled");
                    emailRegister.requestFocus();
                }
                else if(!isValidEmail(email)){
                    emailRegister.setError("Email format doesn't correct");
                    emailRegister.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordRegister.setError("Password must be filled");
                    passwordRegister.requestFocus();
                }
                else if (password.length() < 6) {
                    passwordRegister.setError("Password length less than 6 character");
                }
                else if(confirmPassword.isEmpty()){
                    confirmPasswordRegister.setError("Confirm Password must be filled");
                    confirmPasswordRegister.requestFocus();
                }
                else if(!password.equals(password)){
                    confirmPasswordRegister.setError("Confirm password doesn't match");
                    confirmPasswordRegister.requestFocus();
                }
                else if(!maleRegister.isChecked() && !femaleRegister.isChecked()){
                    Toast.makeText(RegisterActivity.this,
                            "Gender must be choose" , Toast.LENGTH_LONG).show();
                }
                else if(!a.isChecked() && !b.isChecked() && !ab.isChecked() && !o.isChecked()) {
                    Toast.makeText(RegisterActivity.this,
                            "Blood Type must be choose", Toast.LENGTH_LONG).show();
                }
                else {
                    if(maleRegister.isChecked()) gender = "Male";
                    else if(femaleRegister.isChecked()) gender = "Female";
                    if(a.isChecked()) bloodType = "A";
                    else if(b.isChecked()) bloodType = "B";
                    else if(ab.isChecked()) bloodType = "AB";
                    else if (o.isChecked()) bloodType = "O";
                    else bloodType = "-";
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User newUser = new User(profilePicture, firstName, lastName, email, gender, bloodType);

                                dbRef.child("User").child(task.getResult().getUser().getUid()).setValue(newUser);
                                Toast.makeText(RegisterActivity.this,
                                        "Succes Register"
                                        , Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(myIntent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
