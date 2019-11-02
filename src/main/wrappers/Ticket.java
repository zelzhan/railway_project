package main.wrappers;

public class Ticket {
    String client_id;
    String train_id;
    String dept_station;
    String dest_station;
    String dept_time;
    String dest_time;
    String dept_exact_time;
    String dest_exact_time;

    public Ticket(String client_id, String train_id, String dept_station, String dest_station, String dept_time, String dest_time, String dept_exact_time, String dest_exact_time) {
        this.client_id = client_id;
        this.train_id = train_id;
        this.dept_station = dept_station;
        this.dest_station = dest_station;
        this.dept_time = dept_time;
        this.dest_time = dest_time;
        this.dept_exact_time = dept_exact_time;
        this.dest_exact_time = dest_exact_time;
    }

}