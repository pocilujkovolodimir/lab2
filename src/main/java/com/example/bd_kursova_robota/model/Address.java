package com.example.bd_kursova_robota.model;

public class Address {

    private Region region; //регіон
    private String city; //місто
    private String street; //вулиця
    private int building; //будинок
    private int apt; //квартира

    public Address(Region region, String city, String street, int building, int apt){
        this.region = region;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apt = apt;
    }

    public Region getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getApt() {
        return apt;
    }

    public int getBuilding() {
        return building;
    }
}
