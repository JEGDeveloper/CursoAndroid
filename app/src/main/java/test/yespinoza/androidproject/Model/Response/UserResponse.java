package test.yespinoza.androidproject.Model.Response;

import test.yespinoza.androidproject.Model.Entity.User;

public class UserResponse extends BaseResponse {
    private User Data;

    public User getData() {
        return Data;
    }

    public void setData(User data) {
        Data = data;
    }

    public UserResponse(){
        super();
    }
}
