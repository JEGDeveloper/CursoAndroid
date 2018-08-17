package test.yespinoza.androidproject.View.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import test.yespinoza.androidproject.Model.Entity.User;
import test.yespinoza.androidproject.Model.Utils.Helper;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;
public class PersonalInfo extends AppCompatActivity {
    private User oUser;
    public static final String ACTIVITY_CODE = "101";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        getSupportActionBar().setTitle(getString(R.string.personalInfoText));
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.btnlogin_shape));
        //getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.btnlogin_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Project.getInstance().setCurrentActivity(this);
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
    public void createUser(View pView) {

        String idNumber = ((EditText) findViewById(R.id.etIdNumber)).getText().toString();
        String name = ((EditText) findViewById(R.id.etName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.etLastName)).getText().toString();
        String email = ((EditText) findViewById(R.id.etEmail)).getText().toString().trim();
        String phone = ((EditText) findViewById(R.id.etPhone)).getText().toString();
        String dateOfBirth = ((EditText) findViewById(R.id.etDateOfBirth)).getText().toString().trim();
        String address = ((EditText) findViewById(R.id.etAddress)).getText().toString();

        if (idNumber.equals("") ||
                name.equals("") ||
                lastName.equals("") ||
                email.equals("") ||
                phone.equals("") ||
                dateOfBirth.equals("")||
                address.equals("")) {
            Toast.makeText(this, getString(R.string.completeFieldsMsg), Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Helper.emailValidation(email)){
            Toast.makeText(this, getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Helper.dateValidation(dateOfBirth)){
            Toast.makeText(this, getString(R.string.invalidDate), Toast.LENGTH_SHORT).show();
            return;
        }
        oUser = new User(Helper.TextFormat(idNumber), Helper.TextFormat(name), Helper.TextFormat(lastName), email, Helper.TextFormat(phone), dateOfBirth, Helper.TextFormat(address));
        activityRedirection();
    }

    private void activityRedirection() {
        Intent oIntent = new Intent(this, CreateUser.class);
        Project.getInstance().setCurrentUser(oUser);
        oIntent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
        oIntent.putExtra("USER", User.class);
        startActivity(oIntent);
    }
}
