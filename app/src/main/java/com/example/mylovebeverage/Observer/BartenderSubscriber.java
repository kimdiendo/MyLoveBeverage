package com.example.mylovebeverage.Observer;

public class BartenderSubscriber implements Subscriber {

    @Override
    public void update(Notifier notifier) {
        System.out.println(notifier.getMessgae().trim());
    }
}
