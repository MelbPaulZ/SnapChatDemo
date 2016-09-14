package com.example.paul.snapchatdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.helpers.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 7/09/2016.
 */
public class FriendAdapter extends ArrayAdapter<Friend> implements Filterable{
    private FriendFilter friendFilter;
    private ArrayList<Friend> friendArrayList;
    private ArrayList<Friend> filteredArrayList;

    private TextView nameTV;
    private TextView subTitle;
    private ImageView icon;
    private int resource;

    public FriendAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.friendArrayList = (ArrayList<Friend>) objects;
        this.filteredArrayList = (ArrayList<Friend>) objects;
        getFilter();
    }


    public View getView(int position, View convertView, ViewGroup parent){
        Friend friend = getItem(position);

        // here can be set more
        View view = LayoutInflater.from(getContext()).inflate(resource, null );
        nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(friend.getName());

        subTitle = (TextView) view.findViewById(R.id.subtitle_tv);
        subTitle.setText(getStatus(friend));

        icon = (ImageView) view.findViewById(R.id.icon_img);
        icon.setImageResource(getIcon(friend));

        return view;
    }

    public String getStatus(Friend friend){
        if (friend.getStatus() == ""){
            return "Hi, I am using snapchat";
        }else
            return friend.getStatus();
    }

    // this needs to change later, for customisation
    public int getIcon(Friend friend){
        if (friend.getGender() == "male"){
            return R.drawable.male;
        }else if (friend.getGender() == "female"){
            return R.drawable.female;
        }
        return R.drawable.arrow;
    }

    @Override
    public Filter getFilter() {
        if ( friendFilter == null) {
            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }

    @Override
    public int getCount() {
        return filteredArrayList.size();
    }

    @Override
    public Friend getItem(int i) {
        return filteredArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constrains) {
            FilterResults filterResults = new FilterResults();
            if (constrains!=null && constrains.length()>0){
                ArrayList<Friend> showingFriendArrayList = new ArrayList<>();
                for (Friend friend : friendArrayList){
                    if (friend.getName().toLowerCase().contains(constrains.toString().toLowerCase())){
                        showingFriendArrayList.add(friend);
                    }
                }

                filterResults.count = showingFriendArrayList.size();
                filterResults.values = showingFriendArrayList;
            }else{
                filterResults.count = friendArrayList.size();
                filterResults.values = friendArrayList;
            }
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredArrayList = (ArrayList<Friend>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}
