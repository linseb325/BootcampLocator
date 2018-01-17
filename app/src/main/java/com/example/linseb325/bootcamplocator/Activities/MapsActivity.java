package com.example.linseb325.bootcamplocator.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.linseb325.bootcamplocator.Fragments.MainFragment;
import com.example.linseb325.bootcamplocator.R;

public class MapsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MainFragment mainFrag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.container_main);

        if (mainFrag == null) {
            mainFrag = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, mainFrag).commit();
        }
    }

}
