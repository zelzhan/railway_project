package main.wrappers;

public class Agent {
    String first_name;
    String last_name;
    String phone;
    String email;
    String salary;
    String workingHours;
    String station;

    public Agent(String first_name, String last_name, String phone, String email, String salary, String workingHours){
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.workingHours = workingHours;
    }

    public Agent(String first_name, String last_name, String phone, String email, int salary, String station){
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.salary = String.valueOf(salary);
        this.station = station;
    }
}
