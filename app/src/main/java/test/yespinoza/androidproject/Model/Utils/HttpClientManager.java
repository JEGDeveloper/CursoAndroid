package test.yespinoza.androidproject.Model.Utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;



public class HttpClientManager {
    //region Variables
    public static String URL_APP_BACKEND_API = "http://52.179.0.127:1011/APPWeb_API/";

    public static  String BKN_CREATE_USER = "User/CreateUser";
    public static String BKN_GET_USER = "User/GetUser";
    public static String BKN_UPDATE_USER = "User/UpdateUser";
    public static String BKN_GET_PLACES = "Location/GetPlaces";
    public static String BKN_GET_FAVORITE_PLACES = "Location/GetFavoritePlaces";
    public static String BKN_MANAGE_FAVORITE_PLACE = "Location/ManageFavoritePlace";
    public static String BKN_CREATE_PLACE = "Location/CreatePlace";
    //endregion
    private RequestQueue cola;

    public HttpClientManager(Context pContext){
        cola = Volley.newRequestQueue(pContext);
    }

    public void Get(String url, Response.Listener<String> callBack_OK, Response.ErrorListener callback_ERROR){
        StringRequest oRequest = new StringRequest(Request.Method.GET, url, callBack_OK, callback_ERROR);
        cola.add(oRequest);
    }


    public void GetJSON(String url, Response.Listener<JSONObject> callBack_OK, Response.ErrorListener callback_ERROR){
        JsonObjectRequest oRequest = new JsonObjectRequest(Request.Method.GET, url, null, callBack_OK, callback_ERROR);
        cola.add(oRequest);
    }

    public void BACKEND_API_POST(String method, JSONObject params,Response.Listener<JSONObject> callBack_OK, Response.ErrorListener callback_ERROR){
        JsonObjectRequest oRequest = new JsonObjectRequest(Request.Method.POST, URL_APP_BACKEND_API.concat(method), params, callBack_OK, callback_ERROR);
        oRequest.setRetryPolicy(new DefaultRetryPolicy(10000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        cola.add(oRequest);
    }

    public static boolean validateNetworkConnetion(ConnectivityManager pConnectivityManager) {
        NetworkInfo activeNetworkInfo = pConnectivityManager.getActiveNetworkInfo();
        boolean networkConnetion = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return networkConnetion;
    }

}