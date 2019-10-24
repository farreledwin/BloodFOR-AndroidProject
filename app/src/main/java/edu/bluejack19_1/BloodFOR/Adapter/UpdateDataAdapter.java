package edu.bluejack19_1.BloodFOR.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tpamobile.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.bluejack19_1.BloodFOR.Model.Event;
import edu.bluejack19_1.BloodFOR.interfacs.DataListener;

public class UpdateDataAdapter extends RecyclerView.Adapter<UpdateDataAdapter.ListViewHolder> {
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private ArrayList<Event> listEvent;
    private Context c;

    public UpdateDataAdapter(Context c, ArrayList<Event> list) {
        this.listEvent = list;
        this.c = c;
    }

    @NonNull
    @Override
    public UpdateDataAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_event, viewGroup, false);
        return new UpdateDataAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final Event event = listEvent.get(position);
        final String picture = event.getEventPicture();
        final String name = event.getEventName();
        final String desc = event.getEventDesc();
        final String location = event.getEventLocation();
        final Date date = event.getEventDate();
        final Double latitude = event.getEventLatitude();
        final Double longitude = event.getEventLongitude();
         Glide.with(holder.itemView.getContext())
                .load(picture)
                .apply(new RequestOptions().override(400, 400))
                .into(holder.eventPhoto);

        holder.event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataListener listener = (DataListener) c;
                listener.gotoDelete(event);
            }
        });

        final String dates = formatter.format(date) ;
        holder.eventName.setText(name);
        holder.eventDesc.setText(desc);
        holder.eventLocation.setText(location);
        holder.eventDate.setText(dates);
    }


    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder{
        ImageView eventPhoto;
        TextView eventName, eventDesc, eventLocation, eventDate;
        LinearLayout event;

        private ListViewHolder(@NonNull final View itemView) {
            super(itemView);
            event = itemView.findViewById(R.id.item_place);
            eventPhoto = itemView.findViewById(R.id.event_photo);
            eventName = itemView.findViewById(R.id.event_name);
            eventDesc = itemView.findViewById(R.id.event_desc);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventDate = itemView.findViewById(R.id.event_date);
        }
    }
}
