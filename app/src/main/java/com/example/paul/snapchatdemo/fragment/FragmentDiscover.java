package com.example.paul.snapchatdemo.fragment;


import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.bean.DiscoveryUrl;
import com.example.paul.snapchatdemo.manager.UrlManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 24/08/2016.
 */
public class FragmentDiscover extends Fragment implements View.OnClickListener {
    private View root;
    private CustomTabsIntent customTabsIntent;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDiscovers();
    }


    public void initDiscovers(){
        imageView1 = (ImageView) root.findViewById(R.id.discover_image1);
        imageView2 = (ImageView) root.findViewById(R.id.discover_image2);
        imageView3 = (ImageView) root.findViewById(R.id.discover_image3);
        imageView4 = (ImageView) root.findViewById(R.id.discover_image4);
        imageView5 = (ImageView) root.findViewById(R.id.discover_image5);
        imageView6 = (ImageView) root.findViewById(R.id.discover_image6);
        imageView7 = (ImageView) root.findViewById(R.id.discover_image7);
        imageView8 = (ImageView) root.findViewById(R.id.discover_image8);

        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(0).getUrl()).into(imageView1);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(1).getUrl()).into(imageView2);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(2).getUrl()).into(imageView3);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(3).getUrl()).into(imageView4);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(4).getUrl()).into(imageView5);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(5).getUrl()).into(imageView6);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(6).getUrl()).into(imageView7);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(7).getUrl()).into(imageView8);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        Bitmap icon = BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_arrow_back);
        builder.setCloseButtonIcon(icon);
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        customTabsIntent = builder.build();

        if (viewId == R.id.discover_image1 ){
            gotoUrlAndUpdate(0);
        }else if (viewId == R.id.discover_image2){
            gotoUrlAndUpdate(1);
        }else if (viewId == R.id.discover_image3){
            gotoUrlAndUpdate(2);
        }else if (viewId == R.id.discover_image4){
            gotoUrlAndUpdate(3);
        }else if (viewId == R.id.discover_image5){
            gotoUrlAndUpdate(4);
        }else if (viewId == R.id.discover_image6){
            gotoUrlAndUpdate(5);
        }else if (viewId == R.id.discover_image7){
            gotoUrlAndUpdate(6);
        }else if (viewId == R.id.discover_image8){
            gotoUrlAndUpdate(7);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDiscovery();
            }
        }, 3000);
    }

    private void gotoUrlAndUpdate(int index){
        String url = UrlManager.getInstance().getUrls().get(index).getTextUrl();
        int topic = UrlManager.getInstance().getUrls().get(index).getTopic();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
        UrlManager.getInstance().updateUrls(topic);
    }

    private void updateDiscovery(){
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(0).getUrl()).into(imageView1);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(1).getUrl()).into(imageView2);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(2).getUrl()).into(imageView3);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(3).getUrl()).into(imageView4);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(4).getUrl()).into(imageView5);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(5).getUrl()).into(imageView6);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(6).getUrl()).into(imageView7);
        Picasso.with(getContext()).load(UrlManager.getInstance().getUrls().get(7).getUrl()).into(imageView8);
    }
}
