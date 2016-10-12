package com.example.paul.snapchatdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.bean.FriendPhone;

import java.util.List;

/**
 * Created by Anita on 2016/10/3.
 */
public class FriendPhoneAdapter extends ArrayAdapter<FriendPhone> {

    private int resourceId;
    public FriendPhoneAdapter(Context context, int resource, List<FriendPhone> objects) {
        super(context, resource, objects);
        this.resourceId =resource;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        // get one object content
        FriendPhone friendPhone = getItem(position);
        System.out.println("@ "+friendPhone.getName());
        ViewHolder viewHolder;
        View view;
        if (convertView==null){
            //create a new ViewHolder
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            // store instance components into the viewHolder
            viewHolder.tvName =(TextView)view.findViewById(R.id.name_tv);
            viewHolder.tvNumber = (TextView)view.findViewById(R.id.phone_tv);
            // store the viewHolder into the view
            view.setTag(viewHolder);
        }else {
            view = convertView;
            //recover the viewHolder that store the previous instance components again
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvName.setText(friendPhone.getName());
        viewHolder.tvNumber.setText(friendPhone.getPhone());
        return view;
    }
    // Define a ViewHolder class
    class ViewHolder{
        TextView tvName;
        TextView tvNumber;
    }
}
