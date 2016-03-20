package com.scryp.dto;

import java.io.Serializable;

/**
 * Created by mayur.tailor on 06/11/2015.
 */

//{ "request":{ "methodName":"doRegister", "user_loginname":"shivams12", "user_password":"123456", "user_email":"shivams13@gmail.com", "user_device_token":"0f744707bebcf74f9b7c25d48e3358945f6aa01da5ddb387462c7eaf61bbad78" }, "doRegister":{ "replyCode":"success", "replyMessage":"Registered successfully.", "response":{ "data":{ "ID":"139", "user_login":"shivams12", "user_pass":"$P$Bp2qf0B8Sbx77RX.VvEyVzj7xHJXo01", "user_nicename":"shivams12", "user_email":"shivams13@gmail.com", "user_url":"", "user_registered":"2015-11-06 04:47:10", "user_activation_key":"", "user_status":"0", "display_name":"shivams12" }, "ID":139, "caps":{ "subscriber":true }, "cap_key":"wp_capabilities", "roles":[ "subscriber" ], "allcaps":{ "level_0":true, "read":true, "upload_files":true, "subscriber":true }, "filter":null } } }
public class UserDTO implements Serializable{
    public String ID;
    public String  user_login;
    public String  user_pass;
    public String  user_nicename;
    public String  user_email;
    public String  user_url;
    public String  user_registered;
    public String  user_activation_key;
    public String  user_status;
    public String  display_name;

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

    public String getUser_registered() {
        return user_registered;
    }

    public void setUser_registered(String user_registered) {
        this.user_registered = user_registered;
    }

    public String getUser_activation_key() {
        return user_activation_key;
    }

    public void setUser_activation_key(String user_activation_key) {
        this.user_activation_key = user_activation_key;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
