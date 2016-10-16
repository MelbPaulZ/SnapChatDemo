package com.example.paul.snapchatdemo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.squareup.picasso.Picasso;


public class FragmentShowImageTimer extends Fragment {
    /**
     * Initialize fragment
     */
    private View root;
    LayoutInflater inflater;
    Bundle savedInstanceState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root= inflater.inflate(R.layout.fragment_show_image_timer, container, false);
        this.savedInstanceState = savedInstanceState;
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
    }

    public String imagePath= "file:///storage/emulated/0/WhatsApp/Media/WallPaper/Dandenong.jpg";

    /**
     * Initialize image editor
     */
    ImageView timerImageView;
    TextView timerTextView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timerImageView = (ImageView)root.findViewById(R.id.timerImageView);
        Picasso.with(root.getContext()).load(imagePath).into(timerImageView);

        timerTextView = (TextView)root.findViewById(R.id.timerTextView);

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(""+millisUntilFinished / 1000);
            }

            public void onFinish() {
                // redirect to chat screen
            }
        }.start();
    }

}