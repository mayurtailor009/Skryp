package com.scryp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scryp.R;
import com.scryp.dto.UserDTO;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;
import com.scryp.utility.WebServiceCaller;
import com.scryp.utility.WebServiceListener;

import org.json.JSONObject;

import java.util.HashMap;

public class SignupActivity extends BaseActivity implements WebServiceListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
    }

    private void init(){
        setHeader("Sign Up");
        setLeft(R.drawable.back);
        setTouchNClick(R.id.btn_signup);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_signup:
                if(validateForm()){
                    doSignup();
                }
                break;
        }
    }

    public void doSignup(){
        if(Utils.isOnline(this)){
            try {
                JSONObject json = new JSONObject();
                json.put("methodName", Constant.SIGNUP_METHOD);
                json.put("user_loginname", getViewText(R.id.et_usename));
                json.put("user_password", getViewText(R.id.et_password));
                json.put("user_email", getViewText(R.id.et_email));
                json.put("user_device_token", "0f744707bebcf74f9b7c25d48e3358945f6aa01da5ddb387462c7eaf61bbad78");
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("jsonRequest", json.toString());
                new WebServiceCaller(SignupActivity.this, SignupActivity.this, Constant.SERVICE_BASE_URL+"?"
                        +"jsonRequest="+json.toString(),
                        null, 1).execute();
            }catch (Exception e){
                e.printStackTrace();;
            }
        }else{
            Utils.showNoNetworkDialog(this);
        }
    }

    public boolean validateForm(){
        if(getViewText(R.id.et_usename).equals("")){
            Toast.makeText(SignupActivity.this, "Please enter Username", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(getViewText(R.id.et_email).equals("")){
            Toast.makeText(SignupActivity.this, "Please enter Email Id", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(getViewText(R.id.et_password).equals("")){
            Toast.makeText(SignupActivity.this, "Please enter Password", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(getViewText(R.id.et_repassword).equals("")){
            Toast.makeText(SignupActivity.this, "Please enter Confirm Password", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!getViewText(R.id.et_repassword).equals(getViewText(R.id.et_password))){
            Toast.makeText(SignupActivity.this, "Password does not match", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void onTaskComplete(String result, int requestCode, int resultCode) {

        if(resultCode==Constant.RESULT_OK){
            if(requestCode==1){
                if(Utils.getWebServiceStatus(result,Constant.SIGNUP_METHOD)){
                    UserDTO userDTO = new Gson().fromJson(Utils.getResponseInObject(result,Constant.SIGNUP_METHOD), UserDTO.class);
                    Utils.putObjectIntoPref(SignupActivity.this, userDTO, Constant.USER_INFO);
                    finish();
                }else{
                    Utils.showDialog(SignupActivity.this,  "Message", Utils.getWebServiceMessage(result,Constant.SIGNUP_METHOD));
                }
            }
        }else{
            Utils.showExceptionDialog(this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
