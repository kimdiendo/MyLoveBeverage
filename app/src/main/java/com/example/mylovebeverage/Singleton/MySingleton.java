package com.example.mylovebeverage.Singleton;

public class MySingleton {
    private static MySingleton instance;
    private String variable;

    private MySingleton() {
    }

    public static MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }
        return instance;
    }
    public String getVariable() {
        return variable;
    }
    public void setVariable(String variable) {
        this.variable = variable;
    }
}

