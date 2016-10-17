package com.example.paul.snapchatdemo.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.squareup.picasso.Picasso;


public class FragmentShowImageTimer extends android.support.v4.app.Fragment {
    /**
     * Initialize fragment
     */
    private View root;
    LayoutInflater inflater;
    Bundle savedInstanceState;

    // default message timer is 5 seconds
    public int messageTimer = 5;
    public String imageUrl= "none";
    public int messageIdx = -1;

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden){
            // screen is shown
            Picasso.with(root.getContext()).load(imageUrl).into(timerImageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    // start showing the timer
                    loadingPanel.setVisibility(View.GONE);
                    timerTextView.setVisibility(View.VISIBLE);

                    int millisMessageTimer = (messageTimer + 1) * 1000;

                    new CountDownTimer(millisMessageTimer, 1000) {
                        public void onTick(long millisUntilFinished) {
                            timerTextView.setText("" + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            // redirect to chat screen
                            ((MainActivity) getContext()).fromShowImageToChatScreen();
                        }
                    }.start();

                }

                @Override
                public void onError() {
                    Toast.makeText(root.getContext(), "Failed to load image.", Toast.LENGTH_LONG).show();
                }
            });


        }
        else {
            // make it gone again
            timerTextView.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
    }

    /**
     * Initialize image editor
     */
    ImageView timerImageView;
    TextView timerTextView;
    ProgressBar loadingPanel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timerImageView = (ImageView)root.findViewById(R.id.timerImageView);
        timerTextView = (TextView)root.findViewById(R.id.timerTextView);
        loadingPanel = (ProgressBar)root.findViewById(R.id.loadingPanel);
    }

}