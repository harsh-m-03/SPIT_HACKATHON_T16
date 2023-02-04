package com.example.spit_hackathon_ecoquest.Models;

public class Events {
    String title, location, id, green_points, time, date;


    public Events() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGreen_points() {
        return green_points;
    }

    public void setGreen_points(String green_points) {
        this.green_points = green_points;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Events(String title, String location, String id, String green_points, String time, String date) {
        this.title = title;
        this.location = location;
        this.id = id;
        this.time=time;
        this.date=date;
        this.green_points = green_points;
    }
}
