package com.example.paul.snapchatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.api.UserApi;
import com.example.paul.snapchatdemo.bean.C;
import com.example.paul.snapchatdemo.bean.User;
import com.example.paul.snapchatdemo.utils.HttpUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paul on 20/09/2016.
 */
public class LoginActivity  extends AppCompatActivity{
    private final String TAG = "LoginActivity";
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initListeners();
    }



    public void initListeners(){
        // get the username
        final EditText userNameEditText = (EditText) findViewById(R.id.username);

        TextView login = (TextView) findViewById(R.id.login_tv);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the username from edit text
                userName = userNameEditText.getText().toString();

                Toast.makeText(getBaseContext(), "login now...Please be patient...",Toast.LENGTH_LONG).show();

                // get remote service
                UserApi userApi = HttpUtil.accessServer(UserApi.class);

                // this is for getting data back, asynchronous doing this task
                userApi.login(userName, C.methods.METHOD_LOGIN).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.i(TAG, "onResponse: " + response.body().toString());
                        User loginUser = response.body();
                        loginUser.toString();
                        // if server response data successfully, start main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + "userApi failure");
                    }
                });
            }
        });
    }
}
