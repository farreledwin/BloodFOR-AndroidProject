package edu.bluejack19_1.BloodFOR.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tpamobile.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import edu.bluejack19_1.BloodFOR.Model.Event;

public class UpdateDetailFragment extends Fragment {

    private DatabaseReference getReference;
    private Button deleteBtn,updateBtn;
    private Event event;
    private EditText eventName,eventLocation,eventDate,eventDesc;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public UpdateDetailFragment(Event event){
        this.event = event;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_detail, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setEventName(eventName.getText().toString());
                event.setEventLocation(eventLocation.getText().toString());
                event.setEventDesc(eventDesc.getText().toString());
                try {
                    event.setEventDate(formatter.parse(eventDate.getText().toString()));

                } catch (Exception ignored) {
                }
                updateEvent(event);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReference.child("Event").orderByChild("eventName").equalTo(event.getEventName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            d.getRef().removeValue();
                        }
                        loadFragment(new HomeFragment(), false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private boolean loadFragment(Fragment fragment, boolean check) {
        if (fragment != null && check) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment).addToBackStack(null).commit();
            return true;
        }else if (fragment != null && !check) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment).commit();
            return true;
        }
        return false;
    }

    private void updateEvent(Event event) {
        DatabaseReference db = getReference.child("Event").child(event.getEventID());
        db.child("eventName").setValue(event.getEventName());
        db.child("eventDesc").setValue(event.getEventDesc());
        db.child("eventDate").setValue(formatter.format(event.getEventDate()));
        db.child("eventLocation").setValue(event.getEventLocation());
        Toast.makeText(getActivity(),"Update Data Success", Toast.LENGTH_LONG).show();
        loadFragment(new HomeFragment(),false);
    }

    private void init(View view){
        FirebaseDatabase getDatabase = FirebaseDatabase.getInstance();
        getReference = getDatabase.getReference();
        deleteBtn = view.findViewById(R.id.delete_event_button);
        eventName = view.findViewById(R.id.eventNameTxt);
        eventLocation = view.findViewById(R.id.eventLocationTxt);
        eventDate = view.findViewById(R.id.eventDateTxt);
        eventDesc = view.findViewById(R.id.eventDescTxt);
        updateBtn = view.findViewById(R.id.updateBtn);

        eventName.setText(event.getEventName());
        eventLocation.setText(event.getEventLocation());
        final String date = formatter.format(event.getEventDate());
        eventDate.setText(date);
        eventDesc.setText(event.getEventDesc());
    }
}
