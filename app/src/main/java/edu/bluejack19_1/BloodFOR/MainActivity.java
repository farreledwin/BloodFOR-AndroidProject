package edu.bluejack19_1.BloodFOR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tpamobile.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment =  null;
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
                fragment = new ProfileFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        return loadFragment(fragment, false);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
}
