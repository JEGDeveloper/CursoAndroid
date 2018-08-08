package test.yespinoza.androidproject.Model.Response;

import java.util.ArrayList;

import test.yespinoza.androidproject.Model.Entity.Place;

public class PlacesResponse extends BaseResponse {

    private ArrayList<Place> Data;

    public ArrayList<Place> getData() {
        return Data;
    }

    public void setData(ArrayList<Place> data) {
        Data = data;
    }
}
