package test.yespinoza.androidproject.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import test.yespinoza.androidproject.Adapter.UserAdapter;
import test.yespinoza.androidproject.Model.DAL.UserDAL;
import test.yespinoza.androidproject.Model.Person;
import test.yespinoza.androidproject.Model.User;
import test.yespinoza.androidproject.R;

public class CreateUser extends AppCompatActivity {
    private User oUser;
    public static final String ACTIVITY_CODE = "102";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        getSupportActionBar().setTitle(getString(R.string.userCreationText));
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.btnlogin_shape));
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        oUser = UserAdapter.getCurrentUser();
        ((EditText) findViewById(R.id.etUserName)).setText(oUser.getId());
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
        UserAdapter.setCurrentUser(oUser);
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
                Toast.makeText(this, getString(R.string.completeFieldsMsg), Toast.LENGTH_SHORT);
                return;
            }

            if (!password.equals(passwordConfirm)) {
                Toast.makeText(this, getString(R.string.passwordsNoMatch), Toast.LENGTH_SHORT);
                return;
            }

            oUser.setPassword(password);
            oUser.setUserName(userName);
            if(UserDAL.insertarUser(oUser))
                activityRedirection();
            else
                Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT);
        }
        catch(Exception oException){
            Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT);
        }

    }
}
