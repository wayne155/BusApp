package xyz.theoye.hellobus.logic.model;

import androidx.annotation.NonNull;

public class BusStationJava {

    String name;
    double latitude;
    double altitude;
    String city;
    BusStationJava(String name , double latitude, double altitude, String city){}

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public String getCity() {
        return city;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
