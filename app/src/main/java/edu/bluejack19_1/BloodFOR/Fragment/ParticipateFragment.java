package edu.bluejack19_1.BloodFOR.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.tpamobile.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.bluejack19_1.BloodFOR.MainActivity;
import edu.bluejack19_1.BloodFOR.Model.Event;

public class ParticipateFragment extends Fragment {

    private Event event;
    private RadioButton a,b,o,ab;
    private Button participateBtn;

    public ParticipateFragment(Event event) {
        this.event = event;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_participate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        participateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bloodType = "";
                if(a.isChecked()) bloodType = "A";
                else if(b.isChecked()) bloodType = "B";
                else if(ab.isChecked()) bloodType = "AB";
                else if (o.isChecked()) bloodType = "O";
                else bloodType = "-";

                if(bloodType.isEmpty()){
                    Toast.makeText(getContext(),"Must Choose Blood Type",Toast.LENGTH_LONG).show();
                }
                else{

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("User").child(MainActivity.uid).child("Event").push();
                    db.setValue(event,bloodType);
                    loadFragment(new HomeFragment(), false);
                }
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

    public void init(View view){
        a = view.findViewById(R.id.type_a);
        b = view.findViewById(R.id.type_b);
        ab = view.findViewById(R.id.type_ab);
        o = view.findViewById(R.id.type_o);
        participateBtn = view.findViewById(R.id.participate_button);
    }

}
