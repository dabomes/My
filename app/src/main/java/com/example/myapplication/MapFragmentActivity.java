package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import android.content.Context;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.ZoomControlView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MapFragmentActivity extends FragmentActivity
        implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_fragment_activity);


        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


    }



    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        // ...
        //ui 추가
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
//        uiSettings.setZoomControlEnabled(false);
        uiSettings.setLocationButtonEnabled(true);

        //위치 오버레이 표시
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        //사용자 위치 변경(서울시청)
        //locationOverlay.setPosition(new LatLng(37.5670135, 126.9783740));
        //매물 정보(예 : 부산 시청)
        double[] lati = {35.1798159,35.17912, 35.1798};
        double[] logi = {129.0750222,129.0745, 129.07664};
        String[] name = {"부산광역시청","부산 경찰청", "시청역"};
        int[] price = {100,70,50};
        int Mnum=3; //마커 개수


        //마커들 만들기
        InfoWindow[] infoWindows = new InfoWindow[Mnum];
        Marker[] markers=new Marker[Mnum];
        for(int i=0; i<Mnum; i++){
            int finalI = i;
            markers[i]=new Marker();
            markers[i].setPosition(new LatLng(lati[i], logi[i]));
            markers[i].setCaptionText(name[i]);
            markers[i].setCaptionAligns(Align.Center);
            markers[i].setMap(naverMap);

            infoWindows[i]=new InfoWindow();//개별 정보창 추가
            infoWindows[i].setAdapter(new InfoWindow.DefaultTextAdapter(this) {
                @NonNull
                @Override
                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                    return "매물 이름 : " + name[finalI] + "\n가격 : "+price[finalI]+"억";
                }
            });

            markers[i].setOnClickListener(overlay -> {//마커 클릭시 정보창 표시/감춤
                if(markers[finalI].getInfoWindow()==null){
                    infoWindows[finalI].open(markers[finalI]);
                }
                else infoWindows[finalI].close();
                return true;
            });
        }



        //롱클릭 시 좌표 토스트
        naverMap.setOnMapLongClickListener((point, coord) ->
                Toast.makeText(this, coord.latitude + ", " + coord.longitude,
                        Toast.LENGTH_SHORT).show());
        //마커 여러개 만들기
//        Executor executor = new StartExecutor();
//        Handler handler = new Handler(Looper.getMainLooper());
//        executor.execute(() -> {
//            List<Marker> markers = new ArrayList<>();
//            for (int i = 0; i<2; i++){
//                Marker marker = new Marker();
//                marker.setPosition(new LatLng(lo[i], la[i])); //좌표 설정
//                marker.setCaptionText(apt_name+i); //마커 이름 설정
//                marker.setCaptionAligns(Align.Center);
//            }
//
//            handler.post(() -> {
//                for(Marker marker : markers){
//                    marker.setMap(naverMap);
//                }
//            });
//        });




    }
    //마커 여러개 만들기 위한 executor
//    public interface Executor{
//        void execute(Runnable command);
//    }
//    static class StartExecutor implements Executor{
//        @Override
//        public void execute(final Runnable command){
//            new Thread(command).start();
//        }
//
//    }
}