package test.yespinoza.androidproject.Model.Utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import test.yespinoza.androidproject.Model.Entity.Place;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;

public class MapView extends FrameLayout implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap googleMap;
    private Subject<GoogleMap> mapSubject;
    private ArrayList<Marker> markers;
    private ArrayList<Place> places;
    private MapViewEvents listener;

    public MapView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs,
                   @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        if (!isInEditMode()) {
            FragmentTransaction fragmentTransaction =
                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(getId(), mapFragment);
            fragmentTransaction.commit();

            mapSubject = BehaviorSubject.create();
            Observable.create(
                    (ObservableOnSubscribe<GoogleMap>) e -> mapFragment.getMapAsync(this))
                    .subscribe(mapSubject);
        }
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
        if (googleMap != null && markers.isEmpty()) {
            loadMarkers();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (listener != null)
            listener.onMarkerClick(places.get(markers.indexOf(marker)));
        return false;
    }

    public void setListener(MapViewEvents listener) {
        this.listener = listener;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(Project.getInstance().getCurrentActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Project.getInstance().getCurrentActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2022);
        } else {
            this.googleMap = map;
            googleMap.setOnMarkerClickListener(this);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);

            LatLng latLng = new LatLng(10.0070624, -84.21732);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            loadMarkers();
        }
    }

    public void loadMarkers() {
        markers = new ArrayList<>();
        if (places != null) {
            for (Place place : places) {
                addMarker(place);
            }
        }
    }

    public void addMarker(Place place) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(place.getLatitude(), place.getLongitude()));
        marker.title(place.getName());
        if(place.getIcon() == null || place.getIcon().equals(""))
            marker.icon(BitmapDescriptorFactory.fromResource(
                    place.isFavorite() ?
                            R.drawable.ic_favorite :
                            R.drawable.ic_location));
        else{
            Bitmap bitmap = Helper.fromBase64ToBitmap_Scalad(place.getIcon(), 96, 96);
            marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }
        this.markers.add(this.googleMap.addMarker(marker));
    }

    public interface MapViewEvents {
        void onMarkerClick(Place selected);
    }
}
