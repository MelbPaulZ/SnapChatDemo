package com.example.paul.snapchatdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Paul on 24/08/2016.
 */
public class FragmentContactList extends Fragment {
    private View root;

    private ArrayList<String> friendArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.fragment_chat_list, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init(){
        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.scrollview_linear_layout);
        linearLayout.setPadding(10,0,10,0);

        for (String name:friendArrayList){
            RelativeLayout nameSlot = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams nameSlotParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
            nameSlot.setLayoutParams(nameSlotParams);
            nameSlot.setBackground(getResources().getDrawable(R.drawable.table_row_border));

            TextView textView = new TextView(getContext());
            textView.setText(name);
            textView.setTextSize(20);
            RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textViewLayoutParams.setMargins(10+100,20,0,0);
            textView.setLayoutParams(textViewLayoutParams);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            TextView status = new TextView(getContext());
            status.setText("last update in a minute ago");
            status.setTextSize(10);
            status.setTextColor(Color.GRAY);
            RelativeLayout.LayoutParams statusLayout = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            statusLayout.setMargins(10+100, 90, 0 ,0);
            status.setLayoutParams(statusLayout);

            ImageView icon = new ImageView(getContext());
            RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(80,100);
            iconParams.setMargins(10,20,0,0);
            icon.setLayoutParams(iconParams);
            icon.setImageResource(R.drawable.arrow);

            ImageView genderIcon = new ImageView(getContext());
            RelativeLayout.LayoutParams genderIconParams = new RelativeLayout.LayoutParams(50,50);
            genderIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            genderIconParams.addRule(RelativeLayout.CENTER_VERTICAL);
            genderIcon.setLayoutParams(genderIconParams);
            genderIcon.setImageResource(R.drawable.female);

            nameSlot.addView(textView);
            nameSlot.addView(status);
            nameSlot.addView(icon);
            nameSlot.addView(genderIcon);
            linearLayout.addView(nameSlot);
        }
    }
    public void setNames(ArrayList<String> nameArrayList){
        this.friendArrayList = nameArrayList;
    }

}
