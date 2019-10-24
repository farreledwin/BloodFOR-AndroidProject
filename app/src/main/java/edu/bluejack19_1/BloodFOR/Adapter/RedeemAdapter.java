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

import edu.bluejack19_1.BloodFOR.Model.CodeReedem;

public class RedeemAdapter extends RecyclerView.Adapter<RedeemAdapter.ListViewHolder>{
    private ArrayList<CodeReedem> listReedem;
    private Context c;

    public RedeemAdapter(Context c, ArrayList<CodeReedem> listReedem) {
        this.listReedem = listReedem;
        this.c = c;
    }

    @NonNull
    @Override
    public RedeemAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_redeem, viewGroup, false);
        return new RedeemAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedeemAdapter.ListViewHolder holder, int position) {
        final CodeReedem cr = listReedem.get(position);
        final String reedemtype = listReedem.get(position).getRedeemType();
        final String code = listReedem.get(position).getCodereedem();

        holder.reedemtype.setText(reedemtype);
        holder.reedemcode.setText(code);
    }

    @Override
    public int getItemCount() {
        return listReedem.size();
    }
    class ListViewHolder extends RecyclerView.ViewHolder{
        TextView reedemtype, reedemcode;
        LinearLayout event;

        private ListViewHolder(@NonNull final View itemView) {
            super(itemView);
            event = itemView.findViewById(R.id.item_place);
            reedemtype = itemView.findViewById(R.id.reedemType);
            reedemcode = itemView.findViewById(R.id.codeReedemResult);
        }
    }
}
