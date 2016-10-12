package com.example.paul.snapchatdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.adapters.FriendPhoneAdapter;
import com.example.paul.snapchatdemo.bean.FriendPhone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anita on 2016/9/26.
 */
public class FragmentAddedme extends Fragment implements View.OnClickListener{
    private View root;
    private final String TAG = "ADDEDme";
    private List<FriendPhone> friendPhoneList = new ArrayList<>();
    private List<FriendPhone> friendPhoneList1 = new ArrayList<>();
    private Button buttonBackToUserscreen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_addedme, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAddedme();

    }


    public void initAddedme(){
        buttonBackToUserscreen =(Button)root.findViewById(R.id.addedmeBtBack);
        buttonBackToUserscreen.setOnClickListener(this);
        ListView addedmeList = (ListView) root.findViewById(R.id.addedme_list);


/*        for(int i = 0 ; i < 3 ; i ++) {
            friendPhoneList.add(new FriendPhone("Paul","0422062674"));
            friendPhoneList.add(new FriendPhone("Anita","0403141578"));
            friendPhoneList.add(new FriendPhone("Adam","0423062674"));
        }*/
        friendPhoneList1=(ArrayList<FriendPhone>)(((MainActivity)getActivity()).getFriendPhoneList().clone());
/*        for(int i=0;i<friendPhoneList1.size();i++)
        {
            System.out.println("Notice:"+friendPhoneList1.get(i));
        }*/

        FriendPhoneAdapter friendPhoneAdapter = new FriendPhoneAdapter(getContext(), R.layout.added_single_line_view, friendPhoneList1);
        addedmeList.setAdapter(friendPhoneAdapter);
        addedmeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                FriendPhone friendPhone = (FriendPhone) listView.getItemAtPosition(position);
                String name = friendPhone.getName();
                ((MainActivity)getActivity()).setFriendUsername(name);
                System.out.println("Press "+name);
                ((MainActivity)getActivity()).fromAddedmeToResultAddedme();

                //notifies that the data has been changed and any View reflecting the data set
                // should refresh itself
                //adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addedmeBtBack:
                ((MainActivity)getActivity()).fromAddedmeToUserscreen();
                break;
            default:
                break;
        }
    }

}

