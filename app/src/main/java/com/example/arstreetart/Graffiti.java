package com.example.arstreetart;

public class Graffiti {
    private String id;
    private String anchorPose;
    private double latitude;
    private double longitude;
    private String bitmapBase64;
    private long timestamp;

    // Konstruktor wymagany przez Firestore (musi być pusty)
    public Graffiti() {}

    // Konstruktor z parametrami
    public Graffiti(String anchorPose, double latitude, double longitude,
                    String bitmapBase64, long timestamp) {
        this.anchorPose = anchorPose;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bitmapBase64 = bitmapBase64;
        this.timestamp = timestamp;
    }

    // Gettery i Settery
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnchorPose() {
        return anchorPose;
    }

    public void setAnchorPose(String anchorPose) {
        this.anchorPose = anchorPose;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBitmapBase64() {
        return bitmapBase64;
    }

    public void setBitmapBase64(String bitmapBase64) {
        this.bitmapBase64 = bitmapBase64;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}