package test.yespinoza.androidproject.View.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.Rating;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import test.yespinoza.androidproject.Adapter.CardViewAdapter;
import test.yespinoza.androidproject.Adapter.CommentAdapter;
import test.yespinoza.androidproject.Model.Entity.CardView;
import test.yespinoza.androidproject.Model.Entity.Comment;
import test.yespinoza.androidproject.Model.Entity.Place;
import test.yespinoza.androidproject.Model.Entity.UserComment;
import test.yespinoza.androidproject.Model.Request.CommentPlaceRequest;
import test.yespinoza.androidproject.Model.Request.ManageFavoritePlaceRequest;
import test.yespinoza.androidproject.Model.Response.BaseResponse;
import test.yespinoza.androidproject.Model.Response.PlacesResponse;
import test.yespinoza.androidproject.Model.Response.UserCommentResponse;
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
    private TextView et_place_name;
    private TextView et_place_description;
    private TextView et_place_phone;
    private ImageView img_place_detail;
    private RatingBar ratingBar;
    private CommentAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private RecyclerView recycler;
    private ProgressDialog progress;
    private HttpClientManager proxy;
    private String parent_activity_code;
    private ArrayList<UserComment> listComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        getSupportActionBar().setTitle("Detalle del Sitio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Project.getInstance().setCurrentActivity(this);
        listComments = new ArrayList<>();
        adapter = null;
        proxy = new HttpClientManager(this);
        progress = new ProgressDialog(this);
        recycler = findViewById(R.id.recycler_comments);
        img_place_detail = findViewById(R.id.img_place_detail);
        et_place_name = findViewById(R.id.et_place_name);
        et_place_description = findViewById(R.id.et_place_description);
        et_place_phone = findViewById(R.id.et_place_phone);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setPlaceScore(v);
                return false;
            }
        });
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
                intent.putExtra("ACTIVITY_CODE", parent_activity_code != null ? parent_activity_code : ACTIVITY_CODE);
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
            ratingBar.setRating(Float.parseFloat(""+place.getScore()));
            if(place.getImage() != null && !place.getImage().equals(""))
            img_place_detail.setImageBitmap(Helper.fromBase64ToBitmap(place.getImage()));
        }catch (Exception ex){

        }
    }

    private void showComments(){
        try {
            ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

            Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //PlacesResponse oResponse = new Gson().fromJson(response.toString(), PlacesResponse.class);
                    UserCommentResponse oResponse = new Gson().fromJson(response.toString(), UserCommentResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                        listComments = oResponse.getData();
                        recycler.setHasFixedSize(true);
                        lManager = new LinearLayoutManager(Project.getInstance().getCurrentActivity());
                        recycler.setLayoutManager(lManager);
                        adapter = new CommentAdapter(listComments);
                        //adapter.setOnItemClickListener(FragmentFavoritePlaces.this);
                        recycler.setAdapter(adapter);
                        if(listComments.isEmpty()){
                            ((TextView) findViewById(R.id.tv_place_no_comments)).setVisibility(View.VISIBLE);
                        }else{
                            ((TextView) findViewById(R.id.tv_place_no_comments)).setVisibility(View.GONE);
                            recycler.setVisibility(View.VISIBLE);
                        }
                    } else
                        ((TextView) findViewById(R.id.tv_place_no_comments)).setVisibility(View.VISIBLE);
                    progress.dismiss();
                }
            };

            Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ((TextView) findViewById(R.id.tv_place_no_comments)).setVisibility(View.VISIBLE);
                    progress.dismiss();
                }
            };

            proxy.BACKEND_API_POST(HttpClientManager.BKN_GET_COMMENTS, new JSONObject(new Gson().toJson(place)), callBack_OK, callBack_ERROR);

        } catch (Exception oException)
        {
            ((TextView) findViewById(R.id.tv_place_no_comments)).setVisibility(View.VISIBLE);
            progress.dismiss();
        }
    }

    public void openWaze(View view) {
        try {
            String uri = getString(R.string.waze, place.getLocation());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        } catch (Exception e) {
            if (e instanceof ActivityNotFoundException)
                Toast.makeText(getApplicationContext(),getString(R.string.waze_not_install), Toast.LENGTH_SHORT).show();
        }
    }

    public void ManageFavoritePlace(View view) {
        try {
            //ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

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
                    //progress.dismiss();
                }
            };

            Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    place.setFavorite(!place.isFavorite());
                    Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    //progress.dismiss();
                }
            };
            ManageFavoritePlaceRequest oRequest = new ManageFavoritePlaceRequest();
            oRequest.setPlaceId(place.getId());
            oRequest.setUserName(Project.getInstance().getCurrentUser().getUserName());
            place.setFavorite(!place.isFavorite());

            oRequest.setScore(ratingBar.getRating());
            proxy.BACKEND_API_POST(HttpClientManager.BKN_MANAGE_FAVORITE_PLACE, new JSONObject(new Gson().toJson(oRequest)), callBack_OK, callBack_ERROR);
        } catch (Exception oException) {
            progress.dismiss();
        }
    }

    public void setPlaceScore(View view) {
        try {
            //ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

            Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    BaseResponse oResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                        ((ImageView) findViewById(R.id.btnLike)).setImageDrawable(getDrawable(place.isFavorite() ? R.drawable.ic_like : R.drawable.ic_dislike));
                        //finish();
                    } else {
                        //Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    }
                    //progress.dismiss();
                }
            };

            Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //place.setFavorite(!place.isFavorite());
                    //oast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    //progress.dismiss();
                }
            };
            ManageFavoritePlaceRequest oRequest = new ManageFavoritePlaceRequest();
            oRequest.setPlaceId(place.getId());
            oRequest.setUserName(Project.getInstance().getCurrentUser().getUserName());
            oRequest.setScore(ratingBar.getRating());
            proxy.BACKEND_API_POST(HttpClientManager.BKN_SET_PLACE_SCORE, new JSONObject(new Gson().toJson(oRequest)), callBack_OK, callBack_ERROR);
        } catch (Exception oException) {
            progress.dismiss();
        }
    }

    public void makeComment(View view){
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlaceDetail.this);

            alertDialogBuilder.setTitle(getString(R.string.AddComment));
            View dialogView = LayoutInflater.from(PlaceDetail.this).inflate(R.layout.activity_dialog_message, null);
            alertDialogBuilder
                    .setMessage(getString(R.string.add_comment_desc))
                    .setView(dialogView)
                    .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditText et_comment = dialogView.findViewById(R.id.et_comment);
                            if (et_comment.getText().toString().trim().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.completeFieldsMsg), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

                                Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        BaseResponse oResponse = new Gson().fromJson(response.toString(), BaseResponse.class);
                                        if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                                            showComments();
                                            dialog.cancel();
                                        } else {
                                            Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                                        }
                                        progress.dismiss();
                                    }
                                };

                                Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //place.setFavorite(!place.isFavorite());
                                        Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                    }
                                };
                                CommentPlaceRequest oRequest = new CommentPlaceRequest();
                                oRequest.setPlaceId(place.getId());
                                oRequest.setUserName(Project.getInstance().getCurrentUser().getUserName());
                                oRequest.setMessage(et_comment.getText().toString().trim());
                                proxy.BACKEND_API_POST(HttpClientManager.BKN_CREATE_COMMENT, new JSONObject(new Gson().toJson(oRequest)), callBack_OK, callBack_ERROR);
                            } catch (Exception oException) {
                                Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
    }

}
