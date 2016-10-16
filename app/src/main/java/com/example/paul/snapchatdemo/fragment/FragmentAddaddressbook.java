package com.example.paul.snapchatdemo.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anita on 2016/9/26.
 */
public class FragmentAddaddressbook extends Fragment implements View.OnClickListener{
    private View root;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private final String TAG = "ADDaddressbook";
    private Button buttonBackToAddfriends;

    ListView contactsView;
    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_addaddressbook, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAddressbook();
    }


    public void initAddressbook(){
        buttonBackToAddfriends=(Button)root.findViewById(R.id.addaddressbookBtBack);
        buttonBackToAddfriends.setOnClickListener(this);
        contactsView = (ListView) root.findViewById(R.id.contacts_view);
        showContacts();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addaddressbookBtBack:
                ((MainActivity)getActivity()).fromAddaddressbookToAddfriends();
                break;
            default:
                break;
        }
    }

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, contactsList);
            //System.out.println("2");
            contactsView.setAdapter(adapter);
            //System.out.println("3");
            readContacts();

            contactsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ListView listView = (ListView) parent;
                    String telename = (String) listView.getItemAtPosition(position);
                    int s=telename.indexOf("\n");
                    String telephone=telename.substring(s+1).replace(" ", "");
                    //System.out.println("telename is "+ telename);
                    System.out.println("tele "+telephone+".length "+telephone.length());
                    Toast.makeText(FragmentAddaddressbook.this.getActivity().getBaseContext(),"searching for "+telephone, Toast.LENGTH_SHORT).show();
                    // get remote service
                    UserApi userApi = HttpUtil.accessServer(UserApi.class);

                    // this is for getting data back, asynchronous doing this task
                    userApi.searchtelephone(telephone, C.methods.METHOD_SEARCHTELEPHONE).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            //Log.i(TAG, "onResponse: " + response.body().toString());
                            String friendusername=response.body().getUserName();
                            String frienduserid=response.body().getId();
                            ((MainActivity)getActivity()).setFriendUsername(friendusername);
                            ((MainActivity)getActivity()).setFriend_userid(frienduserid);
                            // if server response data successfully, start main activity
                            ((MainActivity)getActivity()).fromAddaddressbookToResultfriend();

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.i(TAG, "onFailure: " + "userApi failure");
                            Toast.makeText(FragmentAddaddressbook.this.getActivity().getBaseContext(),
                                    "cannot find user with this telephone number", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void readContacts() {
        Cursor cursor = null;
        try {
// 查询联系人数据
            cursor = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            //System.out.println("4");
            while (cursor.moveToNext()) {
// 获取联系人姓名
                String displayName = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
// 获取联系人手机号
                String number = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                System.out.println("displayName:"+displayName+"\n"+"number"+number);
                contactsList.add(displayName + "\n" + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
