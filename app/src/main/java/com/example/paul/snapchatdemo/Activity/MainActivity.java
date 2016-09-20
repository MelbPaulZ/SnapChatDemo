package com.example.paul.snapchatdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.paul.snapchatdemo.fragment.FragmentMain;
import com.example.paul.snapchatdemo.R;

public class MainActivity extends AppCompatActivity {
    private FragmentMain fragmentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentMain = new FragmentMain();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragmentMain).commit();
    }

    public FragmentMain getFragmentMain(){
        return fragmentMain;
    }

}
