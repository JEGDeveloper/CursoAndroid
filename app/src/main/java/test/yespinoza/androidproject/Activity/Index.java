package test.yespinoza.androidproject.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import test.yespinoza.androidproject.Model.WhatsApp;
import test.yespinoza.androidproject.R;

import test.yespinoza.androidproject.Model.User;

public class Index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String ACTIVITY_CODE = "101";
    User oUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Intent oIntent = getIntent();
        oUser = (User) oIntent.getSerializableExtra("USER");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenWhatsAppDialog(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView oTextView = (TextView)navigationView.findViewById(R.id.tvDrawerName);
        if(oTextView != null)
            oTextView.setText("Prueba");
        oTextView = (TextView)navigationView.findViewById(R.id.tvDrawerEmail);
        if(oTextView != null)
            oTextView.setText("correo@correo.com");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        switch (id){
            case R.id.action_settings:
                Toast.makeText(this, getString(R.string.OptionNoImplemented), Toast.LENGTH_SHORT).show();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void OpenWhatsAppDialog(View view){
        try{
            final Dialog oDialog = new Dialog(this);

            oDialog.setContentView(R.layout.dialog_whatsapp_message);
            oDialog.setTitle("WhatsApp Message");
            Button button = (Button) oDialog.findViewById(R.id.DialogWhatsBtnAceptar);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String phoneNumber = ((TextView)oDialog.findViewById(R.id.DialogWhatsEtPhoneNumber)).getText().toString().trim();
                        String message = ((TextView) oDialog.findViewById(R.id.DialogWhatsEtMessage)).getText().toString().trim();
                        if(phoneNumber.equals("") && message.equals("")) {
                            Toast.makeText(view.getContext(), "Digite el número de teléfono y/o mensaje que desea enviar.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(phoneNumber.trim().equals("")) {
                            Intent oIntent = new Intent(Intent.ACTION_SEND);
                            oIntent.setType(WhatsApp.TYPE_TEXTPLAIN);
                            PackageInfo oPackageInfo = getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                            oIntent.setPackage("com.whatsapp");
                            oIntent.putExtra(Intent.EXTRA_TEXT, message);
                            startActivity(Intent.createChooser(oIntent, "Share with"));
                        }
                        else if(message.equals("")) {
                            Intent sendIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:" + "" + phoneNumber ));
                            sendIntent.setPackage("com.whatsapp");
                            startActivity(sendIntent);
                        }
                        else{

                            phoneNumber = phoneNumber.contains("+") ? phoneNumber : "+506".concat(phoneNumber);
                            Intent oIntent = new
                                    Intent(Intent.ACTION_VIEW);
                            oIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone=:"
                                    + phoneNumber
                                    + "&text=" + message));
                            oIntent.setPackage("com.whatsapp");
                            startActivity(oIntent);
                        }
                    }
                    catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(view.getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                                .show();
                    }
                    catch (Exception oException){

                        Snackbar.make(view, getString(R.string.OptionNoImplemented), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    oDialog.dismiss();
                }
            });
            button = (Button) oDialog.findViewById(R.id.DialogWhatsBtnCancelar);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    oDialog.dismiss();
                }
            });
            oDialog.show();
        }
        catch (Exception oException){

            Snackbar.make(view, getString(R.string.OptionNoImplemented), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
    private void Logout() {
        Intent oIntent = new Intent(this, Login.class);
        startActivity(oIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.DrawerOptionLogout) {
            Logout();
        }
        else if (id == R.id.DrawerOptionConfig) {
            Toast.makeText(this, getString(R.string.OptionNoImplemented), Toast.LENGTH_SHORT).show();
        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
