package com.example.paul.snapchatdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.bean.Friend;
import com.example.paul.snapchatdemo.manager.FriendManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 7/09/2016.
 */
public class FriendAdapter extends ArrayAdapter<Friend> implements Filterable{
    private FriendFilter friendFilter;
    private ArrayList<Friend> friendArrayList;
    private ArrayList<Friend> filteredArrayList = new ArrayList<>();

    private RelativeLayout contactBox;
    private TextView nameTV;
    private TextView subTitle;
    private ImageView icon;
    private TextView friendShipTV;
    private int resource;

    public FriendAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.friendArrayList = (ArrayList<Friend>) objects;
        this.filteredArrayList = (ArrayList<Friend>) objects;
        getFilter();
    }


    public View getView(int position, View convertView, ViewGroup parent){
        final Friend friend = getItem(position);

        // here can be set more
        View view = LayoutInflater.from(getContext()).inflate(resource, null );

        contactBox = (RelativeLayout) view.findViewById(R.id.contact_box);
        contactBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChatScreen(friend);
            }
        });

        nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(friend.getName());

        subTitle = (TextView) view.findViewById(R.id.subtitle_tv);
        subTitle.setText(getStatus(friend));

        icon = (ImageView) view.findViewById(R.id.icon_img);
        icon.setImageResource(getIcon(friend));

        friendShipTV = (TextView) view.findViewById(R.id.friendship_time);
        friendShipTV.setText(FriendManager.getInstance().friendShipTime(friend.getAddFriendTime()));
        return view;
    }

    private void showChatScreen(Friend friend) {
        ((MainActivity)(Activity) getContext()).contactToChatScreen(friend);
    }

    public String getStatus(Friend friend){
        if (friend.getStatus() == ""){
            return "Hi, I am using snapchat";
        }else
            return friend.getStatus();
    }

    // this needs to change later, for customisation
    public int getIcon(Friend friend){
        if (friend.getGender().equals("male")){
            return R.drawable.male;
        }else if (friend.getGender().equals("female")){
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
