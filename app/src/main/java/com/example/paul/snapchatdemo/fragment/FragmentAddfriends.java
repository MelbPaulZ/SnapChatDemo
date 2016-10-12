package com.example.paul.snapchatdemo.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.activity.MainActivity;

import java.util.Set;

/**
 * Created by Anita on 2016/9/26.
 */
public class FragmentAddfriends extends Fragment implements View.OnClickListener {
    private View root;
    private Button buttonAddress;
    private Button buttonNearby;
    private Button buttonUsername;
    private Button buttonBackToUserscreen;
    private Button buttonShare;
    BluetoothAdapter bluetoothAdapter;
    private IntentFilter filter;
    @TargetApi(Build.VERSION_CODES.M)



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_addfriends, container, false);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAddfriends();
    }


    public void initAddfriends(){
        buttonAddress = (Button) root.findViewById(R.id.button_addressbook);
        buttonAddress.setOnClickListener(this);
        buttonNearby = (Button) root.findViewById(R.id.button_nearby);
        buttonNearby.setOnClickListener(this);
        //for android 6.0 users, you have to check the permission by using the following devices
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        buttonUsername = (Button) root.findViewById(R.id.button_username);
        buttonUsername.setOnClickListener(this);
        buttonBackToUserscreen=(Button)root.findViewById(R.id.addfriendsBtBack);
        buttonBackToUserscreen.setOnClickListener(this);
        buttonShare=(Button)root.findViewById(R.id.button_share);
        buttonShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_addressbook:
                ((MainActivity)getActivity()).fromAddfriendsToAddaddressbook();
                break;
            case R.id.button_nearby:
                startBluetoothSensor();
                break;
            case R.id.button_username:
                ((MainActivity)getActivity()).fromAddfriendsToAddusername();
                break;
            case R.id.addfriendsBtBack:
                ((MainActivity)getActivity()).fromAddfriendsToUserscreen();
                break;
            case R.id.button_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"));
                String username=((MainActivity)getActivity()).getUsername();
                intent.putExtra("sms_body","Add me on Snapchat! Username :"+username);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void startBluetoothSensor(){
        //ask the permission to enable your bluetooth device
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()==false){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }
        //to start scanning whether there are any other Bluetooth devices
        bluetoothAdapter.startDiscovery();

        //register the BroadcastReceiver to broadcast discovered devices
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(receiver, filter);

        //return paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                System.out.println("@ paired devices: "+device.getName());
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    //remember to unregister your broadcast receiver when the activity is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        // do not forget to unregister you broadcast receiver
    }

    //Create a BroadcastRecevier for ACTION_FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //when discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println("@ discovered devices: "+device.getName());
            }
        }
    };



}
