package test.yespinoza.androidproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import test.yespinoza.androidproject.Adapter.UserAdapter;
import test.yespinoza.androidproject.Model.DAL.UserDAL;
import test.yespinoza.androidproject.Model.User;
import test.yespinoza.androidproject.R;

public class ChangePassword extends AppCompatActivity {

    public static final String ACTIVITY_CODE = "205";
    private String currentPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle(getString(R.string.btnChangePassword));
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.btnlogin_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentPassword = UserAdapter.getCurrentUser().getPassword();
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

    public void changePassword(View pView){
        String vCurrentPassword = ((EditText)findViewById(R.id.etCurrentPassword)).getText().toString().trim();
        String vNewPassword = ((EditText)findViewById(R.id.etPassword1)).getText().toString().trim();
        String vNewPasswordConfirm = ((EditText)findViewById(R.id.etPassword2)).getText().toString().trim();

        if(vCurrentPassword.equals("") || vNewPassword.equals("") || vNewPasswordConfirm.equals("")){
            Toast.makeText(getApplicationContext(),getString(R.string.completeFieldsMsg),Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!vNewPassword.equals(vNewPasswordConfirm)){
            Toast.makeText(getApplicationContext(),getString(R.string.passwordsNoMatch),Toast.LENGTH_SHORT).show();
            return;
        }

        else if(!vCurrentPassword.equals(currentPassword)){
            Toast.makeText(getApplicationContext(),getString(R.string.password_incorrect),Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            User oUser = UserAdapter.getCurrentUser();
            oUser.setPassword(vNewPassword);

            boolean userModified = UserDAL.modificarUser(oUser);
            Toast.makeText(getApplicationContext(),
                    getString(userModified ? R.string.SettingsSaveSuccess : R.string.SettingsSaveFailed),
                    Toast.LENGTH_LONG);
            if(userModified) {
                UserAdapter.setCurrentUser(oUser);
                UserAdapter.updateUserPreference(getApplicationContext());
                Index.getInstance().userData();
                this.finish();
            }
        }



    }
}
