package com.example.paul.snapchatdemo.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.gesture.Gesture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.adapters.FriendAdapter;
import com.example.paul.snapchatdemo.helpers.OnPageSlideListener;
import com.example.paul.snapchatdemo.manager.FriendManager;

/**
 * Created by Paul on 24/08/2016.
 */
public class FragmentContactList extends Fragment implements SearchView.OnQueryTextListener, OnPageSlideListener, GestureDetector.OnGestureListener{
    private View root;
    private String TAG = "FragmentContactList";
    private FriendAdapter friendArrayAdapter;
    GestureDetector detector;
    private boolean isScollDown = false;

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

        detector = new GestureDetector(getContext(), this);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                 detector.onTouchEvent(motionEvent);
                return true;
            }
        });
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
        friendArrayAdapter = new FriendAdapter(getContext(), R.layout.contact_single_line_view, FriendManager.getInstance().getFriendList());
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
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            isScollDown = false;
        }
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
        ViewPager viewPager = ((MainActivity)getActivity()).getFragmentMain().getViewPager();
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        // do nothing, it is the most left one
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        if (distanceY<-10){
            ((MainActivity)getActivity()).contactToUserscreen(); // to add friend
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }
}
