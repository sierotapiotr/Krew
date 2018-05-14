package com.example.ignacy.myapplication;


        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.maps.android.data.Feature;
        import com.google.maps.android.data.kml.KmlContainer;
        import com.google.maps.android.data.kml.KmlLayer;
        import com.google.maps.android.data.kml.KmlPlacemark;
        import com.google.maps.android.data.kml.KmlPolygon;
        import org.xmlpull.v1.XmlPullParserException;

        import android.*;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.provider.Settings;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URL;
        import android.os.Bundle;
        import android.support.v4.app.FragmentActivity;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

        import static com.example.ignacy.myapplication.MainActivity.user_location;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button btnLocation;
    private LatLngBounds POLAND = new LatLngBounds(
            new LatLng(52.433708, 14.566766), new LatLng(52.647221, 25.656));



    public void showMarkers () {
        try {
            mMap = getMap();
            retrieveFileFromResource();

            //retrieveFileFromUrl();
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMap();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {

        if (mMap != null) {
            return;
        }
        mMap = map;
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(POLAND,0));
        showMarkers();
        try {
            map.addMarker(new MarkerOptions().position(new LatLng(50.866607,20.642781)).title("Twoja ostatnia lokalizacja")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        catch (Exception e) {
            Log.e("Brak lokalizacji", e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    protected int getLayoutId() {
        return R.layout.activity_maps;
    }

    private void retrieveFileFromResource() {
        try {
            KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.lista_punktow, getApplicationContext());
            kmlLayer.addLayerToMap();
            //moveCameraToKml(kmlLayer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }
    protected GoogleMap getMap() {
        return mMap;
    }
}
