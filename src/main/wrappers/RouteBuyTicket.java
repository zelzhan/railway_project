package main.wrappers;

import javax.swing.*;

public class RouteBuyTicket {
    private String authToken;
    private String train_id;
    private String start_station;
    private String end_station;
    private String destTime;
    private String deptTime;


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
}
