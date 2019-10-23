package edu.bluejack19_1.BloodFOR.Model;

import java.util.Date;

public class Event {
    private String eventPicture;
    private String eventName;
    private String eventDesc;
    private String eventLocation;
    private Date eventDate;
    private Double eventLatitude;
    private Double eventLongitude;

    public String getEventPicture() {
        return eventPicture;
    }

    public void setEventPicture(String eventPicture) {
        this.eventPicture = eventPicture;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Double getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(Double eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public Double getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(Double eventLongitude) {
        this.eventLongitude = eventLongitude;
    }

    public Event(String eventPicture, String eventName, String eventDesc, String eventLocation, Date eventDate, Double eventLatitude, Double eventLongitude) {
        this.eventPicture = eventPicture;
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventLatitude = eventLatitude;
        this.eventLongitude = eventLongitude;
    }
}
