package test.yespinoza.androidproject.Model.Request;

import test.yespinoza.androidproject.Model.Entity.Place;

public class ManageFavoritePlaceRequest {
    private String userName;
    private int placeId;
    private double score;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPlaceId() {
        return placeId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
}
