package test.yespinoza.androidproject.View.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import org.json.JSONObject;

import test.yespinoza.androidproject.Model.Response.UserResponse;
import test.yespinoza.androidproject.Model.Entity.User;
import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;
import test.yespinoza.androidproject.Model.Utils.HttpClientManager;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;

public class Login extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 100;
    public static final String ACTIVITY_CODE = "100";
    private SharedPreferences myPreferences;
    private User oUser;
    private Response.Listener<JSONObject> callBack_OK;
    private Response.ErrorListener callBack_ERROR;
    private ProgressDialog progress;
    private HttpClientManager proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Project.getInstance().setCurrentUser(null);
            Project.getInstance().setCurrentActivity(this);
            proxy = new HttpClientManager(this);
            setContentView(R.layout.activity_login);
            getSupportActionBar().hide();
            //Se obtienen los valores Guardados
            progress  = new ProgressDialog(this);
            myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
            String userName = myPreferences.getString("UserName", "");
            String password = myPreferences.getString("Password", "");
            ((EditText) findViewById(R.id.txtUserName)).setText(userName);
            ((EditText) findViewById(R.id.txtPassword)).setText(password);


            if(!userName.equals("") && !password.equals(""))
                Login(null);

        } catch (Exception ex) {


        }
    }

    public void Login(View view) {
        String email = ((EditText) findViewById(R.id.txtUserName)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString().trim();
        if (email.equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoUsername), Toast.LENGTH_LONG).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoPassword), Toast.LENGTH_LONG).show();
            return;
        }

        try {

            ShowProgressDialog(getString(R.string.user_validation_title), getString(R.string.user_validation_description));
            oUser = new User();
            oUser.setEmail(email);
            oUser.setPassword(password);


            callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    UserResponse oResponse = new Gson().fromJson(response.toString(), UserResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {

                        myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor myEditor = myPreferences.edit();
                        oUser = oResponse.getData();
                        if (oUser != null) {
                            myEditor.putString("UserName", ((EditText) findViewById(R.id.txtUserName)).getText().toString());
                            myEditor.putString("Password", ((EditText) findViewById(R.id.txtPassword)).getText().toString());
                            myEditor.commit();
                            IndexRedirection();
                        } else {
                            LoginFailed();
                        }
                    }else
                        LoginFailed();
                    progress.dismiss();
                }
            };

            callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LoginFailed();
                    progress.dismiss();
                }
            };

            proxy.BACKEND_API_POST(HttpClientManager.BKN_GET_USER,new JSONObject(new Gson().toJson(oUser)),callBack_OK, callBack_ERROR);
        } catch (Exception oException) {
            LoginFailed();
            progress.dismiss();
        }
    }

    public void Facebook_Login(View view){
        Toast.makeText(this,getString(R.string.OptionNoImplemented), Toast.LENGTH_SHORT).show();
    }

    public void Google_Login(View view){
        Toast.makeText(this,getString(R.string.OptionNoImplemented), Toast.LENGTH_SHORT).show();
    }

    private void IndexRedirection() {
        Intent oIntent = new Intent(this, Index.class);
        Project.getInstance().setCurrentUser(oUser);
        oIntent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
        startActivity(oIntent);
        finish();
    }

    public void CreateUserRedirection(View pView) {
        Intent oIntent = new Intent(this, PersonalInfo.class);
        oIntent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
        startActivity(oIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void LoginFailed(){
        ((EditText) findViewById(R.id.txtPassword)).setText("");
        Toast.makeText(this, getString(R.string.LoginTstFailed), Toast.LENGTH_LONG).show();
        oUser.setPassword("");
    }

    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
    }
}
