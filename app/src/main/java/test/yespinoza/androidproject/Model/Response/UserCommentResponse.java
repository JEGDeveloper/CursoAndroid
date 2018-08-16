package test.yespinoza.androidproject.Model.Response;

import java.util.ArrayList;

import test.yespinoza.androidproject.Model.Entity.UserComment;

public class UserCommentResponse extends BaseResponse {

    private ArrayList<UserComment> Data;

    public ArrayList<UserComment> getData() {
        return Data;
    }

    public void setData(ArrayList<UserComment> data) {
        Data = data;
    }
}
