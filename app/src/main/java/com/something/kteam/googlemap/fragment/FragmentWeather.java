package com.something.kteam.googlemap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.something.kteam.googlemap.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by Nguyen Hung Son on 4/15/2017.
 */

public class FragmentWeather extends Fragment {
    public static View view;
    OnManager onFragmentManager;
    PlaceAutocompleteFragment autocompleteFragment;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onFragmentManager = (OnManager) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.search_weather, container, false);
        } catch (InflateException e) {
        }
        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                onFragmentManager.onDataWeather(place);
            }

            @Override
            public void onError(Status status) {

            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public interface OnManager{
        void onDataWeather(Place place);
    }
}
