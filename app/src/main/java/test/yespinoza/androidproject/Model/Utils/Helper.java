package test.yespinoza.androidproject.Model.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    public static String TextFormat(String param){
        if (param == null || param.isEmpty()) {
            return param;
        } else {
            param = param.trim();
            param = param.substring(0, 1).toUpperCase() + param.substring(1);
            char[] chars = param.toCharArray();
            for (int i = 0; i < param.length()- 2; i++)
                if (chars[i] == ' ' || chars[i] == '.' || chars[i] == ',')
                    chars[i + 1] = Character.toUpperCase(chars[i + 1]);

            return new String(chars);
        }
    }

    public static boolean dateValidation(String date){
        try{
            date = date.trim();
            String[] aux = date.split("/");
            if(aux[0].length() == 2 && aux[1].length() == 2 && aux[2].length() == 4) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                dateFormat.parse(date);

                return Integer.parseInt(aux[1].toString()) <= 12 &&  Integer.parseInt(aux[0].toString()) <= 31;
            }else{
                return false;
            }
        }catch (Exception ex){
            return false;
        }
    }

    public static boolean emailValidation(String email){
        try{
            email = email.trim();
            String[] aux = email.split("@");
            String mailType = aux[1].toString();
            String[] secondPart = mailType.split("\\.");
            if(aux[0].length() >= 1
                    && secondPart[0].length() >= 1
                    && secondPart[1].length() >= 1) {
               return !email.contains(" ");
            }else{
                return false;
            }
        }catch (Exception ex){
            return false;
        }
    }
}
