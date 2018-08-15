package test.yespinoza.androidproject.View.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import test.yespinoza.androidproject.Model.Entity.Place;
import test.yespinoza.androidproject.Model.Response.BaseResponse;
import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;
import test.yespinoza.androidproject.Model.Utils.HttpClientManager;
import test.yespinoza.androidproject.R;
import test.yespinoza.androidproject.View.Fragment.FragmentLocation;

public class AddPlace extends AppCompatActivity {
    public static String ACTIVITY_CODE = "98";
    private ProgressDialog progress;
    private HttpClientManager proxy;
    public ImageView img;
    public Intent i;
    public Bitmap bmp;
    public final static int CONS=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        getSupportActionBar().setTitle(getString(R.string.nuevoSitio));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress  = new ProgressDialog(this);
        proxy = new HttpClientManager(this);
        img=(ImageView)findViewById(R.id.img_place_detail);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK)
        {
            Bundle ext = data.getExtras();
            bmp = (Bitmap)ext.get("data");
            img.setImageBitmap(bmp);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Index.class);
        intent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, Index.class);
                intent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
    public void createPlace(View view){
        String name = ((EditText)findViewById(R.id.et_place_name)).getText().toString().trim();
        String description = ((EditText)findViewById(R.id.et_place_description)).getText().toString().trim();
        String phone = ((EditText)findViewById(R.id.et_place_phone)).getText().toString().trim();

        Place place = new Place();
        place.setName(name);
        place.setDescription(description);
        place.setPhone(phone);
        place.setLatitude(FragmentLocation.latitude);
        place.setLongitude(FragmentLocation.longitude);

        if(name.equals("") || description.equals("")){
            Toast.makeText(this,getString(R.string.completeFieldsMsg),Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.length()>0 && phone.length()<8){
            Toast.makeText(this, "Teléfono Inválido",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

            Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    BaseResponse oResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                        //FragmentLocation.instance.loadPlaces();
                        Intent intent = new Intent(getApplicationContext(), Index.class);
                        intent.putExtra("ACTIVITY_CODE", ACTIVITY_CODE);
                        startActivity(intent);
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

            proxy.BACKEND_API_POST(HttpClientManager.BKN_CREATE_PLACE, new JSONObject(new Gson().toJson(place)), callBack_OK, callBack_ERROR);

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
    public void addImage(View v){
            int id;
        id=v.getId();
        switch (id){
            case R.id.img_place_detail:
            i= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,CONS);
            break;
        }
    }

    public void convertImageToBase64(Bitmap bitmap){
        ImageView image =(ImageView)findViewById(R.id.img_place_detail);
        //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void convertBase64ToImage(String pCadena){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        String imageString = pCadena;
        //decode base64 string to image
        imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        img.setImageBitmap(decodedImage);
    }
}
