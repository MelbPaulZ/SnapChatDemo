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
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.paul.snapchatdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

/**
 * Created by Paul on 24/08/2016.
 */
public class FragmentDiscover extends Fragment implements View.OnClickListener {
    private View root;

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
        String url1 ="http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg" ;
        ImageView imageView1 = (ImageView) root.findViewById(R.id.discover_image1);
        Picasso.with(getContext()).load(url1).into(imageView1);
        imageView1.setOnClickListener(this);

        String url2 = "http://rter.info/image.php?iso=AUD&name=1-8cc9325417c87e4405fe57b8f371582c";
        ImageView imageView2 = (ImageView) root.findViewById(R.id.discover_image2);
        Picasso.with(getContext()).load(url2).into(imageView2);
        imageView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        Bitmap icon = BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_arrow_back);
        builder.setCloseButtonIcon(icon);
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        if (viewId == R.id.discover_image1 ){
            String url = "http://www.wonderslist.com/most-beautiful-women-of-2016/";
            customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
        }else if (viewId == R.id.discover_image2){
            String url ="http://www.news.com.au/finance";
            customTabsIntent.launchUrl(getActivity(), Uri.parse(url));


        }
    }



}
