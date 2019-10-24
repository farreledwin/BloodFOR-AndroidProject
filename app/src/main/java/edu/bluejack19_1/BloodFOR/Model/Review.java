package edu.bluejack19_1.BloodFOR.Model;

public class Review {
    private String EventID;
    private String UserID;
    private String reviewDesc;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }

    private String ReviewDesc;

    public String getReviewDesc() {
        return ReviewDesc;
    }

    public void setReviewDesc(String reviewDesc) {
        this.ReviewDesc = reviewDesc;
    }

    public String getEventID() {
        return EventID;
    }

    public Review(String eventID, String userID, String reviewDesc) {
        this.EventID = eventID;
        this.UserID = userID;
        this.ReviewDesc = reviewDesc;
    }

    public void setEventID(String eventID) {
        this.EventID = eventID;
    }
}
