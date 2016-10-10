package com.example.paul.snapchatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.adapters.ContactAdapter;
import com.example.paul.snapchatdemo.adapters.FriendAdapter;
import com.example.paul.snapchatdemo.bean.Friend;
import com.example.paul.snapchatdemo.manager.FriendManager;

import java.util.List;

/**
 * Created by Paul on 9/10/16.
 */
public class FragmentFriendSelection extends Fragment implements View.OnClickListener{
    private View root;
    private TextView backBtn, nextBtn;
    private ContactAdapter contactAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_friend_selection, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListeners();
        initContent();
    }

    public void initListeners(){
        backBtn = (TextView) root.findViewById(R.id.friend_selection_back_btn);
        backBtn.setOnClickListener(this);
        nextBtn = (TextView) root.findViewById(R.id.friend_selection_done_btn);
        nextBtn.setOnClickListener(this);
    }

    public void initContent(){
        ListView contactList = (ListView) root.findViewById(R.id.friend_selection_listview);
        contactAdapter = new ContactAdapter(getContext(), R.layout.friend_selection_single_line, FriendManager.getInstance().getFriendList());
        contactList.setAdapter(contactAdapter);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.friend_selection_back_btn:
                // set the back listener here
                Toast.makeText(getContext(), "on click back",Toast.LENGTH_SHORT).show();
                break;
            case R.id.friend_selection_done_btn:
                // set the done listener here
                List<Friend> friendList = FriendManager.getInstance().getSelectedFriendList();
                Log.i("myApp", "onClick: " + FriendManager.getInstance().getSelectedFriendList().size());
                Toast.makeText(getContext(), "on click done", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), "don't know what is clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
