package test.yespinoza.androidproject.Model;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class WhatsApp {
    private String phonNumber;
    private String message;
    private static String packageName = "com.whatsapp";
    public static final String TYPE_TEXTPLAIN = "text/plain";

    public WhatsApp() {
        this.phonNumber = "";
        this.message = "";
    }

    public WhatsApp(String phonNumber, String message) {
        this.phonNumber = phonNumber;
        this.message = message;
    }

    public String getPhonNumber() {
        return phonNumber;
    }

    public void setPhonNumber(String phonNumber) {
        this.phonNumber = phonNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Intent openWhatsApp(PackageManager oPackageManager){
        return null;
    }
}
