package com.scryp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.scryp.R;
import com.scryp.dto.UserDTO;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;

public class ProfileActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    private void init(){
        FacebookSdk.sdkInitialize(this);
        setHeader("Profile");
        setLeft(R.drawable.btnlogout);

        setFooterClick();
        setProfileSelected();
        UserDTO userDTO = Utils.getObjectFromPref(this, Constant.USER_INFO);
        if(userDTO!=null) {
            setViewText(R.id.et_name, userDTO.getUser_nicename());
            setViewText(R.id.et_email, userDTO.getUser_email());
            setViewText(R.id.et_password, userDTO.getUser_pass());
        }
        setTouchNClick(R.id.btn_tuto);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.iv_left:
                Utils.removeObjectIntoPref(this, Constant.USER_INFO);
                LoginManager.getInstance().logOut();
                finish();
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra("isLogout",true);
                startActivity(i);
                break;
            case R.id.btn_tuto:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
        }
        super.onClick(arg0);
    }



}
