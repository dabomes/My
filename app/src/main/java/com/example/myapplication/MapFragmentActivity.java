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
        //UiSettings uiSettings = naverMap.getUiSettings();
        //uiSettings.setCompassEnabled(true);
        //위치 오버레이 표시
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        //사용자 위치 변경(서울시청)
        //locationOverlay.setPosition(new LatLng(37.5670135, 126.9783740));
        //매물 정보(예 : 부산 시청)
        double lati = 35.1798159;
        double logi = 129.0750222;
        double[] la = {35.17912, 35.1798};
        double[] lo = {129.0745, 129.07664};
        String[] name = {"부산 경찰청", "시청역"};
        String apt_name = "부산광역시청";
        int apt_price = 101;
        //정보창 만들기
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "매물 이름 : " + apt_name + "\n가격 : "+apt_price+"억";
            }
        });

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
        int Mnum=2;

        Marker[] markers=new Marker[Mnum];
        for(int i=0; i<Mnum; i++){
            markers[i]=new Marker();
            markers[i].setPosition(new LatLng(la[i], lo[i]));
            markers[i].setCaptionText(name[i]);
            markers[i].setCaptionAligns(Align.Center);
            markers[i].setMap(naverMap);
        }

        // 마커 만들기
        Marker marker = new Marker(); //마커 생성
        marker.setPosition(new LatLng(lati, logi)); //좌표 설정
        marker.setCaptionText(apt_name); //마커 이름 설정
        marker.setCaptionAligns(Align.Center); //이름 위치 설정
        marker.setMap(naverMap);
        infoWindow.open(marker);
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