package com.example.mylovebeverage.Models;

import android.widget.ImageView;

public class Detail_Attendance {
    private String Staff_ID = "";
    private String Check_In_time = "";
    private int imageView;

    public Detail_Attendance(String staff_ID, String check_In_time, int imageView) {
        Staff_ID = staff_ID;
        Check_In_time = check_In_time;
        this.imageView = imageView;
    }

    public String getStaff_ID() {
        return Staff_ID;
    }

    public void setStaff_ID(String staff_ID) {
        Staff_ID = staff_ID;
    }

    public String getCheck_In_time() {
        return Check_In_time;
    }

    public void setCheck_In_time(String check_In_time) {
        Check_In_time = check_In_time;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }
}

