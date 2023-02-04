package com.example.spit_hackathon_ecoquest.Models;

public class CompletedTask {
    String day,task,image,green_points,user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGreen_points() {
        return green_points;
    }

    public void setGreen_points(String green_points) {
        this.green_points = green_points;
    }
}
