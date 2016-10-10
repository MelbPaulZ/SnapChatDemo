package com.example.paul.snapchatdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.bean.Friend;
import com.example.paul.snapchatdemo.manager.FriendManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 10/10/16.
 */
public class ContactAdapter extends ArrayAdapter<Friend> {
    private Context context;
    private int resource;
    private List<Friend> friends;

    public ContactAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.friends = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final Friend friend = this.getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource, null );
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.friend_checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FriendManager.getInstance().isFriendSelected(friend)){
                    FriendManager.getInstance().getSelectedFriendList().remove(friend);
                }else {
                    FriendManager.getInstance().getSelectedFriendList().add(friend);
                }
            }
        });

        TextView name = (TextView) view.findViewById(R.id.friend_selection_name);
        name.setText(friends.get(position).getName());
        return view;
    }

}
