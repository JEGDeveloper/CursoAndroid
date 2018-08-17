package test.yespinoza.androidproject.View.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import test.yespinoza.androidproject.Adapter.CardViewAdapter;
import test.yespinoza.androidproject.Model.Entity.CardView;
import test.yespinoza.androidproject.Model.Entity.Place;
import test.yespinoza.androidproject.Model.Response.PlacesResponse;
import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;
import test.yespinoza.androidproject.Model.Utils.HttpClientManager;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;
import test.yespinoza.androidproject.View.Activity.PlaceDetail;

public class FragmentFavoritePlaces extends Fragment implements CardViewAdapter.OnItemClickListener {
    public static String ACTIVITY_CODE = "96";
    public static View rootView;
    public static CardViewAdapter adapter;
    private static List<CardView> items;
    private RecyclerView.LayoutManager lManager;
    private RecyclerView recycler;
    private ProgressDialog progress;
    private HttpClientManager proxy;
    private ArrayList<Place> places;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favorite_places, container, false);
        items = new ArrayList<>();
        adapter = null;
        proxy = new HttpClientManager(getContext());
        progress = new ProgressDialog(getContext());
        recycler = rootView.findViewById(R.id.reciclador);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavoritePlaces();
    }

    private void loadFavoritePlaces() {
        try {
            ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

            Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    PlacesResponse oResponse = new Gson().fromJson(response.toString(), PlacesResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                        places = oResponse.getData();

                        for(Place oPlace : places){
                            CardView cardView = new CardView(oPlace.getName(), oPlace.getDescription(), oPlace.getImage().equals("")?oPlace.getType().getIcon(): oPlace.getImage());
                            items.add(cardView);
                        }

                        recycler.setHasFixedSize(true);
                        lManager = new LinearLayoutManager(rootView.getContext());
                        recycler.setLayoutManager(lManager);
                        adapter = new CardViewAdapter(items);
                        adapter.setOnItemClickListener(FragmentFavoritePlaces.this);
                        recycler.setAdapter(adapter);
                        /**Swipe to Delete**/
                        //recycler.addOnItemTouchListener( new SwipeableRecyclerViewTouchListener(recycler, new SwipeListener(adapter, items)));

                        if(places.isEmpty()){
                            Toast.makeText(rootView.getContext(), getString(R.string.sinFavoritos), Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(getContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    DismissProgressDialog();
                }
            };

            Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    DismissProgressDialog();
                }
            };
            proxy.BACKEND_API_POST(HttpClientManager.BKN_GET_FAVORITE_PLACES, new JSONObject(new Gson().toJson(Project.getInstance().getCurrentUser())), callBack_OK, callBack_ERROR);

        } catch (Exception oException)
        {
            Toast.makeText(getContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
            DismissProgressDialog();
        }
    }

    public void showPlaceDetail(Place place) {
        PlaceDetail.place = place;
        place.setFavorite(true);
        Intent oIntet = new Intent(getContext(), PlaceDetail.class);
        oIntet.putExtra("ACTIVITY_CODE",ACTIVITY_CODE);
        startActivity(oIntet);

        getActivity().finish();
    }
    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        Project.getInstance().getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        progress.show();
    }

    private void DismissProgressDialog(){
        progress.dismiss();
        Project.getInstance().getCurrentActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    @Override
    public void OnItemClick(int position) {
        showPlaceDetail(places.get(position));
    }
}
