package test.yespinoza.androidproject.View.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import test.yespinoza.androidproject.Model.Response.UserResponse;
import test.yespinoza.androidproject.Model.Entity.User;
import test.yespinoza.androidproject.Model.Utils.Helper;
import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;
import test.yespinoza.androidproject.Model.Utils.HttpClientManager;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;

public class Login extends AppCompatActivity {
    public static final String ACTIVITY_CODE = "100";
    private final int RC_SIGN_IN = 10;
    private SharedPreferences myPreferences;
    private User oUser;
    private Response.Listener<JSONObject> callBack_OK;
    private Response.ErrorListener callBack_ERROR;
    private ProgressDialog progress;
    private HttpClientManager proxy;
    private FirebaseAuth mAuth;
    private boolean isBKNAuthentication = true;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Project.getInstance().setCurrentUser(null);
            Project.getInstance().setCurrentActivity(this);
            proxy = new HttpClientManager(this);
            setContentView(R.layout.activity_login);
            getSupportActionBar().hide();
            //Se obtienen los valores Guardados
            progress  = new ProgressDialog(this);
            myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
            String userName = myPreferences.getString("UserName", "");
            String password = myPreferences.getString("Password", "");
            ((EditText) findViewById(R.id.txtUserName)).setText(userName);
            ((EditText) findViewById(R.id.txtPassword)).setText(password);
            if(!userName.equals("") && !password.equals(""))
                Login(null);
            mAuth = FirebaseAuth.getInstance();

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();


        } catch (Exception ex) {


        }
    }

    public void Login(View view) {
        String email = ((EditText) findViewById(R.id.txtUserName)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString().trim();
        isBKNAuthentication = true;
        if (email.equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoUsername), Toast.LENGTH_LONG).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoPassword), Toast.LENGTH_LONG).show();
            return;
        }

        oUser = new User();
        oUser.setEmail(email);
        oUser.setPassword(password);
        userAuthentication(oUser);
    }

    private void userAuthentication(User user) {
        try {
            ShowProgressDialog(getString(R.string.user_validation_title), getString(R.string.user_validation_description));
            callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    UserResponse oResponse = new Gson().fromJson(response.toString(), UserResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {

                        myPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor myEditor = myPreferences.edit();
                        oUser = oResponse.getData();
                        if (oUser != null) {
                            myEditor.putString("UserName", user.getUserName());
                            if(isBKNAuthentication)
                                myEditor.putString("Password", user.getPassword());
                            myEditor.commit();
                            IndexRedirection();
                        } else {
                            LoginFailed();
                        }
                    } else
                        LoginFailed();
                    DismissProgressDialog();
                }
            };

            callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LoginFailed();
                    DismissProgressDialog();
                }
            };

            proxy.BACKEND_API_POST(HttpClientManager.BKN_GET_USER, new JSONObject(new Gson().toJson(user)), callBack_OK, callBack_ERROR);
        } catch (Exception oException) {
            LoginFailed();
            DismissProgressDialog();
        }
    }

    public void Facebook_Login(View view){
        Toast.makeText(this,getString(R.string.OptionNoImplemented), Toast.LENGTH_SHORT).show();
    }

    public void Google_Login(View view){
        isBKNAuthentication = false;

        ShowProgressDialog(getString(R.string.user_validation_title), getString(R.string.user_validation_description));
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //Toast.makeText(this,getString(R.string.OptionNoImplemented), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mGoogleSignInClient.signOut();
                User userToAuthenticate = new User();
                userToAuthenticate.setEmail(account.getEmail());
                userToAuthenticate.setUserName(account.getEmail());
                userToAuthenticate.setName(account.getGivenName());
                userToAuthenticate.setLastName(account.getFamilyName());
                if(account.getPhotoUrl() != null)
                    userToAuthenticate.setPicture(Helper.fromUriToBase64(account.getPhotoUrl().toString()));
                userAuthentication(userToAuthenticate);
            } catch (ApiException e) {
                LoginFailed();
                DismissProgressDialog();
            }
        }
    }

    private void IndexRedirection() {
        Intent oIntent = new Intent(this, Index.class);
        Project.getInstance().setCurrentUser(oUser);
        oIntent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
        startActivity(oIntent);
        finish();
    }

    public void CreateUserRedirection(View pView) {
        Intent oIntent = new Intent(this, PersonalInfo.class);
        oIntent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
        startActivity(oIntent);
    }


    private void LoginFailed(){
        ((EditText) Project.getInstance().getCurrentActivity().findViewById(R.id.txtPassword)).setText("");
        Toast.makeText(Project.getInstance().getCurrentActivity(), getString(R.string.LoginTstFailed), Toast.LENGTH_LONG).show();
        if(oUser != null)
            oUser.setPassword("");
    }

    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        progress.show();
    }

    private void DismissProgressDialog(){
        progress.dismiss();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public void FirebaseSignIn(View view){
        isBKNAuthentication = false;
        String email = ((EditText) findViewById(R.id.txtUserName)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString().trim();
        if (email.equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoUsername), Toast.LENGTH_LONG).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(this, getString(R.string.LoginTstNoPassword), Toast.LENGTH_LONG).show();
            return;
        }

        try {

            ShowProgressDialog(getString(R.string.user_validation_title), getString(R.string.user_validation_description));
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser() != null) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                User userToAuthenticate = new User();
                                userToAuthenticate.setEmail(firebaseUser.getEmail());
                                userToAuthenticate.setName(firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "");
                                userToAuthenticate.setPhone(firebaseUser.getPhoneNumber() != null ? firebaseUser.getPhoneNumber() : "");
                                userAuthentication(userToAuthenticate);
                            }else {
                                LoginFailed();
                                DismissProgressDialog();
                            }
                        } else {
                            LoginFailed();
                            DismissProgressDialog();
                        }
                    }
                });
        } catch (Exception oException) {
            LoginFailed();
            DismissProgressDialog();
        }
    }
}
