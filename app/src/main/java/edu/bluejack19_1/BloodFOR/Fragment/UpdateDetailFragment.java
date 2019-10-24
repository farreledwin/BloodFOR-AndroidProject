package edu.bluejack19_1.BloodFOR.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tpamobile.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.bluejack19_1.BloodFOR.Model.Event;

public class UpdateDetailFragment extends Fragment {

    private DatabaseReference getReference;
    private Button deleteBtn;
    private Event event;

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

    public boolean loadFragment(Fragment fragment, boolean check) {
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

    public void init(View view){
        FirebaseDatabase getDatabase = FirebaseDatabase.getInstance();
        getReference = getDatabase.getReference();
        deleteBtn = view.findViewById(R.id.delete_event_button);
    }
}
