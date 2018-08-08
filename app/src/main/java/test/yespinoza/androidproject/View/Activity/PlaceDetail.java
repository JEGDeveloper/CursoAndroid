package test.yespinoza.androidproject.View.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import test.yespinoza.androidproject.Model.Entity.Place;
import test.yespinoza.androidproject.Model.Request.ManageFavoritePlaceRequest;
import test.yespinoza.androidproject.Model.Response.BaseResponse;
import test.yespinoza.androidproject.Model.Response.PlacesResponse;
import test.yespinoza.androidproject.Model.Utils.Helper;
import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;
import test.yespinoza.androidproject.Model.Utils.HttpClientManager;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;
import test.yespinoza.androidproject.View.Fragment.FragmentLocation;

public class PlaceDetail extends AppCompatActivity {

    public static Place place;
    EditText et_place_name;
    EditText et_place_description;
    EditText et_place_phone;
    ImageView img_place_detail;
    private ProgressDialog progress;
    private HttpClientManager proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        getSupportActionBar().setTitle("Detalle del Sitio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress  = new ProgressDialog(this);
        proxy = new HttpClientManager(this);
        img_place_detail = findViewById(R.id.img_place_detail);
        et_place_name = findViewById(R.id.et_place_name);
        et_place_description = findViewById(R.id.et_place_description);
        et_place_phone = findViewById(R.id.et_place_phone);
        Button button = findViewById(R.id.btnSend);
        button.setText(place.isFavorite() ? "Eliminar Favorito" : "Añadir Favorito");
        LoadPlace();
    }
    @Override
    public void onBackPressed() {
        place=null;
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                place=null;
                finish();
                break;
        }
        return true;
    }
    private void LoadPlace(){
        try{
            et_place_name.setText(place.getName());
            et_place_description.setText(place.getDescription());
            et_place_phone.setText(place.getPhone());
            img_place_detail.setImageBitmap(Helper.fromBase64ToBitmap(place.getImage()));
        }catch (Exception ex){

        }
    }

    public void ManageFavoritePlace(View view){
        try {
            ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

            Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    BaseResponse oResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                        place = null;
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            };

            Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            };
            ManageFavoritePlaceRequest oRequest = new ManageFavoritePlaceRequest();
            oRequest.setPlaceId(place.getId());
            oRequest.setUserName(Project.getInstance().getCurrentUser().getUserName());
            proxy.BACKEND_API_POST(HttpClientManager.BKN_MANAGE_FAVORITE_PLACE, new JSONObject(new Gson().toJson(oRequest)), callBack_OK, callBack_ERROR);

        } catch (Exception oException) {
            progress.dismiss();
        }
    }

    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
    }

}
