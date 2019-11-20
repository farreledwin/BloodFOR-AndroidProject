package edu.bluejack19_1.BloodFOR;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.bluejack19_1.BloodFOR.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double longitude,latitude;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        latitude = extras.getDouble("lat");
        longitude = extras.getDouble("long");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setIntent = new Intent(MapsActivity.this, MainActivity.class);
        String uid = MainActivity.uid;
        String email = MainActivity.email;
        Boolean cek = MainActivity.cekGoogle;
        setIntent.putExtra("uid",uid);
        setIntent.putExtra("email",email);
        setIntent.putExtra("cekGoogle",cek);
        startActivity(setIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(loc).title("Marker in "+name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng point) {
//                mMap.clear();
//                MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("aku disini");
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//
//                Log.d("test", "lalallalaalla");
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
//                    Log.d("fr",addresses.get(0).getAddressLine(0) + "CUPANG ");
//                } catch(Exception e) {
//                    Log.d("ssss","exception");
//                }
//
//
//                mMap.addMarker(marker);
//            }
//        });
    }
}
