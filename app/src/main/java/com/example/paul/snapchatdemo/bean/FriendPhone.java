package com.example.paul.snapchatdemo.bean;

/**
 * Created by Anita on 2016/10/3.
 */
public class FriendPhone {


    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public FriendPhone(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "FriendPhone{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }


}
