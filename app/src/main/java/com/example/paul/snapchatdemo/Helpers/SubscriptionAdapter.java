package com.example.paul.snapchatdemo.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Paul on 13/09/2016.
 */
public class SubscriptionAdapter extends ArrayAdapter<String> implements View.OnClickListener {
    private Context context;
    private List<String> urls;
    private int resource;
    public SubscriptionAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.urls = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.subscription_image);

        Picasso.with(context).load(urls.get(position)).into(imageView);
        imageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(context, "On Click image", Toast.LENGTH_SHORT).show();
    }
}
