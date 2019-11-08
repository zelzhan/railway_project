package main.wrappers;

import java.util.ArrayList;

public class Agent {


    String first_name;
    String last_name;
    String email;
    String schedule;
    int salary;
    String station;

    public Agent(String first_name, String last_name, String email, String schedule, int salary, String station) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.schedule = schedule;
        this.email = email;
        this.salary = salary;
        this.station = station;
    }

}
