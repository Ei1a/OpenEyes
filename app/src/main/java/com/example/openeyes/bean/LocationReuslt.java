package com.example.openeyes.bean;

import java.util.ArrayList;
import java.util.List;

public class LocationReuslt {
    List<String> provinces;
    List<List<String>> cities;
    List<List<List<String>>> counties;

    public LocationReuslt(List<String> provinces, List<List<String>> cities, List<List<List<String>>> counties){
        this.provinces = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.counties = new ArrayList<>();
        this.provinces.addAll(provinces);
        this.cities.addAll(cities);
        this.counties.addAll(counties);
    }

    public List<String> getProvinces() {
        return provinces;
    }

    public List<List<String>> getCities() {
        return cities;
    }

    public List<List<List<String>>> getCounties() {
        return counties;
    }

    public void setProvinces(List<String> provinces) {
        this.provinces = provinces;
    }

    public void setCities(List<List<String>> cities) {
        this.cities = cities;
    }

    public void setCounties(List<List<List<String>>> counties) {
        this.counties = counties;
    }
}
