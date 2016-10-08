package com.example.paul.snapchatdemo.firebase;

import com.example.paul.snapchatdemo.chat.Token;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = Token.generateToken();
        Token.registerToken(token, "verra1");
    }

}
