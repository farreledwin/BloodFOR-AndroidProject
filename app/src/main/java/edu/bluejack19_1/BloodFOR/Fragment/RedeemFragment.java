package edu.bluejack19_1.BloodFOR.Fragment;

import android.content.Context;
import android.net.Uri;
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

import java.util.ArrayList;

import edu.bluejack19_1.BloodFOR.Adapter.RedeemAdapter;
import edu.bluejack19_1.BloodFOR.Model.CodeReedem;

public class RedeemFragment extends Fragment {

    private RecyclerView rvreedem;
    private ArrayList<CodeReedem> listReedem = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference getReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_redeem, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        addData();

    }
    private void init(View view) {
        FirebaseDatabase getDatabase = FirebaseDatabase.getInstance();

        rvreedem = view.findViewById(R.id.rv_reedem);
        rvreedem.setHasFixedSize(true);
        rvreedem.setLayoutManager(layoutManager);
        getReference = getDatabase.getReference();
    }

    private void addData() {
        getReference.child("User").child("4BL6nKoB7hYtj3MJEOUUi2L2EGv1").child("ReedemCode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listReedem.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String reedemtype = d.child("ReedemType").getValue(String.class);
                    String code = d.child("code").getValue(String.class);
                    CodeReedem cr = new CodeReedem(reedemtype,code);
                    listReedem.add(cr);
                }
                showRecyclerList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRecyclerList() {
        rvreedem.setLayoutManager(new LinearLayoutManager(getContext()));
        RedeemAdapter adapts = new RedeemAdapter(getContext(), listReedem);
        rvreedem.setAdapter(adapts);
    }
}
