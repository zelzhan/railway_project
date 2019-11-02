package main.wrappers;

public class Route {
    String dep;
    String des;
    String train_id;
    String datey;
    String dateh;
    int route_id;

    public Route(String dep, String des, String train_id, String dateh, String datey, int route_id) {
        this.dep = dep;
        this.des = des;
        this.train_id = train_id;
        this.dateh = dateh;
        this.datey = datey;
        this.route_id = route_id;
    }
}