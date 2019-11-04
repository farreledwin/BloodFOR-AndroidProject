package edu.bluejack19_1.BloodFOR.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import edu.bluejack19_1.BloodFOR.R;

import edu.bluejack19_1.BloodFOR.Fragment.HistoryEventFragment;
import edu.bluejack19_1.BloodFOR.Fragment.ListItemRedeemFragment;
import edu.bluejack19_1.BloodFOR.Fragment.ProfileFragment;
import edu.bluejack19_1.BloodFOR.Fragment.RedeemFragment;

public class ViewPagerAdapter extends androidx.fragment.app.FragmentStatePagerAdapter {

    private Context context;
    private int numOfTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int numOfTabs, Context context) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.numOfTabs = numOfTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                    return new HistoryEventFragment();
            case 1:
                    return new RedeemFragment();
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
