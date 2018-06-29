package com.example.student.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private WebView webView;
    private ImageView imageView;
    private LinearLayout mapLayout;
    private GoogleMap mMap;
    private double[] a,b;
    private String[] name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        makeUi();
        invisible();
        mapLayout.setVisibility(View.VISIBLE);

    }

    public void makeUi(){
        webView=findViewById(R.id.webView);
        imageView=findViewById(R.id.imageView);
        mapLayout=findViewById(R.id.maplayout);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);


    }

    public void invisible(){
        webView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        mapLayout.setVisibility(View.INVISIBLE);
    }

    public void clickBt(View v){

        if(v.getId()==R.id.btmap){
            invisible();
            mapLayout.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.btimg){
            invisible();
            imageView.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.btchart){
            invisible();

            webView.loadUrl("http://m.naver.com");
            webView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera


        LatLng mulCam = new LatLng( 37.510843, 127.029281);
        mMap.addMarker(new MarkerOptions().position(mulCam).title("Marker in MultiCampus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mulCam,17));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        requestMyLocation();
    }

    public void showBake(View v){
        mMap.clear();
        a= new double[]{37.5107102,37.4989964,37.5108269};
        b=new double[]{127.0208831,127.0363477,127.0292881};
        name=new String[]{"헤이미 마카롱","잇츠데이지 마카롱","오묘한 과자점 마카롱"};


        // for loop를 통한 n개의 마커 생성
        for (int idx = 0; idx < 3; idx++) {
            // 1. 마커 옵션 설정 (만드는 과정)
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(a[idx], b[idx]))
                    .title("marker" + name[idx]); // 타이틀.

            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }

        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.5107102, 127.0208831)));
    }

    public void showDrink(View v){
        mMap.clear();
        a= new double[]{37.5207102,37.1989964,37.3108269};
        b=new double[]{127.0308831,127.1363477,127.2292881};
        name=new String[]{"술1","술2","술3"};


        // for loop를 통한 n개의 마커 생성
        for (int idx = 0; idx < 3; idx++) {
            // 1. 마커 옵션 설정 (만드는 과정)
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions.position(new LatLng(a[idx], b[idx])).title("marker" + name[idx]);
            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }

        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.5207102, 127.0308831)));
    }

    public void showCoff(View v){
        mMap.clear();
        a= new double[]{37.5007102,37.1889964,37.2108269};
        b=new double[]{127.1308831,127.1263477,127.1292881};
        name=new String[]{"커피1","커피2","커피3"};


        // for loop를 통한 n개의 마커 생성
        for (int idx = 0; idx < 3; idx++) {
            // 1. 마커 옵션 설정 (만드는 과정)
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions.position(new LatLng(a[idx], b[idx])).title("marker" + name[idx]);
            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }

        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.5007102, 127.1308831)));
    }




    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;//~초동안 변화가 없으면 이전에 있던 위치값을 쓴다
            float minDistance = 0;
            manager.requestLocationUpdates(//로케이션 매니저에 바뀔 때 마다 아래 수행되도록.
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) { //위치바뀌면 바뀐위치객체 넘겨줌
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, //게이트웨이 중계기 위치에 따라.
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );


        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true); //파란점을 화면 가운데 계속 놓겠다는 말.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 13));
        //화면(카메라)위치를 현재 위치에 맞게 옮겨주는것.
    }
}
