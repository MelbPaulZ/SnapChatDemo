package com.example.paul.snapchatdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.snapchatdemo.R;
import com.example.paul.snapchatdemo.chat.Token;
import com.example.paul.snapchatdemo.presenter.LoginPresenter;
import com.example.paul.snapchatdemo.utils.UserUtil;

/**
 * Created by Paul on 20/09/2016.
 */
public class LoginActivity  extends AppCompatActivity{
    private final String TAG = "LoginActivity";
    private String userName;
    private String passWord;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(getBaseContext());
        presenter.setActivity(this);
        initListeners();
    }



    public void initListeners(){
        // get the username
        final EditText userNameEditText = (EditText) findViewById(R.id.username);

        final TextView login = (TextView) findViewById(R.id.login_tv);

        final EditText passWordEditText = (EditText) findViewById(R.id.password);

        final TextView register = (TextView) findViewById(R.id.register_tv);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = userNameEditText.getText().toString();
                passWord = passWordEditText.getText().toString();
                presenter.signup(userName, passWord);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the username from edit text
                userName = userNameEditText.getText().toString();
                passWord = passWordEditText.getText().toString();

                Toast.makeText(getBaseContext(), "login now...Please be patient...", Toast.LENGTH_LONG).show();
                presenter.login(userName, passWord);
            }
        });
    }

    private void registerToken() {
        String token = Token.generateToken();
        Token.registerToken(token, UserUtil.getId());
    }

    public void loginSuccessful(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerToken();
            }
        }).start();

        // if server response data successfully, start main activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
