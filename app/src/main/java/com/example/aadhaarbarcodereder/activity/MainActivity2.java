package com.example.aadhaarbarcodereder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.aadhaarbarcodereder.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity2 extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FragmentManager fm = getSupportFragmentManager();
//        Fragment f = fm.findFragmentById(R.id.fragmentContainer);
//
//        if (f == null) {
//            f = MainFragment.newInstance("Start Application");
//            fm.beginTransaction().add(R.id.fragmentContainer, f).commit();
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        System.out.println("the code is catch");

        IntentResult scanResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);
// handle scan result
        if (scanResult != null) {
            FragmentManager fm = getSupportFragmentManager();

            Fragment newFrame = MainFragment.newInstance(scanResult.toString());

          //  fm.beginTransaction().replace(R.id.fragmentContainer, newFrame).commit();
        }

    }



}