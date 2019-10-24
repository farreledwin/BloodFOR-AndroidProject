package edu.bluejack19_1.BloodFOR.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tpamobile.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.bluejack19_1.BloodFOR.Adapter.HistoryAdapter;
import edu.bluejack19_1.BloodFOR.MainActivity;
import edu.bluejack19_1.BloodFOR.Model.Event;

public class HistoryEventFragment extends Fragment {
    private ArrayList<Event> listEvent = new ArrayList<>();
    private RecyclerView rvEventView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference getReference;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Date today = new Date();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_event, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        addData();
    }

    private void showRecyclerList() {
        rvEventView.setLayoutManager(new LinearLayoutManager(getContext()));
        HistoryAdapter listEventAdapter = new HistoryAdapter(getContext(), listEvent);
        rvEventView.setAdapter(listEventAdapter);
    }

    private void addData() {
        getReference.child("User").child(MainActivity.uid).child("Event").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Event e = new Event(eventPicture, eventName, eventDesc, eventLocation, eventDate, eventLatitude, eventLongitude,eventId );
                    if(today.after(eventDate))
                        listEvent.add(e);
                }
                showRecyclerList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init(View view) {
        FirebaseDatabase getDatabase = FirebaseDatabase.getInstance();

        rvEventView = view.findViewById(R.id.rv_events);
        rvEventView.setHasFixedSize(true);
        rvEventView.setLayoutManager(layoutManager);
        getReference = getDatabase.getReference();
    }
}
