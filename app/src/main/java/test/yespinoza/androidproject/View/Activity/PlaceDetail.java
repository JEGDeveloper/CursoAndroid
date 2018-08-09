package test.yespinoza.androidproject.View.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import test.yespinoza.androidproject.View.Fragment.FragmentFavoritePlaces;
import test.yespinoza.androidproject.View.Fragment.FragmentLocation;

public class PlaceDetail extends AppCompatActivity {
    public static String ACTIVITY_CODE = "97";
    public static Place place;
    TextView et_place_name;
    TextView et_place_description;
    TextView et_place_phone;
    ImageView img_place_detail;
    private ProgressDialog progress;
    private HttpClientManager proxy;
    private String parent_activity_code;

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
        showComments();
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            parent_activity_code = extras.getString("ACTIVITY_CODE");
        ((ImageView)findViewById(R.id.btnLike)).setImageDrawable(getDrawable(place.isFavorite()?R.drawable.ic_like:R.drawable.ic_dislike));
        LoadPlace();
    }

    @Override
    public void onBackPressed() {
        place=null;
        Intent intent = new Intent(this, Index.class);
        intent.putExtra("ACTIVITY_CODE", parent_activity_code != null ? parent_activity_code : ACTIVITY_CODE);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                place=null;
                Intent intent = new Intent(this, Index.class);
                intent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
                startActivity(intent);
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
            if(place.getImage() != null && !place.getImage().equals(""))
            img_place_detail.setImageBitmap(Helper.fromBase64ToBitmap(place.getImage()));
        }catch (Exception ex){

        }
    }

    private void showComments(){
        if(true){
            //No comments
            ((TextView)findViewById(R.id.tv_place_no_comments)).setVisibility(View.VISIBLE);
        }else{
            //Cargar CardViews
        }
    }

    public void openWaze(View view) {
        try {
            String uri = getString(R.string.waze, place.getLocation());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        } catch (Exception e) {
            if (e instanceof ActivityNotFoundException)
                Toast.makeText(getApplicationContext(),"La aplicación no está instalada.", Toast.LENGTH_SHORT).show();
        }
    }

    public void ManageFavoritePlace(View view) {
        try {
            ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

            Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    BaseResponse oResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                        ((ImageView) findViewById(R.id.btnLike)).setImageDrawable(getDrawable(place.isFavorite() ? R.drawable.ic_like : R.drawable.ic_dislike));
                        //finish();
                    } else {
                        place.setFavorite(!place.isFavorite());
                        Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }
            };

            Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    place.setFavorite(!place.isFavorite());
                    Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            };
            ManageFavoritePlaceRequest oRequest = new ManageFavoritePlaceRequest();
            oRequest.setPlaceId(place.getId());
            oRequest.setUserName(Project.getInstance().getCurrentUser().getUserName());
            place.setFavorite(!place.isFavorite());
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
