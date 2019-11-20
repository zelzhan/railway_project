package main.wrappers;

import java.util.ArrayList;

public class CreateRoute {
    private ArrayList<String> stations;
    private String departureTime;


    public String getDepartureTime() {
        return departureTime;
    }

    public ArrayList<String> getStations() {
        return stations;
    }

    public void setStations(ArrayList<String> stations) {
        this.stations = stations;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}
