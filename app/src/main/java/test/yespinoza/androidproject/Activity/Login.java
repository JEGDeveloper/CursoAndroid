package test.yespinoza.androidproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import test.yespinoza.androidproject.Model.User;
import test.yespinoza.androidproject.R;

public class Login extends AppCompatActivity {
    public static String ACTIVITY_CODE = "100";
    SharedPreferences myPreferences;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_FACE_DETECTION = 0;
    CheckBox chkFaceLogin;
    User oUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
        ((EditText) findViewById(R.id.txtUserName)).setText(myPreferences.getString("UserName", ""));
        chkFaceLogin = ((CheckBox) findViewById(R.id.chkFaceLogin));
        chkFaceLogin.setChecked(myPreferences.getBoolean("FaceDetection", false));
        this.TakePhoto(null);

            findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Login(v);
                }
            });
        }
        catch (Exception ex) {



        }
    }

    public void Login(View view){
        boolean userValidation = true;
        oUser = new User(
                ((EditText)findViewById(R.id.txtUserName)).getText().toString().trim(),
                ((EditText)findViewById(R.id.txtPassword)).getText().toString().trim());
        if(oUser.getUserName().equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoUsername), Toast.LENGTH_LONG).show();
            userValidation = false;
        }
        if(oUser.getPassword().equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoPassword), Toast.LENGTH_LONG).show();
            userValidation = false;
        }
        //userValidation = {Método de Validar Usuario con BackEnd}
        if(userValidation) {
            myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
            SharedPreferences.Editor myEditor = myPreferences.edit();
            myEditor.putString("UserName", ((EditText) findViewById(R.id.txtUserName)).getText().toString());
            myEditor.putBoolean("FaceDetection", ((CheckBox) findViewById(R.id.chkFaceLogin)).isChecked());
            myEditor.commit();
            REQUEST_FACE_DETECTION = 0;
            IndexRedirection();
        }
        }

    public void TakePhoto(View view) {
        if(chkFaceLogin.isChecked()){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private void IndexRedirection(){
        Intent oIntent = new Intent(this, Index.class);
        oIntent.putExtra("USER",oUser);
        oIntent.putExtra("ACTIVITY_CODE",ACTIVITY_CODE);
        startActivity(oIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageBytes = stream.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            //Se llama el Web API
            /*pendiente...*/
            boolean isFaceDetected = false;
            if(isFaceDetected){

            }else{
                REQUEST_FACE_DETECTION++;
                if(REQUEST_FACE_DETECTION>2){
                    chkFaceLogin.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, getString(R.string.LoginTstFaceDectFailed), Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(this, getString(R.string.LoginTstFaceDectNoMatch), Toast.LENGTH_LONG).show();

                chkFaceLogin.setChecked(false);
                SharedPreferences.Editor myEditor = myPreferences.edit();
                myEditor.putBoolean("FaceDetection", false);
                myEditor.commit();
            }

        }
    }
}
