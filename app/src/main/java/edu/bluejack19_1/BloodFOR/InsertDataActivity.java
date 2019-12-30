package edu.bluejack19_1.BloodFOR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import edu.bluejack19_1.BloodFOR.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import edu.bluejack19_1.BloodFOR.Model.Event;

public class InsertDataActivity extends AppCompatActivity {


    private DatePickerDialog datePickerDialog;
    private DatabaseReference database;
    private EditText eventTxt, eventDateTxt, eventLocationTxt,eventDesc;
    private Button insertBtn,btnMap;
    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String downloadURL;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private String uid, email, lats , longs;
    private Boolean cek;
    private Button btDatePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);


        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        uid = extras.getString("uid");
        cek = extras.getBoolean("cek");


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        eventTxt = findViewById(R.id.eventDateTxt);
        eventDateTxt = findViewById(R.id.eventDate);
        eventLocationTxt = findViewById(R.id.eventLocationText);
        insertBtn = findViewById(R.id.insertBtn);
        btnChoose = findViewById(R.id.imageUploadBtn);

        eventDesc = findViewById(R.id.eventDescTxt);
//        btDatePicker = findViewById(R.id.datepicker);
//        btDatePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDateDialog();
//            }
//        });
        eventDateTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDateDialog();
                }
            }
        });
        imageView = findViewById(R.id.imgView);
        btnMap = findViewById(R.id.gotomaps);
        eventLocationTxt.setText(extras.getString("location"));
        database = FirebaseDatabase.getInstance().getReference();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                uploadImage();
            }
        });



        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(eventTxt.getText().toString()) && !TextUtils.isEmpty(eventDesc.getText().toString()) && !TextUtils.isEmpty(longs) && !TextUtils.isEmpty(lats) && !TextUtils.isEmpty(eventDateTxt.getText().toString())
                        && !TextUtils.isEmpty((eventLocationTxt.getText().toString()))) {
                    try {
                        submitData(new Event(downloadURL, eventTxt.getText().toString(), eventDesc.getText().toString(),eventLocationTxt.getText().toString(), formatter.parse(eventDateTxt.getText().toString()),Double.parseDouble(lats),Double.parseDouble(longs),""));
                        Intent i = new Intent(InsertDataActivity.this, MainActivity.class);
                        i.putExtra("uid",uid);
                        i.putExtra("email",email);
                        i.putExtra("cekGoogle",cek);
                        startActivity(i);
                        finish();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(InsertDataActivity.this, "There is still empty data", Toast.LENGTH_LONG).show();
                }

            }
        });

        gotoMaps();
        if(extras != null) {
            lats = extras.getString("longitude");
            longs = extras.getString("latitude");
        }
    }
    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(InsertDataActivity.this, new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                eventDateTxt.setText(formatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    public void submitData(Event event) {

        DatabaseReference db = database.child("Event").push();
        db.child("eventName").setValue(event.getEventName());
        db.child("eventDesc").setValue(event.getEventDesc());
        db.child("eventPicture").setValue(event.getEventPicture());
        db.child("eventDate").setValue(formatter.format(event.getEventDate()));
        db.child("eventLatitude").setValue(event.getEventLatitude().toString());
        db.child("eventLongitude").setValue(event.getEventLongitude().toString());
        db.child("eventLocation").setValue(event.getEventLocation());


        eventTxt.setText("");
        eventDateTxt.setText("");
        eventLocationTxt.setText("");
        eventDesc.setText("");

        Toast.makeText(InsertDataActivity.this, "Succcess Insert", Toast.LENGTH_LONG).show();
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            if(filePath != null)
            {

                final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(InsertDataActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadURL = uri.toString();
                                    }
                                });
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(InsertDataActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setIntent = new Intent(InsertDataActivity.this, MainActivity.class);
        String uid = MainActivity.uid;
        String email = MainActivity.email;
        Boolean cek = MainActivity.cekGoogle;
        setIntent.putExtra("uid",uid);
        setIntent.putExtra("email",email);
        setIntent.putExtra("cekGoogle",cek);
        startActivity(setIntent);
    }

    private void uploadImage() {

        if(filePath != null)
        {

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(InsertDataActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadURL = uri.toString();
                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(InsertDataActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void gotoMaps() {
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setIntent = new Intent(InsertDataActivity.this, GetMapActivity.class);
                String uid = MainActivity.uid;
                String email = MainActivity.email;
                Boolean cek = MainActivity.cekGoogle;
                setIntent.putExtra("uid",uid);
                setIntent.putExtra("email",email);
                setIntent.putExtra("cekGoogle",cek);
                startActivity(setIntent);
            }
        });
    }
}
