package edu.bluejack19_1.BloodFOR.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import edu.bluejack19_1.BloodFOR.InsertDataActivity;
import edu.bluejack19_1.BloodFOR.MainActivity;
import edu.bluejack19_1.BloodFOR.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.bluejack19_1.BloodFOR.Adapter.ListEventAdapter;
import edu.bluejack19_1.BloodFOR.Model.Event;

public class HomeFragment extends Fragment {

    private DatabaseReference getReference;
    private RecyclerView rvEventView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Event> listEvent = new ArrayList<>();
    private Context c;
    private View view;
    private EditText search;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Double lat, lng;
    private String mycity;
    private CheckBox geo;
    private Date today = new Date();
    private FloatingActionButton insertEventBtn;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("unique", "testing");
        view = inflater.inflate(R.layout.fragment_event, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        addData();

        insertEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InsertDataActivity.class);
                String email = MainActivity.email;
                String uid = MainActivity.uid;
                Boolean cek = MainActivity.cekGoogle;
                i.putExtra("email",email);
                i.putExtra("uid",uid);
                i.putExtra("cek",cek);
                String lats = "-6.0";
                String longs = "106.0";
                i.putExtra("longitude",longs);
                i.putExtra("latitude",lats);

                getActivity().finish();
                startActivity(i);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchData(search.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchData(final Editable text) {
        getReference.child("Event").orderByChild("eventName").startAt(text.toString())
                .endAt(text.toString() + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot){

                if (text.toString().equals("")) {
                    listEvent.clear();
                    if (geo.isChecked()) geolocation();
                    else addData();
                } else {
                    listEvent.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String eventPicture = d.child("eventPicture").getValue(String.class);
                        String eventName = d.child("eventName").getValue(String.class);
                        String eventDesc = d.child("eventDesc").getValue(String.class);
                        String eventLocation = d.child("eventLocation").getValue(String.class);
                        Date eventDate = null;
                        try {
                            eventDate = formatter.parse(d.child("eventDate").getValue(String.class)+"");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Double eventLatitude = Double.parseDouble(d.child("eventLatitude").getValue(String.class));
                        Double eventLongitude = Double.parseDouble(d.child("eventLongitude").getValue(String.class));
                        String eventId = d.getKey();
                        Event e = new Event(eventPicture, eventName, eventDesc, eventLocation, eventDate, eventLatitude, eventLongitude, eventId);

                        assert eventDate != null;
                        if(eventDate.after(today)){
                            if(geo.isChecked()){
                                assert eventLocation != null;
                                if(eventLocation.equals(mycity))
                                    listEvent.add(e);
                            }
                            else listEvent.add(e);
                        }
                    }
                }
                showRecyclerList();
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }
        });
    }

    private void addData() {
        getReference.child("Event").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listEvent.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String eventPicture = d.child("eventPicture").getValue(String.class);
                    String eventName = d.child("eventName").getValue(String.class);
                    String eventDesc = d.child("eventDesc").getValue(String.class);
                    String eventLocation = d.child("eventLocation").getValue(String.class);
                    Date eventDate = null;
                    try {
                        eventDate = formatter.parse(d.child("eventDate").getValue(String.class)+"");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Double eventLatitude = Double.parseDouble(d.child("eventLatitude").getValue(String.class));
                    Double eventLongitude = Double.parseDouble(d.child("eventLongitude").getValue(String.class));
                    String eventId = d.getKey();
                    Event e = new Event(eventPicture, eventName, eventDesc, eventLocation, eventDate, eventLatitude, eventLongitude, eventId);

                    assert eventDate != null;
                    if(eventDate.after(today))
                        listEvent.add(e);
                }
                showRecyclerList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRecyclerList() {
        rvEventView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListEventAdapter listEventAdapter = new ListEventAdapter(getContext(), listEvent);
        rvEventView.setAdapter(listEventAdapter);
    }

    private void init(View view) {
        FirebaseDatabase getDatabase = FirebaseDatabase.getInstance();

        search = view.findViewById(R.id.search_bar);
        rvEventView = view.findViewById(R.id.rv_events);
        rvEventView.setHasFixedSize(true);
        rvEventView.setLayoutManager(layoutManager);
        getReference = getDatabase.getReference();
        geo = view.findViewById(R.id.check_box_geolocation);
        insertEventBtn = view.findViewById(R.id.insert_event_button);

        getReference.child("User").child(MainActivity.uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("role").getValue().toString().equals("Member")){
                    insertEventBtn.hide();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        geo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("Baki","sadas");
                if(geo.isChecked()){
                    geolocation();
                }
                else addData();
            }
        });
    }

    private void geolocation() {
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocation.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    Geocoder gcd = new Geocoder(Objects.requireNonNull(getActivity()).getBaseContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            mycity = addresses.get(0).getSubAdminArea();
                        }
                    } catch (IOException ie) {
                        System.out.println("Address Error");
                        ie.printStackTrace();
                    }
                    getReference.child("Event").orderByChild("eventLocation").equalTo(mycity).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            listEvent.clear();
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                String eventPicture = d.child("eventPicture").getValue(String.class);
                                String eventName = d.child("eventName").getValue(String.class);
                                String eventDesc = d.child("eventDesc").getValue(String.class);
                                String eventLocation = d.child("eventLocation").getValue(String.class);
                                Date eventDate = null;
                                try {
                                    eventDate = formatter.parse(d.child("eventDate").getValue(String.class)+"");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Double eventLatitude = Double.parseDouble(d.child("eventLatitude").getValue(String.class));
                                Double eventLongitude = Double.parseDouble(d.child("eventLongitude").getValue(String.class));
                                String eventId = d.getKey();
                                Event e = new Event(eventPicture, eventName, eventDesc, eventLocation, eventDate, eventLatitude, eventLongitude, eventId);
                                assert eventDate != null;
                                if(eventDate.after(today))
                                    listEvent.add(e);
                            }
                            showRecyclerList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private int checkSelfPermission(String accessFineLocation) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeAllViews();
            }
        }

    }
}
