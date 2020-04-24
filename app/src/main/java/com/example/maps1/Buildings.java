package com.example.maps1;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Buildings {

    private String Name, Description, Image;
    private ArrayList<String> Range_room_no;



    private GeoPoint Location;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    public ArrayList<String> getRange_room_no() {
        return Range_room_no;
    }

    public void setRange_room_no(ArrayList<String> range_room_no) {
        Range_room_no = range_room_no;
    }

    public GeoPoint getLocation() {
        return Location;
    }

    public void setLocation(GeoPoint location) {
        this.Location = location;
    }
}
