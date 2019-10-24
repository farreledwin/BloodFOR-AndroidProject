package edu.bluejack19_1.BloodFOR.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpamobile.R;

import java.util.ArrayList;
import edu.bluejack19_1.BloodFOR.Model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ListViewHolder> {

    private ArrayList<Review> listReview;
    private Context c;

    public ReviewAdapter(Context c,ArrayList<Review> listReview) {
        this.listReview = listReview;
        this.c = c;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_event, viewGroup, false);
        return new ReviewAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ListViewHolder holder, int position) {
        final Review review = listReview.get(position);
        final String userid = listReview.get(position).getUserID();
        final String reviewdesc = listReview.get(position).getReviewDesc();
        final String eventid = listReview.get(position).getEventID();

        holder.reviewname.setText(userid);
        holder.reviewdesc.setText(reviewdesc);
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView reviewname, reviewdesc;
        LinearLayout review;

        private ListViewHolder(@NonNull final View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.item_place);
            reviewname = itemView.findViewById(R.id.reviewerName);
            reviewdesc = itemView.findViewById(R.id.reviewerDesc);
        }
    }
}