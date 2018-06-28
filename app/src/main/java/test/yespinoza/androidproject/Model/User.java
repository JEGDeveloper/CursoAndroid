package test.yespinoza.androidproject.Model;

import java.io.Serializable;

public class User  extends Person  implements Serializable{
    private String userName;
    private String password;

    public User(String pUserName, String pPassword){
        super();
        this.userName = pUserName;
        this.password = pPassword;
    }

    public User(String pId, String pName, String pLastName, String pEmail, String pPhone, String pDateOfBirth, String pAddress){
        super(pId, pName, pLastName, pEmail, pPhone, pDateOfBirth, pAddress);
        this.userName = pId;
        this.password = "";
    }
    public User(){
        super();
        userName = "";
        password = "";
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
