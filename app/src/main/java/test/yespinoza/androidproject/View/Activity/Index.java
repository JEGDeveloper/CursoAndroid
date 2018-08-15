package test.yespinoza.androidproject.View.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import test.yespinoza.androidproject.View.Fragment.FragmentFavoritePlaces;
import test.yespinoza.androidproject.View.Fragment.FragmentLocation;
import test.yespinoza.androidproject.View.Fragment.FragmentSettings;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;

import test.yespinoza.androidproject.Model.Entity.User;

public class Index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String ACTIVITY_CODE = "103";
    User oUser;
    NavigationView navigationView;
    public static Index instance;
    private boolean onIndex = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Project.getInstance().setCurrentActivity(this);
        instance = this;
        setContentView(R.layout.activity_index);
        //Se asigna el usuario Actual
        oUser = Project.getInstance().getCurrentUser();
        navigationView = findViewById(R.id.nav_view);
        userData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        String parent_activity_code;
        if(extras != null) {
            parent_activity_code = extras.getString("ACTIVITY_CODE");
            if(parent_activity_code != null){
                if(parent_activity_code.equals(PlaceDetail.ACTIVITY_CODE) || parent_activity_code.equals(AddPlace.ACTIVITY_CODE))
                    mostrarFragment(new FragmentLocation());
                if(parent_activity_code.equals(FragmentFavoritePlaces.ACTIVITY_CODE)){
                    mostrarFragment(new FragmentFavoritePlaces());
                }
                if(parent_activity_code.equals(FragmentFavoritePlaces.ACTIVITY_CODE))
                    mostrarFragment(new FragmentFavoritePlaces());
            }
        }
    }

    public static Index getInstance() {
        return instance;
    }

    public void userData() {
        oUser = Project.getInstance().getCurrentUser();
        navigationView.setNavigationItemSelectedListener(this);
        TextView tvDrawerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvDrawerName);
        TextView tvDrawerEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvDrawerEmail);
        if(tvDrawerName != null)
            tvDrawerName.setText(oUser.getFullName());
        if(tvDrawerEmail != null)
            tvDrawerEmail.setText(oUser.getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showMenuOption(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void showMenuOption(int menuItemId){
        try {

            switch (menuItemId) {
                case R.id.action_settings:
                    mostrarFragment(new FragmentSettings());
                    onIndex = false;
                    break;
                case R.id.action_location:
                    if (ActivityCompat.checkSelfPermission(Project.getInstance().getCurrentActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Project.getInstance().getCurrentActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2022);
                    }else {
                        mostrarFragment(new FragmentLocation());
                        onIndex = false;
                    }
                    break;
                case R.id.action_logout:
                    Logout();
                    break;
                case R.id.action_favorite_locations:
                    mostrarFragment(new FragmentFavoritePlaces());
                    onIndex = false;
                    break;
                default:
                    mostrarFragment(new Fragment());
                    Toast.makeText(this, getString(R.string.OptionNoImplemented), Toast.LENGTH_SHORT).show();
                    onIndex = true;
                    break;
            }
        } catch (Exception ex)
        {
            Toast.makeText(this, getString(R.string.OptionNoImplemented), Toast.LENGTH_SHORT).show();
        }
    }

    private void Logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.LogoutDialogMessage))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.LogoutDialogAcept), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Index.this);
                        SharedPreferences.Editor myEditor = myPreferences.edit();
                        myEditor.putString("Password", "");
                        myEditor.commit();
                        Intent oIntent = new Intent(Index.this, Login.class);
                        Project.getInstance().setCurrentUser(null);
                        startActivity(oIntent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.LogoutDialogDecline), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void mostrarFragment(Fragment fragment) {
        FragmentTransaction transaccion = getSupportFragmentManager().beginTransaction();
        transaccion.replace(R.id.frmLayoutIndex, fragment);
        transaccion.commit();
    }

    @Override
    public void onBackPressed(){
        if(!onIndex) {
            startActivity(new Intent(this, Index.class));
        }
        onIndex = true;
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        showMenuOption(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
