package test.yespinoza.androidproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import test.yespinoza.androidproject.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        CargarLogin();
        finish();
    }
    private void CargarLogin() {
        Intent mensajero = new Intent(this, Login.class);

        startActivityForResult(mensajero, 110);
    }
}
