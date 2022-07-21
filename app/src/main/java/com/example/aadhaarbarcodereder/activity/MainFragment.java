package com.example.aadhaarbarcodereder.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aadhaarbarcodereder.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static final String EXTRA_CODE = "com.example.testingcodereading.code";
    private Button mButtonXZing;
    private TextView mTextView;

    public static MainFragment newInstance(String code) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CODE, code);

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle    savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_main, parent, false);

       // mTextView = (TextView) v.findViewById(R.id.text_code);
        mTextView.setText((String) getArguments().getSerializable(EXTRA_CODE));

       // mButtonXZing = (Button) v.findViewById(R.id.button_xzing);
        mButtonXZing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.initiateScan();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        System.out.println("never here");
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
        }
// else continue with any other code you need in the method
    }

}