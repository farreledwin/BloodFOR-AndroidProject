package edu.bluejack19_1.BloodFOR.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tpamobile.R;

import edu.bluejack19_1.BloodFOR.Fragment.HistoryFragment;
import edu.bluejack19_1.BloodFOR.Fragment.HomeFragment;
import edu.bluejack19_1.BloodFOR.Fragment.ProfileFragment;

public class VIewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private int numOfTabs;

    public VIewPagerAdapter(@NonNull FragmentManager fm,int numOfTabs, Context context) {
        super(fm);
        this.context = context;
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                    return new HomeFragment();
            case 1:
                    return new ProfileFragment();
//                    return new PointFragment();
            default:
                    return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return context.getString(R.string.history);
            case 1: return context.getString(R.string.points);
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
