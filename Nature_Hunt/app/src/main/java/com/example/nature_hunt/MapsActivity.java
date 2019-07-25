package com.example.nature_hunt;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.example.nature_hunt.db.CloudDataRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Double.parseDouble;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                onMapMarkerClick(marker);
                return true;
            }
        });

        LoadMap();
    }

    public boolean onMapMarkerClick(Marker marker) {
        Hunt hunt = (Hunt)marker.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("hunt", hunt);

        HuntPreviewFrag previewFrag = HuntPreviewFrag.newInstance();
        previewFrag.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(android.R.id.content, previewFrag)
                .addToBackStack(null).commit();
        return true;
    }

    private void LoadMap() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    if (!CloudDataRepository.isLoaded)
                    {
                        CloudDataRepository dataRepository = new CloudDataRepository(getApplicationContext());
                        dataRepository.LoadData();
                    }
                    final Map<Integer, Hunt> huntsMap = CloudDataRepository.huntsMap;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Hunt hunt: CloudDataRepository.huntsMap.values()) {
                                String[] values = hunt.coords().split(",");
                                LatLng markerCoordinates = new LatLng(parseDouble(values[0]), parseDouble(values[1]));
                                Marker marker = mMap.addMarker(new MarkerOptions().position(markerCoordinates).title(hunt.name()));
                                marker.setTag(hunt);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(markerCoordinates));
                            }

                            mMap.setMinZoomPreference(10);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public void closeActivity(View view) {
        finish();
    }
}
