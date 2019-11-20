package edu.bluejack19_1.BloodFOR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class GetMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button confirmBtn;
    private Double longitude, latitude;
    private String email,uid;
    private Boolean cek;
    public List<Address> addresses;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_map);

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        uid = extras.getString("uid");
        cek = extras.getBoolean("cek");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            confirmBtn = findViewById(R.id.confirmbtn);

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    mMap.clear();
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("aku disini");
                    mMap.addMarker(marker);
                    longitude = point.longitude;
                    latitude = point.latitude;
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                Log.d("test", "lalallalaalla");
                try {
                    List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                    Log.d("fr",addresses.get(0).getAddressLine(0));
                    location = addresses.get(0).getAddressLine(0);
                } catch(Exception e) {
                    Log.d("ssss","exception");
                }
                }
            });

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(GetMapActivity.this, InsertDataActivity.class);
                    i.putExtra("longitude",longitude.toString());
                    i.putExtra("latitude",latitude.toString());
                    i.putExtra("uid",uid);
                    i.putExtra("email",email);
                    i.putExtra("cekGoogle",cek);
                    i.putExtra("location",location);
                    startActivity(i);
                }
            });
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setIntent = new Intent(GetMapActivity.this, InsertDataActivity.class);
        String uid = MainActivity.uid;
        String email = MainActivity.email;
        Boolean cek = MainActivity.cekGoogle;
        setIntent.putExtra("uid",uid);
        setIntent.putExtra("email",email);
        setIntent.putExtra("cekGoogle",cek);
        startActivity(setIntent);
    }
}
