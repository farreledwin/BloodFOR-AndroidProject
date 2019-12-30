package edu.bluejack19_1.BloodFOR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import edu.bluejack19_1.BloodFOR.Model.NotificationPublisher;
import edu.bluejack19_1.BloodFOR.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import edu.bluejack19_1.BloodFOR.Fragment.DetailFragment;
import edu.bluejack19_1.BloodFOR.Fragment.HistoryDetailFragment;
import edu.bluejack19_1.BloodFOR.Fragment.HistoryFragment;
import edu.bluejack19_1.BloodFOR.Fragment.HomeFragment;
import edu.bluejack19_1.BloodFOR.Fragment.ParticipateFragment;
import edu.bluejack19_1.BloodFOR.Fragment.ProfileFragment;
import edu.bluejack19_1.BloodFOR.Fragment.StatisticFragment;
import edu.bluejack19_1.BloodFOR.Fragment.UpdateDataFragment;
import edu.bluejack19_1.BloodFOR.Fragment.UpdateDetailFragment;
import edu.bluejack19_1.BloodFOR.Model.Event;
import edu.bluejack19_1.BloodFOR.Model.User;
import edu.bluejack19_1.BloodFOR.interfacs.DataListener;

public class MainActivity extends AppCompatActivity implements DataListener, BottomNavigationView.OnNavigationItemSelectedListener{

    public static String email,uid;
    public static Boolean cekGoogle, cekFb;
    public static Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    public static Bitmap bitmap;
    private String GetUserID;
    private FirebaseAuth auth;
    private FirebaseDatabase getDatabase;
    private DatabaseReference getReference;
    Calendar objCalendar;
    Calendar objCalendar2;
    private String[] arrDate;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment =  null;
        boolean check = false;
        switch (item.getItemId()){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_history:
                fragment = new HistoryFragment();
                break;
            case R.id.nav_statistic:
                fragment = new StatisticFragment();
                break;
            case R.id.nav_profile:
                check = true;
                fragment = new ProfileFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        return loadFragment(fragment, check);
    }

    public boolean loadFragment(Fragment fragment, boolean check) {
        if (fragment != null && check) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment).addToBackStack(null).commit();
            return true;
        }else if (fragment != null && !check) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objCalendar = Calendar.getInstance();
        objCalendar2 = Calendar.getInstance();
        getDatabase = FirebaseDatabase.getInstance();
        getReference = getDatabase.getReference();
        scheduleNotification(getNotification("BANGSAT"), 1000);
        Bundle extras = getIntent().getExtras();
        BottomNavigationView btm = findViewById(R.id.bn_main);
        btm.setOnNavigationItemSelectedListener(this);
        contextOfApplication = getApplicationContext();

        email = extras.getString("email");
        uid = extras.getString("uid");
        cekGoogle = extras.getBoolean("cekGoogle");
        cekFb = extras.getBoolean("cekFb");
        loadFragment(new HomeFragment(),false);

    }

    @Override
    public void gotoDetailFragment(Event event) {
        loadFragment(new DetailFragment(event),true);
    }

    @Override
    public void gotoHistoryDetailFragment(Event event) {
        loadFragment(new HistoryDetailFragment(event),true);
    }
    @Override
    public void gotoDelete(Event event) {
        loadFragment(new UpdateDetailFragment(event),true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ProfileFragment.changeImage(ProfileFragment.view2,bitmap);
                ProfileFragment.uploadImage(filePath);
                ProfileFragment.pbar.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void scheduleNotification(Notification notification, int delay) {

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        GetUserID = user.getUid();
        Log.d("userid",GetUserID);
        final Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 4);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        Intent notificationIntent2 = new Intent(this, NotificationPublisher.class);
        notificationIntent2.putExtra(NotificationPublisher.NOTIFICATION_ID, 3);
        notificationIntent2.putExtra(NotificationPublisher.NOTIFICATION, notification);

        final PendingIntent pendingIntent[] = new PendingIntent[100];
//        final PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 101, notificationIntent2, 0);

        getReference.child("User").child(GetUserID).child("Event").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    final String getdate = d.child("eventDate").getValue(String.class);
                    arrDate = getdate.split("-");
                    final String day = arrDate[0];
                    final String month = arrDate[1];
                    pendingIntent[i] = PendingIntent.getBroadcast(getBaseContext(),100+i,notificationIntent,0);



                    objCalendar2.set(Calendar.YEAR, objCalendar.get(Calendar.YEAR));
                    //objCalendar.set(Calendar.YEAR, objCalendar.get(Calendar.YEAR));
                    objCalendar2.set(Calendar.MONTH, Integer.parseInt(month)-1);
                    objCalendar2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

                    long dif = objCalendar.getTimeInMillis() - objCalendar2.getTimeInMillis();
                    long diffinday = dif / (24 * 60 * 60 * 1000);
                    Log.d("datenow", ""+diffinday);
                    Log.d("aaazz",""+objCalendar2.getTime());


                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, objCalendar2.getTimeInMillis(), pendingIntent);
                    if( diffinday ==1) {
///                   alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
                        alarmManager2.setExact(AlarmManager.RTC_WAKEUP, 1000, pendingIntent[i]);
                    }
                    i+=1;
                    Log.d("int",i+"");

                }
                long futureInMillis = SystemClock.elapsedRealtime() + 1000;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Event Ended");
        builder.setContentText("You got points");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        return builder.build();
    }

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
}
