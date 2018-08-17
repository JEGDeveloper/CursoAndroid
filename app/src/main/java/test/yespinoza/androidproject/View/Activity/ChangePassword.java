package test.yespinoza.androidproject.View.Activity;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class ChangePassword extends AppCompatActivity {

    public static final String ACTIVITY_CODE = "205";
    private String currentPassword;
    private ProgressDialog progress;
    private User oUser;
    private TextView tvCurrentPassword;
    private EditText etCurrentPassword;
    private boolean withoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle(getString(R.string.btnChangePassword));
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.btnlogin_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Project.getInstance().setCurrentActivity(this);
        currentPassword = Project.getInstance().getCurrentUser().getPassword();
        tvCurrentPassword = findViewById(R.id.tvCurrentPassword);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        isUserWithoutPassword();
    }

    private void isUserWithoutPassword(){
        withoutPassword = currentPassword == null || currentPassword.equals("");
        if(withoutPassword){
            tvCurrentPassword.setVisibility(View.GONE);
            etCurrentPassword.setVisibility(View.GONE);
        }else{
            tvCurrentPassword.setVisibility(View.VISIBLE);
            etCurrentPassword.setVisibility(View.VISIBLE);
        }
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

    public void changePassword(View pView) {
        String vCurrentPassword = ((EditText) findViewById(R.id.etCurrentPassword)).getText().toString().trim();
        String vNewPassword = ((EditText) findViewById(R.id.etPassword1)).getText().toString().trim();
        String vNewPasswordConfirm = ((EditText) findViewById(R.id.etPassword2)).getText().toString().trim();
        try {
            if ( (!withoutPassword && vCurrentPassword.equals("")) || vNewPassword.equals("") || vNewPasswordConfirm.equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.completeFieldsMsg), Toast.LENGTH_SHORT).show();
                return;
            } else if (!vNewPassword.equals(vNewPasswordConfirm)) {
                Toast.makeText(getApplicationContext(), getString(R.string.passwordsNoMatch), Toast.LENGTH_SHORT).show();
                return;
            } else if (!vCurrentPassword.equals(currentPassword)) {
                Toast.makeText(getApplicationContext(), getString(R.string.password_incorrect), Toast.LENGTH_SHORT).show();
                return;
            } else {
                oUser = Project.getInstance().getCurrentUser();
                oUser.setPassword(vNewPassword);

                //boolean userModified = UserDAL.modificarUser(oUser);
                //region BackEnd Called
                ShowProgressDialog(getString(R.string.user_update_title), getString(R.string.user_validation_description));
                Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserResponse oResponse = new Gson().fromJson(response.toString(), UserResponse.class);
                        if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                            currentPassword = vNewPassword;
                            Project.getInstance().setCurrentUser(oUser);
                            Project.getInstance().updateUserPreference(getApplicationContext());
                            Index.getInstance().userData();
                            Toast.makeText(getApplicationContext(), getString(R.string.SettingsSaveSuccess), Toast.LENGTH_LONG).show();
                            ((EditText)findViewById(R.id.etCurrentPassword)).setText("");
                            ((EditText)findViewById(R.id.etPassword1)).setText("");
                            ((EditText)findViewById(R.id.etPassword2)).setText("");
                            isUserWithoutPassword();
                        } else
                            Toast.makeText(getApplicationContext(), getString(R.string.SettingsSaveFailed), Toast.LENGTH_SHORT).show();
                        DismissProgressDialog();
                    }
                };

                Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                        DismissProgressDialog();
                    }
                };
                HttpClientManager proxy = new HttpClientManager(getApplicationContext());
                proxy.BACKEND_API_POST(HttpClientManager.BKN_CHANGE_PASSWORD, new JSONObject(new Gson().toJson(oUser)), callBack_OK, callBack_ERROR);
                //endregion
            }
        } catch (Exception oException) {
            Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
        }

    }

    private void ShowProgressDialog(String tittle, String message) {
        if (progress == null) {
            progress = new ProgressDialog(this);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            progress.setTitle(tittle);
            progress.setMessage(message);
            progress.setCancelable(false);
            progress.show();
        }
    }

    private void DismissProgressDialog(){
        progress.dismiss();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}