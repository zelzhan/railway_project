package main.wrappers;

import java.util.ArrayList;

public class Passenger {
    String first_name;
    String last_name;
    String phone;
    String email;

    ArrayList<Ticket> past;
    ArrayList<Ticket> future;

    public Passenger(String first_name, String last_name, String phone, String email, ArrayList<Ticket> past, ArrayList<Ticket> future) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.past = past;
        this.future = future;
    }
}

