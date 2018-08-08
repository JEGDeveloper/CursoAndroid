package test.yespinoza.androidproject.View.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

public class CreateUser extends AppCompatActivity {
    private User oUser;
    public static final String ACTIVITY_CODE = "102";
    private Response.Listener<JSONObject> callBack_OK;
    private Response.ErrorListener callBack_ERROR;
    private ProgressDialog progress;
    private HttpClientManager proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        getSupportActionBar().setTitle(getString(R.string.userCreationText));
        progress  = new ProgressDialog(this);
        oUser = Project.getInstance().getCurrentUser();
        ((EditText) findViewById(R.id.etUserName)).setText(oUser.getEmail());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    private void activityRedirection() {
        Intent oIntent = new Intent(this, Login.class);
        Project.getInstance().setCurrentUser(oUser);
        oIntent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
        startActivity(oIntent);
        finish();
    }

    public  void createUser(View pView){
        try {
            String userName = ((EditText) findViewById(R.id.etUserName)).getText().toString();
            String password = ((EditText) findViewById(R.id.etPassword1)).getText().toString();
            String passwordConfirm = ((EditText) findViewById(R.id.etPassword2)).getText().toString();

            if (userName.equals("") || password.equals("") || passwordConfirm.equals("")) {
                Toast.makeText(this, getString(R.string.completeFieldsMsg), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(passwordConfirm)) {
                Toast.makeText(this, getString(R.string.passwordsNoMatch), Toast.LENGTH_SHORT).show();;
                return;
            }

            oUser.setPassword(password);
            oUser.setUserName(userName);
            ShowProgressDialog(getString(R.string.user_create_title), getString(R.string.user_validation_description));
            callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    UserResponse oResponse = new Gson().fromJson(response.toString(), UserResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE)
                        activityRedirection();
                    else
                        Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            };

            callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            };
            proxy = new HttpClientManager(this);
            proxy.BACKEND_API_POST(HttpClientManager.BKN_CREATE_USER,new JSONObject(new Gson().toJson(oUser)),callBack_OK, callBack_ERROR);

        }
        catch(Exception oException){
            Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
        }
    }
    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
    }
}
