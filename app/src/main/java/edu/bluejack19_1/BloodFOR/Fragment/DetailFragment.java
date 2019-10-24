package edu.bluejack19_1.BloodFOR.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tpamobile.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import edu.bluejack19_1.BloodFOR.Adapter.ReviewAdapter;
import edu.bluejack19_1.BloodFOR.MainActivity;
import edu.bluejack19_1.BloodFOR.MapsActivity;
import edu.bluejack19_1.BloodFOR.Model.Event;
import edu.bluejack19_1.BloodFOR.Model.Review;

public class DetailFragment extends Fragment {

    private Event event;
    private RecyclerView rvEventView;
    private TextView eventName, eventDesc, eventLocation, eventDate;
    private ImageView eventPhoto;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Button showMap, participateBtn, insertreviewbtn;
    private EditText insertreviewtxt;
    private String GetUserID;
    private ArrayList<Review> listReview = new ArrayList<>();
    private RecyclerView rvReview;
    private DatabaseReference getReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        eventName.setText(event.getEventName());
        eventLocation.setText(event.getEventLocation());
        final String date = formatter.format(event.getEventDate());
        eventDate.setText(date);
        eventDesc.setText(event.getEventDesc());
        final String picture = event.getEventPicture();
        addReview();

        Glide.with(view.getContext())
                .load(picture)
                .apply(new RequestOptions().override(400, 400))
                .into(eventPhoto);

        participateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("User").child(MainActivity.uid).child("Event").push();
                db.child("eventPicture").setValue(event.getEventPicture());
                db.child("eventName").setValue(event.getEventName());
                db.child("eventDesc").setValue(event.getEventDesc());
                db.child("eventDate").setValue(formatter.format(event.getEventDate()).toString());
                db.child("eventLocation").setValue(event.getEventLocation());
                db.child("eventLatitude").setValue(event.getEventLatitude().toString());
                db.child("eventLongitude").setValue(event.getEventLongitude().toString());
                loadFragment(new HomeFragment(), false);
            }
        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), MapsActivity.class);
                myIntent.putExtra("name",event.getEventName());
                myIntent.putExtra("lat",event.getEventLatitude());
                myIntent.putExtra("long",event.getEventLongitude());
                startActivity(myIntent);
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        insertreviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertReview(new Review(event.getEventID(),GetUserID,insertreviewtxt.getText().toString()));
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

    public DetailFragment(Event event){
       this.event = event;
    }

    private void init(View view){
        eventName = view.findViewById(R.id.event_name);
        eventPhoto = view.findViewById(R.id.event_photo);
        eventDesc = view.findViewById(R.id.event_desc);
        eventLocation = view.findViewById(R.id.event_location);
        eventDate = view.findViewById(R.id.event_date);
        participateBtn = view.findViewById(R.id.participate_button);
        showMap = view.findViewById(R.id.show_map_button);
        insertreviewtxt = view.findViewById(R.id.insert_review);
        insertreviewbtn = view.findViewById(R.id.insert_review_button);
        FirebaseDatabase getDatabase = FirebaseDatabase.getInstance();
        FirebaseDatabase getDatabase2 = FirebaseDatabase.getInstance();
        getReference = getDatabase.getReference();
        rvReview = view.findViewById(R.id.rv_review);
        GetUserID = MainActivity.uid;
    }

    private void showRecyclerList() {
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        ReviewAdapter reviewadapt = new ReviewAdapter(getContext(), listReview);
        rvReview.setAdapter(reviewadapt);
    }

    private void insertReview(Review review) {
        getReference.child("Event").child(event.getEventID()).child("Review").push().setValue(review).addOnSuccessListener((Activity)getContext(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {

            }
        });
    }

    private void addReview() {
        FirebaseDatabase.getInstance().getReference().child("Event").child(event.getEventID()).child("Review").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listReview.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    d.getKey();
                    final String eventid = d.child("eventID").getValue(String.class);
                    final String userID = d.child("userID").getValue(String.class);
                    final String reviewdesc = d.child("reviewDesc").getValue(String.class);
                    FirebaseDatabase.getInstance().getReference().child("User").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String fullname = dataSnapshot.child("firstName").getValue(String.class);
                            Review review = new Review(eventid, fullname, reviewdesc);
                            listReview.add(review);
                            showRecyclerList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
