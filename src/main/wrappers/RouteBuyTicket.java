package main.wrappers;

import javax.swing.*;

public class RouteBuyTicket {
    private String authToken;
    private String train_id;
    private String start_station;
    private String end_station;
    private String destTime;
    private String deptTime;
    private String route_id;


    public RouteBuyTicket () {

    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setDestTime(String destTime) {
        this.destTime = destTime;
    }

    public void setDeptTime(String deptTime) {
        this.deptTime = deptTime;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getTrain_id() {
        return train_id;
    }

    public String getStart_station() {
        return start_station;
    }

    public String getEnd_station() {
        return end_station;
    }

    public String getDestTime() {
        return destTime;
    }

    public String getDeptTime() {
        return deptTime;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    public void setStart_station(String start_station) {
        this.start_station = start_station;
    }

    public void setEnd_station(String end_station) {
        this.end_station = end_station;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }
}
