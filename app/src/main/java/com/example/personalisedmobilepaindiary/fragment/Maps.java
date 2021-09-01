package com.example.personalisedmobilepaindiary.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.example.personalisedmobilepaindiary.R;
import com.example.personalisedmobilepaindiary.databinding.MapsBinding;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;

import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;


import java.io.IOException;
import java.util.List;

public class Maps extends Fragment {
    private MapsBinding binding;
    private MapView mapView;
    private String address;
    private Circle circle;
    private Symbol symbol;
    private static final String ID_ICON_LOCATION = "location";
    public Maps(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));

        binding = MapsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        showDialog();

        mapView = (MapView) binding.mapView;
        mapView.onCreate(savedInstanceState);



        return view;

    }

    //ask user to enter address
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please enter your full address");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                address = input.getText().toString();

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {

                        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                //convert address to lan and lng
                                LatLng latLng = getLatLngFromAddress(address);

                                addIconToStyle(style);

                                // Create a SymbolManager.
                                SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);

                                // Set non-data-driven properties.
                                symbolManager.setIconAllowOverlap(true);
                                symbolManager.setTextAllowOverlap(true);

                                // Create a symbol at the location entered.
                                SymbolOptions symbolOptions = new SymbolOptions()
                                        .withLatLng(latLng)
                                        .withIconImage(ID_ICON_LOCATION)
                                        .withIconSize(1.8f)
                                        .withIconColor("#B75FE2")
                                        .withSymbolSortKey(10.0f)
                                        .withDraggable(false);

                                //draw the symbol.
                                symbol = symbolManager.create(symbolOptions);


                                //set camera position
                                CameraPosition cameraPosition =
                                        new CameraPosition.Builder().target(latLng).zoom(15).build();
                                mapboxMap.setCameraPosition(cameraPosition);

                            }
                        });

                    }
                });
            }


        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void addIconToStyle(Style style) {
        style.addImage(ID_ICON_LOCATION,
                BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_baseline_location_on_24)),
                true);
    }


    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            Address location = addressList.get(0);

            return new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
