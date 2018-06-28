package test.yespinoza.androidproject.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;

import test.yespinoza.androidproject.Adapter.UserAdapter;
import test.yespinoza.androidproject.Fragment.JobFragment;
import test.yespinoza.androidproject.Fragment.ReferenceFragment;
import test.yespinoza.androidproject.Fragment.StudyFragment;
import test.yespinoza.androidproject.Model.Biometric.BiometricFace;
import test.yespinoza.androidproject.Model.Biometric.BiometricPerson;
import test.yespinoza.androidproject.Model.Biometric.ResponseBiometric_API;
import test.yespinoza.androidproject.Model.DAL.UserDAL;
import test.yespinoza.androidproject.Model.DAO.SQLiteDataBaseHelper;
import test.yespinoza.androidproject.Model.User;
import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;
import test.yespinoza.androidproject.Model.Utils.HttpClientManager;
import test.yespinoza.androidproject.R;

public class Login extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 100;
    public static final String ACTIVITY_CODE = "100";
    private final String DB_NAME = "DATA";
    private SharedPreferences myPreferences;
    private int REQUEST_FACE_DETECTION = 0;
    private CheckBox chkFaceLogin;
    private User oUser;
    private Response.Listener<JSONObject> callBack_OK;
    private Response.ErrorListener callBack_ERROR;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            UserAdapter.setCurrentUser(null);
            setContentView(R.layout.activity_login);
            getSupportActionBar().hide();
            //Se obtienen los valores Guardados
            progress  = new ProgressDialog(this);
            myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
            String userName = myPreferences.getString("UserName", "");
            String password = myPreferences.getString("Password", "");
            ((EditText) findViewById(R.id.txtUserName)).setText(userName);
            ((EditText) findViewById(R.id.txtPassword)).setText(password);
            chkFaceLogin = findViewById(R.id.chkFaceLogin);

            //Se inicializa la base de datos:
            SQLiteDataBaseHelper.initiatetInstance(this,DB_NAME,null,1);

            if(!userName.equals("") && !password.equals(""))
                Login(null);
            else{
                ClearSession();
            }
        } catch (Exception ex) {


        }
    }

    public void Login(View view) {
        oUser = new User(
                ((EditText) findViewById(R.id.txtUserName)).getText().toString().trim(),
                ((EditText) findViewById(R.id.txtPassword)).getText().toString().trim());
        if (oUser.getUserName().equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoUsername), Toast.LENGTH_LONG).show();
            return;
        }
        if (oUser.getPassword().equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoPassword), Toast.LENGTH_LONG).show();
            return;
        }

        oUser = UserDAL.userValidate(oUser);
        if (oUser != null) {
            myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
            SharedPreferences.Editor myEditor = myPreferences.edit();
            myEditor.putString("UserName", ((EditText) findViewById(R.id.txtUserName)).getText().toString());
            myEditor.putString("Password", ((EditText) findViewById(R.id.txtPassword)).getText().toString());
            myEditor.commit();
            REQUEST_FACE_DETECTION = 0;
            IndexRedirection();
        }else
        {
            ((EditText) findViewById(R.id.txtPassword)).setText("");
            Toast.makeText(this, getString(R.string.LoginTstFailed), Toast.LENGTH_LONG).show();
        }
        progress.dismiss();
    }

    public void TakePhoto(View view) {
        if (chkFaceLogin.isChecked()) {
            boolean networkConnetion = HttpClientManager.validateNetworkConnetion((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE));
            if (!networkConnetion) {
                chkFaceLogin.setChecked(false);
                Toast.makeText(this, R.string.NoInternetConnectionMsg, Toast.LENGTH_SHORT).show();
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    private void IndexRedirection() {
        Intent oIntent = new Intent(this, Index.class);
        UserAdapter.setCurrentUser(oUser);
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] imageBytes = stream.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //Se realiza el reconocimiento facial
            recognizeFace(encodedImage);
        }
        chkFaceLogin.setChecked(false);
    }

    private void recognizeFace(String Buffer) {
        try {

            ShowProgressDialog("Analizando rostro", "Espere mientras validamos sus datos");
            BiometricPerson oBiometricPerson = new BiometricPerson();
            BiometricFace oBiometricFace = oBiometricPerson.getPersonFace();
            oBiometricFace.setBuffer(Buffer);
            callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                    ResponseBiometric_API oResponse =  new Gson().fromJson(response.toString(),ResponseBiometric_API.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                        if (oResponse.getData().getDecision().toUpperCase().equals(ResponseBiometric_API.HIT)) {
                            myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                            SharedPreferences.Editor myEditor = myPreferences.edit();
                            oUser = UserDAL.consultarUser(Integer.parseInt(oResponse.getData().getIdCandidate()));
                            if(oUser != null) {
                                ((EditText) findViewById(R.id.txtUserName)).setText(oUser.getUserName());
                                ((EditText) findViewById(R.id.txtPassword)).setText(oUser.getPassword());
                                myEditor.putString("UserName", oUser.getUserName());
                                myEditor.putString("Password", oUser.getPassword());
                                myEditor.commit();
                                REQUEST_FACE_DETECTION = 0;
                                IndexRedirection();
                            }else{
                                faceDetectionFailed();
                            }
                        } else {
                            faceDetectionFailed();
                        }
                    }
                    progress.dismiss();
                    } catch (Exception oException) {
                        faceDetectionFailed();
                        chkFaceLogin.setChecked(false);
                    }
                }
            };

            callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    faceDetectionFailed();
                    progress.dismiss();
                }
            };
            HttpClientManager proxy = new HttpClientManager(this);
            proxy.Biometric_API_POST(ResponseBiometric_API.PERSON_MATCH_REQUEST,new JSONObject(new Gson().toJson(oBiometricPerson)),callBack_OK, callBack_ERROR);
        } catch (Exception oException) {
            faceDetectionFailed();
            chkFaceLogin.setChecked(false);
        }
    }

    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
    }

    private void ClearSession(){
        JobFragment.rootView = null;
        ReferenceFragment.rootView = null;
        StudyFragment.rootView = null;
    }

    private void faceDetectionFailed(){
        REQUEST_FACE_DETECTION++;
        if (REQUEST_FACE_DETECTION > 2) {
            chkFaceLogin.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), getString(R.string.LoginTstFaceDectFailed), Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.LoginTstFaceDectNoMatch), Toast.LENGTH_LONG).show();

        chkFaceLogin.setChecked(false);
        progress.dismiss();
    }


}
