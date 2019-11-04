package edu.bluejack19_1.BloodFOR.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import edu.bluejack19_1.BloodFOR.R;


import java.text.SimpleDateFormat;
import java.util.Objects;

import edu.bluejack19_1.BloodFOR.MapsActivity;
import edu.bluejack19_1.BloodFOR.Model.Event;

public class HistoryDetailFragment extends Fragment {

    private Event event;
    private RecyclerView rvEventView;
    private TextView eventName, eventDesc, eventLocation, eventDate;
    private ImageView eventPhoto;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Button showMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_detail, container, false);
    }

    public HistoryDetailFragment(Event event){
        this.event = event;
    }

    private void init(View view){
        eventName = view.findViewById(R.id.event_name);
        eventPhoto = view.findViewById(R.id.event_photo);
        eventDesc = view.findViewById(R.id.event_desc);
        eventLocation = view.findViewById(R.id.event_location);
        eventDate = view.findViewById(R.id.event_date);
        showMap = view.findViewById(R.id.show_map_button);
    }
}
