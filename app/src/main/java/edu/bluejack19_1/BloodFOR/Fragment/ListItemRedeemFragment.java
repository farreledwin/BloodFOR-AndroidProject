package edu.bluejack19_1.BloodFOR.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import edu.bluejack19_1.BloodFOR.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;
import edu.bluejack19_1.BloodFOR.Adapter.CustomListRedeemAdapter;
import edu.bluejack19_1.BloodFOR.MainActivity;

public class ListItemRedeemFragment extends Fragment {

    private FirebaseDatabase getDatabase;
    private DatabaseReference getReference;
    private String point;
    private Integer result;

    String[] menuItem = {"Voucher Alfamart","Voucher Sembako"};

    Uri[] uris = new Uri[]{Uri.parse("https://indomaret.co.id/logo_indomaret.png"),Uri.parse("https://cdn.brilio.net/news/2017/04/17/124502/615937-sembako.jpg")};

    ListView listview;


    public ListItemRedeemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_list_item_redeem, container, false);
        ListView listview = (ListView) rootView.findViewById(R.id.listReedem);
        final String[] items = new String[] {"Item 1", "Item 2", "Item 3"};
        CustomListRedeemAdapter adapter = new CustomListRedeemAdapter(getActivity(),menuItem,uris);
        listview.setAdapter(adapter);
        getDatabase = FirebaseDatabase.getInstance();
        getReference = getDatabase.getReference();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference db2 = getReference.child("User").child(MainActivity.uid).child("ReedemCode").push();
                final DatabaseReference db = getReference.child("User").child(MainActivity.uid);

                if(position == 1) {

                    final String code = randomVoucherCode();
                    db2.child("code").setValue(code);
                    db2.child("ReedemType").setValue("Sembako");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          point = dataSnapshot.child("point").getValue(String.class);
                          Log.d("Baki","msk");
                          result = (Integer.parseInt(point) - 5);
                          db.child("point").setValue(result.toString());
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Success Reedem Sembako " + code, Toast.LENGTH_LONG).show();
                }
                else if(position == 0) {
                    String code = randomVoucherCode();
                    db2.child("code").setValue(code);
                    db2.child("ReedemType").setValue("Indomaret");

                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            point = dataSnapshot.child("point").getValue(String.class);
                            result = (Integer.parseInt(point) - 7);
                            db.child("point").setValue(Integer.toString(result));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"Success Reedem Indomaret "+code,Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    private String randomVoucherCode() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 8; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
