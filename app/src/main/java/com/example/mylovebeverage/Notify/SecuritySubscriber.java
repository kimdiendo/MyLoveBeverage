package com.example.mylovebeverage.Notify;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SecuritySubscriber implements Subscriber {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://check-attendance-e80df-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference("/Notification");
    DatabaseReference security = myRef.child("/Security");

    @Override
    public void update(Notifier notifier) {
        System.out.println(notifier.getMessage().trim());
        security.child(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now())).setValue(new SimpleDateFormat("Y-MM-dd").format(new Date()) + " " + notifier.getMessage().trim());
    }
}
