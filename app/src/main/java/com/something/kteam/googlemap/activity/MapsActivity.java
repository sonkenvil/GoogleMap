package com.something.kteam.googlemap.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.something.kteam.googlemap.R;
import com.something.kteam.googlemap.fragment.FragmentATM;
import com.something.kteam.googlemap.fragment.FragmentDirection;
import com.something.kteam.googlemap.fragment.FragmentSearchPlace;
import com.something.kteam.googlemap.fragment.FragmentWeather;
import com.something.kteam.googlemap.model.Atm;
import com.something.kteam.googlemap.model.Routes;
import com.something.kteam.googlemap.model.Weather;
import com.something.kteam.googlemap.model.WeatherDetail;
import com.something.kteam.googlemap.parse.ParseJsonAtm;
import com.something.kteam.googlemap.parse.ParseJsonDirection;
import com.something.kteam.googlemap.parse.ParseJsonWeather;
import com.something.kteam.googlemap.parse.ParseJsonWeatherDetail;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.something.kteam.googlemap.R.id.map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
        , NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
        , FragmentSearchPlace.OnFragmentManager, FragmentWeather.OnManager
        , FragmentATM.OnFragmentManager, FragmentDirection.OnFragmentManager {

    private GoogleMap mMap;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageButton menu;
    private FragmentManager fragmentManager;
    private ImageView status;
    private TextView temp;
    private TextView title;
    private TextView temp_min;
    private TextView temp_max;
    private TextView day_date;
    private Vector<Marker> listMarker;
    private Vector<Polyline> listPolylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        navigationView = (NavigationView) findViewById(R.id.NavigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        menu = (ImageButton) findViewById(R.id.menu);
        listMarker = new Vector<>();
        listPolylines = new Vector<>();
        menu.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();

        // set vi tri mylocation
        View mapView = mapFragment.getView();
        if (mapView != null &&
                mapView.findViewById(map) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 20, 32);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentSearchPlace searchPlace = new FragmentSearchPlace();
        transaction.replace(R.id.LinearMapActivity, searchPlace);
        drawerLayout.closeDrawer(Gravity.START);
        transaction.commit();

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                removeMarker_Polyline();
                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                FragmentSearchPlace searchPlace = new FragmentSearchPlace();
                transaction1.replace(R.id.LinearMapActivity, searchPlace);
                transaction1.commit();
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.weather:
                removeMarker_Polyline();
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                FragmentWeather fragmentWeather = new FragmentWeather();
                transaction2.replace(R.id.LinearMapActivity, fragmentWeather);
                transaction2.commit();
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.direction:
                removeMarker_Polyline();
                FragmentTransaction transaction3 = fragmentManager.beginTransaction();
                FragmentDirection fragmentDirection = new FragmentDirection();
                transaction3.replace(R.id.LinearMapActivity, fragmentDirection);
                transaction3.commit();
                drawerLayout.closeDrawer(Gravity.START);
                break;
//            case R.id.searchATM:
//                removeMarker_Polyline();
//                FragmentTransaction transaction4 = fragmentManager.beginTransaction();
//                FragmentATM fragmentATM = new FragmentATM();
//                transaction4.replace(R.id.LinearMapActivity, fragmentATM);
//                transaction4.commit();
//                drawerLayout.closeDrawer(Gravity.START);
//                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.menu:
                drawerLayout.openDrawer(Gravity.START);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        closeAppDialog();
    }

    private void closeAppDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).
                setCancelable(false).
                create();
        View viewAlert = getLayoutInflater().inflate(R.layout.layout_alert_dialog, null);
        TextView tvTitle = (TextView) viewAlert.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) viewAlert.findViewById(R.id.tv_content);
        Button btnOK = (Button) viewAlert.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) viewAlert.findViewById(R.id.btn_cancel);
        tvTitle.setText(getString(R.string.text_title_close_app_dialog));
        tvContent.setText(getString(R.string.text_are_you_sure_to_close_app));
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                alertDialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.setView(viewAlert);
        alertDialog.show();
    }

    @Override
    public void onDataSelected(Place place) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place.getLatLng());
        listMarker.add(mMap.addMarker(markerOptions
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker1))
                .title(place.getName().toString())));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }

    @Override
    public void onDataWeather(final Place place) {
        removeMarker_Polyline();
        LatLng latLng = place.getLatLng();
        String URL = "http://api.wunderground.com/api/5ae1667f5a3fcf2a/conditions/q/" + latLng.latitude + "," + latLng.longitude + ".json";
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place.getLatLng());
        listMarker.add(mMap.addMarker(markerOptions
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_maker))
                .title(place.getAddress().toString())));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

        ParseJsonWeather parseJsonWeather = new ParseJsonWeather(MapsActivity.this, URL);
        parseJsonWeather.getInfor(new ParseJsonWeather.Callback() {
            @Override
            public void onSuccess(final Weather weather) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View view = getLayoutInflater().inflate(R.layout.custom_inforwindow, null);
                        temp = (TextView) view.findViewById(R.id.temp);
                        status = (ImageView) view.findViewById(R.id.status);
                        temp_min = (TextView) view.findViewById(R.id.temp_min);
                        temp_max = (TextView) view.findViewById(R.id.temp_max);
                        day_date = (TextView) view.findViewById(R.id.day_date);

                        int kq = (int) ((Float.parseFloat(weather.getTemp()) - 32) / 1.8);
                        if ((float) ((Float.parseFloat(weather.getTemp()) - 32) / 1.8) - (int) ((Float.parseFloat(weather.getTemp()) - 32) / 1.8) >= 0.5)
                            kq += 1;
                        temp.setText("" + kq + "°C");

                        Glide.with(MapsActivity.this)
                                .load(weather.getImage())
                                .override(200, 300)
                                .into(status);

                        title = (TextView) view.findViewById(R.id.title);
                        title.setText(weather.getCity());


                        int low = (int) ((Float.parseFloat(weather.getLow()) - 32) / 1.8);
                        if ((float) ((Float.parseFloat(weather.getLow()) - 32) / 1.8) - (int) ((Float.parseFloat(weather.getLow()) - 32) / 1.8) >= 0.5)
                            low += 1;
                        temp_min.setText(low + "°C");


                        int high = (int) ((Float.parseFloat(weather.getHigh()) - 32) / 1.8);
                        if ((float) ((Float.parseFloat(weather.getHigh()) - 32) / 1.8) - (int) ((Float.parseFloat(weather.getHigh()) - 32) / 1.8) >= 0.5)
                            high += 1;
                        temp_max.setText(high + "°C");


                        day_date.setText(weather.getDay());
                        return view;
                    }
                });
            }
        });

        ParseJsonWeather parse = new ParseJsonWeather(MapsActivity.this, URL);
        parse.getInfor(new ParseJsonWeather.Callback() {
            @Override
            public void onSuccess(final Weather weather) {
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String URL2 = "http://api.wunderground.com/api/5ae1667f5a3fcf2a/forecast7day/q/" + place.getLatLng().latitude + "," + place.getLatLng().longitude + ".json";
                        ParseJsonWeatherDetail parseJsonWeatherDetail = new ParseJsonWeatherDetail(MapsActivity.this, URL2);
                        parseJsonWeatherDetail.getInfor(new ParseJsonWeatherDetail.CallBack() {
                            @Override
                            public void onSuccess(ArrayList<WeatherDetail> listWeather) {
                                Intent intent = new Intent(MapsActivity.this, WeatherActivity.class);
                                intent.putExtra("city", weather.getCity());
                                intent.putExtra("day", weather.getDay());
                                intent.putExtra("temp", weather.getTemp());
                                intent.putExtra("min", weather.getLow());
                                intent.putExtra("max", weather.getHigh());
                                intent.putExtra("tp", weather.getTp());
                                intent.putExtra("weather", weather.getDescription());
                                intent.putParcelableArrayListExtra("list", listWeather);
                                startActivity(intent);
                            }
                        });

                    }
                });
            }
        });

    }

    @Override
    public void onDataATM(Place place) {
        removeMarker_Polyline();
        String APIkey = "AIzaSyDdg1V9pKIF_dc4zRyr32_KKLS0rtKjTTQ";
        String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + place.getLatLng().latitude + ","
                + place.getLatLng().longitude + "&radius=500&type=atm&key=" + APIkey;
        ParseJsonAtm parseJsonAtm = new ParseJsonAtm(URL, MapsActivity.this);
        listMarker.add(mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                .title(place.getName().toString())));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
        parseJsonAtm.getATM(new ParseJsonAtm.CallBack() {
            @Override
            public void onSuccess(ArrayList<Atm> listATM) {
                for (Atm i : listATM) {
                    Log.d("123",""+listATM.size());
                    LatLng latLng = new LatLng(i.getLat(), i.getLng());
                    listMarker.add(mMap.addMarker(new MarkerOptions().position(latLng)
                            .title(i.getName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.atm))));
                }
            }
        });
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }


    @Override
    public void onDataDirection(final String transport, final Place locago, final Place locaDes, final ProgressDialog progressDialog, final LinearLayout linearLayout, final TextView tv_time) {
        removeMarker_Polyline();
        String urlOrigin = null, urlDestination = null;
        String origin = null, destination = null;
        try {
            urlOrigin = URLEncoder.encode(locago.getAddress().toString(), "utf-8");
            origin = urlOrigin.replace(" ", "%20");
            urlDestination = URLEncoder.encode(locaDes.getAddress().toString(), "utf-8");
            destination = urlDestination.replace(" ", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String APIkey = "AIzaSyDdg1V9pKIF_dc4zRyr32_KKLS0rtKjTTQ";
        String URL = "https://maps.googleapis.com/maps/api/directions/json?origin=" + locago.getLatLng().latitude+", "+ locago.getLatLng().longitude+ "&destination="
                + locaDes.getLatLng().latitude+", "+ locaDes.getLatLng().longitude+  "&mode=" + transport;

        ParseJsonDirection parseJsonDirection = new ParseJsonDirection(URL, MapsActivity.this);
        parseJsonDirection.getDirection(new ParseJsonDirection.Callback() {
            @Override
            public void onSuccess(Routes routes, boolean bl) {
                if(bl) {
                    listMarker.add(mMap.addMarker(new MarkerOptions().position(locago.getLatLng())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))));
                    listMarker.add(mMap.addMarker(new MarkerOptions().position(locaDes.getLatLng())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locago.getLatLng(), 16));

                    String distance = routes.getDistance().getText();
                    String duration = routes.getDuration().getText();
                    tv_time.setText(duration + "( " + distance + ")");
                    PolylineOptions polylineOptions = new PolylineOptions().
                            geodesic(true).
                            color(Color.RED).
                            width(10);
                    polylineOptions.add(locago.getLatLng());
                    for (int i = 0; i < routes.getListPoint().size(); i++) {
                        polylineOptions.add(routes.getListPoint().get(i));
                    }

                    polylineOptions.add(locaDes.getLatLng());

                    listPolylines.add(mMap.addPolyline(polylineOptions));
                    progressDialog.dismiss();
                    linearLayout.setVisibility(View.VISIBLE);
                }else {
                    progressDialog.dismiss();
                    tv_time.setText(""+transport+" không khả dụng");
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

        });
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }

    private void removeMarker_Polyline(){
        if (listPolylines.size() > 0) {
            for (Polyline i : listPolylines) i.remove();
        }
        if (listMarker.size() > 0) {
            for (Marker i : listMarker)
                i.remove();
        }
    }
}
