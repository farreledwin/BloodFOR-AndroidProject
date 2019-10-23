package edu.bluejack19_1.BloodFOR.interfacs;

import edu.bluejack19_1.BloodFOR.Model.Event;
import edu.bluejack19_1.BloodFOR.Model.User;

public interface DataListener {
    public void gotoDetailFragment(Event event);
    public void gotoParticipateFragment(Event event);
}
