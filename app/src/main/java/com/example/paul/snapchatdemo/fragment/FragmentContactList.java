package com.example.paul.snapchatdemo.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.bean.Friend;
import com.example.paul.snapchatdemo.adapters.FriendAdapter;
import com.example.paul.snapchatdemo.helpers.OnPageSlideListener;

import java.util.ArrayList;

/**
 * Created by Paul on 24/08/2016.
 */
public class FragmentContactList extends Fragment implements SearchView.OnQueryTextListener, OnPageSlideListener{
    private View root;

    private FriendAdapter friendArrayAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.fragment_chat_list, container, false);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initListener();

        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) root.findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    public void initListener(){
        TextView right = (TextView) root.findViewById(R.id.contact_right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveNext();
            }
        });
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchMenuItem;
        SearchView searchView;
        MenuInflater myInflater = getActivity().getMenuInflater();
        myInflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }





    public void init(){
        ListView contactList = (ListView) root.findViewById(R.id.contact_list);

        // simulate an arrayList data
        ArrayList<Friend> simuArrayList = new ArrayList<>();
        for(int i = 0 ; i < 5 ; i ++) {
            simuArrayList.add(new Friend("Paul","message me","male"));
            simuArrayList.add(new Friend("William","I am having lunch","male"));
            simuArrayList.add(new Friend("Verra","coding now","female"));
            simuArrayList.add(new Friend("Aaron","lecturing, busy","male"));
            simuArrayList.add(new Friend("Brad Peter","guess where I am"));
        }

        friendArrayAdapter = new FriendAdapter(getContext(), R.layout.contact_single_line_view, simuArrayList);
        contactList.setAdapter(friendArrayAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.menu.search_menu:

                // Do Fragment menu item stuff here
                return true;

            default:
                break;
        }

        return false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        friendArrayAdapter.getFilter().filter(newText);

        return false;
    }

    @Override
    public void moveNext() {
        ViewPager viewPager = ((MainActivity)getActivity()).getFragmentMain().getViewPager();
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    @Override
    public void movePrevious() {
        // do nothing, it is the most left one
    }
}
