package com.example.mylovebeverage.Notify;

import java.util.ArrayList;

public class Notifier {
    private String messgae;
    private String type_position;
    private ArrayList<Subscriber> subscribers;

    public Notifier() {
        this.messgae = "";
        this.type_position = "";
        this.subscribers = new ArrayList<>();
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public String getMessage() {
        return messgae;
    }

    public void setMessage(String messgae) {
        this.messgae = messgae;
    }

    public String getType_position() {
        return type_position;
    }

    public void setType_position(String type_position) {
        this.type_position = type_position;
    }

    public void Notify() {
        if (getType_position().equals("Bartender")) {
            subscribers.get(0).update(this);
        } else if (getType_position().equals("Waitress")) {
            subscribers.get(1).update(this);
        } else if (getType_position().equals("Security")) {
            subscribers.get(2).update(this);
        } else if (getType_position().equals("Order")) {
            subscribers.get(3).update(this);
        } else {
            for (int i = 0; i < subscribers.size(); i++) {
                subscribers.get(i).update(this);
            }
        }
    }
}
