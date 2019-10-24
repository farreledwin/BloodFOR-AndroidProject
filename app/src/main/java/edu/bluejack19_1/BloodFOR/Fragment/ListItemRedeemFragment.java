package edu.bluejack19_1.BloodFOR.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tpamobile.R;

import java.util.Objects;
import java.util.Random;
import edu.bluejack19_1.BloodFOR.Adapter.CustomListRedeemAdapter;

public class ListItemRedeemFragment extends Fragment {
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),"Success Reedem "+randomVoucherCode(),Toast.LENGTH_LONG).show();
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
