package test.yespinoza.androidproject.Model.Utils;

import java.util.HashMap;
import java.util.Map;

public class HttpApiResponse {
    public final static int SUCCES_CODE = 0;

    private int IdResponse;
    private String Code;
    private String Message;

    public static int getSuccesCode() {
        return SUCCES_CODE;
    }

    public int getIdResponse() {
        return IdResponse;
    }

    public void setIdResponse(int idResponse) {
        IdResponse = idResponse;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
