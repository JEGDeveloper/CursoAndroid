package test.yespinoza.androidproject.View.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import test.yespinoza.androidproject.Model.Entity.Place;
import test.yespinoza.androidproject.Model.Response.PlacesResponse;
import test.yespinoza.androidproject.Model.Utils.FragmentBase;
import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;
import test.yespinoza.androidproject.Model.Utils.HttpClientManager;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;
import test.yespinoza.androidproject.View.Activity.AddPlace;
import test.yespinoza.androidproject.View.Activity.Index;
import test.yespinoza.androidproject.View.Activity.PlaceDetail;
import test.yespinoza.androidproject.databinding.FragmentLocationBinding;

public class FragmentLocation extends FragmentBase {

    private Index activity;
    public FragmentLocationBinding binding;
    private ProgressDialog progress;
    private HttpClientManager proxy;
    private ArrayList<Place> places;
    public static double latitude, longitude;
    private LocationManager locationManager;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            activity = Index.getInstance();
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false);
            binding.mapView.setListener(this::showPlaceDetail);
            proxy = new HttpClientManager(getContext());
            progress  = new ProgressDialog(getContext());
            binding.getRoot().findViewById(R.id.addPlace).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(binding.getRoot().getContext(), "Prueba de Agregar",Toast.LENGTH_SHORT).show();
                    locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Toast.makeText(getContext(),"Por favor encienda el GPS del dispositivo",Toast.LENGTH_SHORT).show();
                    }else {
                        getLocation();
                        startActivity(new Intent(getContext(), AddPlace.class));
                        getActivity().finish();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPlaces();
    }

    public void loadPlaces() {
        try {

            ShowProgressDialog(getString(R.string.title_loading_data), getString(R.string.description_loading_data));

            Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    PlacesResponse oResponse = new Gson().fromJson(response.toString(), PlacesResponse.class);
                    if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                        places = oResponse.getData();
                        if(places != null)
                            binding.mapView.setPlaces(places);
                    }else
                        Toast.makeText(getContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            };

            Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            };
            proxy.BACKEND_API_POST(HttpClientManager.BKN_GET_PLACES, new JSONObject(new Gson().toJson(Project.getInstance().getCurrentUser())),callBack_OK, callBack_ERROR);

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

    public void showPlaceDetail(Place place) {
        PlaceDetail.place = place;
        startActivity(new Intent(getContext(), PlaceDetail.class));
        getActivity().finish();
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                String provider = locationManager.getBestProvider(new Criteria(), true);

                // Getting Current Location
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2022);
                }
                location = locationManager.getLastKnownLocation(provider);

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                //locationManager.requestLocationUpdates(provider, 20000, 0, this);
            } else
                Toast.makeText(getContext(),"Por favor encienda el GPS del dispositivo",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getContext(),getString(R.string.somethingWentWrong),Toast.LENGTH_SHORT).show();
        }

    }

    private void mostrarFragment(Fragment fragment) {
        FragmentTransaction transaccion = getActivity().getSupportFragmentManager().beginTransaction();
        transaccion.replace(R.id.frmLayoutIndex, fragment);
        transaccion.commit();
    }

}
