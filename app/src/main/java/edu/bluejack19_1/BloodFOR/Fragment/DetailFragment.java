package edu.bluejack19_1.BloodFOR.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tpamobile.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

import edu.bluejack19_1.BloodFOR.MainActivity;
import edu.bluejack19_1.BloodFOR.Model.Event;

public class DetailFragment extends Fragment {

    private Event event;
    private RecyclerView rvEventView;
    private TextView eventName, eventDesc, eventLocation, eventDate;
    private ImageView eventPhoto;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Button showMap, particiapteBtn;

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

        Glide.with(view.getContext())
                .load(picture)
                .apply(new RequestOptions().override(400, 400))
                .into(eventPhoto);

        particiapteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("User").child(MainActivity.uid).child("Event").push();
                db.setValue(event);
                loadFragment(new HomeFragment(), false);
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

    public DetailFragment(Event event){
       this.event = event;
    }

    private void init(View view){

        eventName = view.findViewById(R.id.event_name);
        eventPhoto = view.findViewById(R.id.event_photo);
        eventDesc = view.findViewById(R.id.event_desc);
        eventLocation = view.findViewById(R.id.event_location);
        eventDate = view.findViewById(R.id.event_date);
        particiapteBtn = view.findViewById(R.id.participate_button);
        showMap = view.findViewById(R.id.show_map_button);
    }
}
