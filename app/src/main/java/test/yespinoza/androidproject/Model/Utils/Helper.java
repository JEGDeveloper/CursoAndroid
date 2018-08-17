package test.yespinoza.androidproject.Model.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import test.yespinoza.androidproject.R;

public class Helper {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String FAVORITE_ICON = "iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAQAAAC0NkA6AAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAADk8AAA5PATkrq4AAAAAHdElNRQfiCAISKRhFqePAAAADnUlEQVRYw7WXTUhUURSAvzcaqVOkCVMy0c9gaUgJtbEyLApaFOKm1kVhhFOB0LpIcSO40lqI7U1SCCwqIqyp1lmh+JdEZUyOkpqV48xr0XN697w3M++N0zmbd3/O/e695957ztNwInlUcJTDlLOZAmCRrwzzgme855ejEdKIl1p6mCKGLjTGFD3U4l0dQKOKPhYtw5t1kT6q0DJF5BPkS0rAin4hSH4miPW08tMRQkfnJ62sd4sooI2oY4SOTpQ2Ctz54hq/XSF0dH5zzY1vjhNOMlCcKFHiSVrDHLefs1U2cpdjltooQwwwSBjwsZcadrPG0uspZ5hxso7zNt54ywU2maak4eMCb208c94JYgMDli3qpTTR7sGT+C7lnmXrBtjgxB8LwqwXn2kFDTSYVuSjV/ResPeLKi3C6B27TK1lTDJJmalmF++ERUs6RD5PFINl6pX2JnR0mpS6epYVmyfpbr+fUeHwEmUdY+jojFNuqi1hULEZxa8OmisgxRQp5RBTeNhJgK1sp5oAADvoJMQkH5lglClessdkU0Qxn1Ot5ADzyqwuAT5eiw35t5mv8QGXlNp5DqiDegQkR7meMcLANF3M2U5pjjtMA2FiplqNnNSQORHpNCDOHRqZtiAiNNJFHPlu/EoypYT4xIFsSMDOivuzwLnE4A3i0PvUQeVKZhhSypXGQDrPxZs0wwC6MYFKpWVIvl4SskxIKVcnjmOJOHdFicPt55DSEmI5NQRCREylMuqMrwBeIM4E48QBr3GcoU65NRExTVsp4JGyw8NUAHATnQ/cIECA63xA5yYAFQwr/R85i49B8bL24wdO00K54SGNclo4DfjpFy920AkCSpkQl+6BsRopFTwQPSdMQSGlaLRb7vYIV9li8qCHLVxhxNKv3S7W2gf+au6LswQxxnjFGyP8VnKQUnmzmaXWidv/ylp6kuYkMZuEdUV7WOsUAXDSEh/T6wIn3SDAK06NE+13n3rX8cMV4kfi2rqQdS7X0s869xA4JQJYKp3nVCYIyKPbMaSbvMwgUEPEESJCTaYIyKXTEaTTJit2Ifv4lBbxif2rQYBmpHOptDnzP8YV2SZSN6mDbFstAqCepaSIJS5mAwGFPE4KeWx5rTOWE8zaImY5kS0E5NJhC+mw5NKrkjKbKDiiZClZkaD4k4xyOdsIKBRJw0MKsw+BI6a/+28c+R8I8NBkxPcYzTa5Z5akmFt85zu3KXZj9gfxnj5EHtT9jgAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxOC0wOC0wMlQxODo0MToyNC0wNDowMDp9PKoAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTgtMDgtMDJUMTg6NDE6MjQtMDQ6MDBLIIQWAAAAGXRFWHRTb2Z0d2FyZQB3d3cuaW5rc2NhcGUub3Jnm+48GgAAAABJRU5ErkJggg==";
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

    public static Bitmap fromBase64ToBitmap(String encode){
        try {
            byte[] decodedString = Base64.decode(encode, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        catch (Exception ex){
            return null;
        }
    }

    public static Bitmap fromBase64ToBitmap_Scalad(String encode, int dstWidth, int dstHeight) {
        try {
            byte[] decodedString = Base64.decode(encode, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return Bitmap.createScaledBitmap(bitmap, 96, 96, false);
        } catch (Exception ex) {
            return null;
        }
    }


    public static String fromBitmapToBase64(Bitmap bitmap){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }catch(Exception ex){
            return null;
        }
    }

    public static Bitmap fromUriToBitmap(String uri){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(uri);
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            return bitmap;
        }catch (Exception ex) {
            return null;
        }
    }

    public static String fromUriToBase64(String uri){
        try {
            String image = fromBitmapToBase64(fromUriToBitmap(uri));
            return image;
        }catch (Exception ex) {
            return null;
        }
    }
}

