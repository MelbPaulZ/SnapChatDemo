package com.example.paul.snapchatdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.bean.FriendPhone;
import com.example.paul.snapchatdemo.bean.PhotoStory;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anita on 2016/10/17.
 */
public class PhotoStoryAdapter extends ArrayAdapter<PhotoStory> {
    private Context context;
    private int resourceId;
    public PhotoStoryAdapter(Context context, int resource, List<PhotoStory> objects) {
        super(context, resource, objects);
        this.resourceId =resource;
        this.context = context;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        // get one object content
        PhotoStory photoStory = getItem(position);
        System.out.println("@ "+photoStory.getImage());
        ViewHolder viewHolder;
        View view;
        if (convertView==null){
            //create a new ViewHolder
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            // store instance components into the viewHolder
            viewHolder.ivPhotoSecret =(ImageView) view.findViewById(R.id.photo_iv_secret);
            viewHolder.tvName = (TextView)view.findViewById(R.id.name_tv);
            // store the viewHolder into the view
            view.setTag(viewHolder);
        }else {
            view = convertView;
            //recover the viewHolder that store the previous instance components again
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvName.setText(photoStory.getId());
        if(photoStory.getImage()!=null){
                Picasso.with(context).load(photoStory.getImage()).into(viewHolder.ivPhotoSecret);
        }
        return view;
    }
    // Define a ViewHolder class
    class ViewHolder{
        ImageView ivPhotoSecret;
        TextView tvName;
    }
}
