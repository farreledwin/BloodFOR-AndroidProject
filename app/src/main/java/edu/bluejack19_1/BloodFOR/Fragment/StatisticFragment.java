package edu.bluejack19_1.BloodFOR.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tpamobile.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import edu.bluejack19_1.BloodFOR.MainActivity;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class StatisticFragment extends Fragment {

    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat month_format = new SimpleDateFormat("MM");
    public static int [] yAxisData = new int[15];
    private int temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase getDatabase = FirebaseDatabase.getInstance();
        DatabaseReference getReference = getDatabase.getReference();

        LineChartView lineChartView = view.findViewById(R.id.chart);

        final List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
                "Oct", "Nov", "Dec"};

        for(int i = 0; i < 12; i++){
            final int finalI = i+1;
            getReference.child("User").child(MainActivity.uid).child("Event").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   int x = 0;
                   for(DataSnapshot d : dataSnapshot.getChildren()){
                       try {
                           Date date = formatter.parse(d.child("eventDate").getValue().toString());
                           assert date != null;
                           String month = month_format.format(date);
                           Date now = new Date();
                           if(Integer.parseInt(month) == finalI && date.before(now)){
                               x++;
                           }
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }
                   }
                   yAxisData[finalI] = x;
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
        }

        for(int i = 0; i < 12; i++) {
            Log.d("Baki",""+yAxisData[i+1]+" "+i +temp);
        }

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#8A0303"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < 12; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i+1]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#008489"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Total");
        yAxis.setTextColor(Color.parseColor("#1E90FF"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}
