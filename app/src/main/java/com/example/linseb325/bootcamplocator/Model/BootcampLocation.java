package com.example.linseb325.bootcamplocator.Model;

/**
 * Created by linseb325 on 2/9/18.
 */

public class BootcampLocation {

    private float latitude;
    private float longitude;
    private String locationTitle;
    private String locationAddress;
    private String imageURL;

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getImageURL() {
        return imageURL;
    }

    public BootcampLocation(float latitude, float longitude, String locationTitle, String locationAddress, String imageURL) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationTitle = locationTitle;
        this.locationAddress = locationAddress;
        this.imageURL = imageURL;
    }
}
