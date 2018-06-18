package com.something.kteam.googlemap.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.something.kteam.googlemap.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.something.kteam.googlemap.R.drawable.ic_bike;
import static com.something.kteam.googlemap.R.drawable.ic_bike_click;
import static com.something.kteam.googlemap.R.drawable.ic_taxi;
import static com.something.kteam.googlemap.R.drawable.ic_taxi_click;
import static com.something.kteam.googlemap.R.drawable.ic_walk;
import static com.something.kteam.googlemap.R.drawable.ic_walk_click;

/**
 * Created by Nguyen Hung Son on 4/17/2017.
 */

public class FragmentDirection extends Fragment implements View.OnClickListener {
    static View view;
    TextView tv_go, tv_destination, tv_bike, tv_walk, tv_car, tv_time;
    ImageView img_bike, img_walk, img_car;
    LinearLayout bg_bike, bg_walk, bg_car, ll_menu_bottom_find_customer;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    boolean isGo = false, isDesti = false;
    String transpost;
    OnFragmentManager onFragmentManager;
    Place placeGo, placeDes;

    public interface OnFragmentManager {
        void onDataDirection(String transport, Place locago, Place locaDes, ProgressDialog progressDialog, LinearLayout linearLayout, TextView time);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onFragmentManager = (OnFragmentManager) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.layout_direction, container, false);
        } catch (InflateException e) {
        }
        anhxa();
        return view;
    }

    private void anhxa() {

        tv_go = (TextView) view.findViewById(R.id.tv_go);
        tv_destination = (TextView) view.findViewById(R.id.tv_destination);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_bike = (TextView) view.findViewById(R.id.tv_bike);
        tv_walk = (TextView) view.findViewById(R.id.tv_walk);
        tv_car = (TextView) view.findViewById(R.id.tv_car);

        img_bike = (ImageView) view.findViewById(R.id.img_bike);
        img_walk = (ImageView) view.findViewById(R.id.img_walk);
        img_car = (ImageView) view.findViewById(R.id.img_car);

        bg_bike = (LinearLayout) view.findViewById(R.id.bg_bike);
        bg_walk = (LinearLayout) view.findViewById(R.id.bg_walk);
        bg_car = (LinearLayout) view.findViewById(R.id.bg_car);
        ll_menu_bottom_find_customer = (LinearLayout) view.findViewById(R.id.ll_menu_bottom_find_customer);
        ll_menu_bottom_find_customer.setVisibility(View.INVISIBLE);
        bg_bike.setOnClickListener(this);
        bg_car.setOnClickListener(this);
        bg_walk.setOnClickListener(this);

        tv_go.setOnClickListener(this);
        tv_destination.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bg_bike:
                transpost = "bicycling";
                onClickBike();
                findTheWay(transpost);
                break;
            case R.id.bg_walk:
                transpost = "walking";
                onClickWalk();
                findTheWay(transpost);
                break;
            case R.id.bg_car:
                transpost = "driving";
                findTheWay(transpost);
                onClickCar();
                break;
            case R.id.tv_go:
                isGo = true;
                isDesti = false;
                openPickUpPoints();
                break;
            case R.id.tv_destination:
                isDesti = true;
                isGo = false;
                openPickUpPoints();
                break;
        }
    }

    private void onClickWalk() {
        bg_walk.setBackgroundResource(R.drawable.bg_white);
        img_walk.setImageResource(ic_walk_click);
        tv_walk.setTextColor(getResources().getColor(R.color.color_blue));

        bg_bike.setBackgroundResource(R.color.color_blue);
        img_bike.setImageResource(ic_bike);
        tv_bike.setTextColor(getResources().getColor(R.color.color_white));

        bg_car.setBackgroundResource(R.color.color_blue);
        img_car.setImageResource(ic_taxi);
        tv_car.setTextColor(getResources().getColor(R.color.color_white));
    }

    private void onClickCar() {
        bg_car.setBackgroundResource(R.drawable.bg_white);
        img_car.setImageResource(ic_taxi_click);
        tv_car.setTextColor(getResources().getColor(R.color.color_blue));

        bg_bike.setBackgroundResource(R.color.color_blue);
        img_bike.setImageResource(ic_bike);
        tv_bike.setTextColor(getResources().getColor(R.color.color_white));

        bg_walk.setBackgroundResource(R.color.color_blue);
        img_walk.setImageResource(ic_walk);
        tv_walk.setTextColor(getResources().getColor(R.color.color_white));
    }

    private void onClickBike() {
        bg_bike.setBackgroundResource(R.drawable.bg_white);
        img_bike.setImageResource(ic_bike_click);
        tv_bike.setTextColor(getResources().getColor(R.color.color_blue));

        bg_walk.setBackgroundResource(R.color.color_blue);
        img_walk.setImageResource(ic_walk);
        tv_walk.setTextColor(getResources().getColor(R.color.color_white));

        bg_car.setBackgroundResource(R.color.color_blue);
        img_car.setImageResource(ic_taxi);
        tv_car.setTextColor(getResources().getColor(R.color.color_white));
    }

    private void openPickUpPoints() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    private void findTheWay(String transport) {
        if (placeGo != null & placeDes != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Đang tìm đường");
            progressDialog.show();
            onFragmentManager.onDataDirection(transport, placeGo, placeDes, progressDialog, ll_menu_bottom_find_customer, tv_time);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                if (isGo) {
                    isGo = false;
                    isDesti = false;
                    tv_go.setText(place.getName().toString());
                    placeGo = place;
                }
                if (isDesti) {
                    isGo = false;
                    isDesti = false;
                    tv_destination.setText(place.getName().toString());
                    placeDes = place;
                }

                if (placeGo != null & placeDes != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Đang tìm đường");
                    progressDialog.show();
                    onFragmentManager.onDataDirection("bicycling", placeGo, placeDes, progressDialog, ll_menu_bottom_find_customer, tv_time);
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

}
