package edu.bluejack19_1.BloodFOR.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tpamobile.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import edu.bluejack19_1.BloodFOR.LoginActivity;
import edu.bluejack19_1.BloodFOR.MainActivity;
import edu.bluejack19_1.BloodFOR.Model.User;

public class ProfileFragment extends Fragment {

    public static ImageView profilePic;
    private EditText email,firstName,lastName;
    private FirebaseAuth auth;
    private FirebaseDatabase getDatabase;
    private DatabaseReference getReference;
    private String GetUserID;
    private String email2, downloadURL;
    private RadioButton maleProfile, femaleProfile;
    private Button changeProfilePicture, saveBtn, insertEvent, changePassword;
    private StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference ref;
    public static View view2;
    private RadioButton a,b,o,ab;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        view2 = view;
        changeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(view);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailTxt = email.getText().toString();
                final String firstNameTxt = firstName.getText().toString();
                String lastNameTxt = lastName.getText().toString();
                String gender = "";
                if(maleProfile.isChecked()){
                    gender = "Male";
                }else{
                    gender = "Female";
                }

                String bloodType = "";
                if(a.isChecked()) bloodType = "A";
                else if(b.isChecked()) bloodType = "B";
                else if(ab.isChecked()) bloodType = "AB";
                else if (o.isChecked()) bloodType = "O";
                else bloodType = "-";

                final User saveData = new User( uploadImage()+"", firstNameTxt, lastNameTxt, emailTxt, gender,bloodType);
                getReference.child("User").child(GetUserID).setValue(saveData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Success Update Profile",Toast.LENGTH_LONG).show();
                    }
                });
                Glide.with(view)
                        .load(saveData.getProfilePicture())
                        .apply(new RequestOptions().override(400, 400))
                        .into(profilePic);
            }
        });
    }

    public void chooseImage(final View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        filePath = MainActivity.filePath;
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public static void changeImage(View view, Bitmap bitmap){
        Glide.with(view)
                .load(bitmap)
                .apply(new RequestOptions().override(400, 400))
                .into(profilePic);
    }

    private String uploadImage() {
        filePath = MainActivity.filePath;
        if(filePath != null) {
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadURL = uri.toString();
                                    System.out.println("test download" + downloadURL);
                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        return downloadURL;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    private void init(final View view){
        profilePic = view.findViewById(R.id.profile_picture);
        email = view.findViewById(R.id.email_profile);
        firstName = view.findViewById(R.id.first_name_profile);
        lastName = view.findViewById(R.id.last_name_profile);
        maleProfile = view.findViewById(R.id.radio_male);
        femaleProfile = view.findViewById(R.id.radio_female);
        changeProfilePicture = view.findViewById(R.id.choose_button);
        changePassword = view.findViewById(R.id.password_button);
        insertEvent = view.findViewById(R.id.insert_event_button);
        saveBtn = view.findViewById(R.id.save_button);

        a = view.findViewById(R.id.type_a);
        b = view.findViewById(R.id.type_b);
        ab = view.findViewById(R.id.type_ab);
        o = view.findViewById(R.id.type_o);
        auth = FirebaseAuth.getInstance();
        if(!MainActivity.cekGoogle) {
            FirebaseUser user = auth.getCurrentUser();
            GetUserID = user.getUid();
            email2 = user.getEmail();
        }else{
            GetUserID = MainActivity.uid;
            email2 = MainActivity.email;
        }
        getDatabase = FirebaseDatabase.getInstance();
        getReference = getDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getReference.child("User").orderByChild("email").equalTo(email2).addChildEventListener(new ChildEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String profilePicUser = dataSnapshot.child("profilePicture").getValue(String.class);
                final String emailUser = dataSnapshot.child("email").getValue(String.class);
                final String firstNameUser = dataSnapshot.child("firstName").getValue(String.class);
                final String lastNameUser = dataSnapshot.child("lastName").getValue(String.class);
                final String genderUser = dataSnapshot.child("gender").getValue(String.class);
                final String bloodTypeUser = dataSnapshot.child("bloodType").getValue(String.class);
                User user = new User(profilePicUser, firstNameUser, lastNameUser, emailUser, genderUser,bloodTypeUser );
                email.setText(""+user.getEmail());
                firstName.setText(""+user.getFirstName());
                lastName.setText(""+user.getLastName());
                if(user.getGender().equals("Male")){
                    maleProfile.setChecked(true);
                }else{
                    femaleProfile.setChecked(true);
                }
                if(user.getBloodType().equals("A")) a.setChecked(true);
                else if(user.getBloodType().equals("B")) b.setChecked(true);
                else if(user.getBloodType().equals("AB")) ab.setChecked(true);
                else if(user.getBloodType().equals("O")) o.setChecked(true);
                Glide.with(view)
                        .load(user.getProfilePicture())
                        .apply(new RequestOptions().override(400, 400))
                        .into(profilePic);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
