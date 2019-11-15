package main.wrappers;

public class Route {
    String dep;
    String des;
    String train_id;
    String start_date;
    String end_date;
    int route_id;
    int capacity;

    public Route(String dep, String des, String train_id, String dateh, String datey, int route_id, int capacity) {
        this.dep = dep;
        this.des = des;
        this.train_id = train_id;
        this.start_date = dateh;
        this.end_date = datey;
        this.route_id = route_id;
        this.capacity = capacity;
    }
}