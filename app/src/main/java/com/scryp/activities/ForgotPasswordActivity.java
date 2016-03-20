package com.scryp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.scryp.R;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;
import com.scryp.utility.WebServiceCaller;
import com.scryp.utility.WebServiceListener;

import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity implements WebServiceListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        init();
    }

    private void init(){
        setHeader("Forgot Password");
        setLeft(R.drawable.back);
        setTouchNClick(R.id.btn_submit);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_submit:
                if(validateForm())
                    forgetPassword();
                break;
        }
    }

    public boolean validateForm(){
        if(getViewText(R.id.et_usename).equals("")){
            Toast.makeText(this, "Please enter Username", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void forgetPassword(){
        if(Utils.isOnline(this)){
            try {
                JSONObject json = new JSONObject();
                json.put("methodName", Constant.FORGET_PASSWORD_METHOD);
                json.put("user_login", getViewText(R.id.et_usename));
                new WebServiceCaller(ForgotPasswordActivity.this, ForgotPasswordActivity.this, Constant.SERVICE_BASE_URL+"?"
                        +"jsonRequest="+json.toString(),
                        null, 1).execute();
            }catch (Exception e){
                e.printStackTrace();;
            }
        }else{
            Utils.showNoNetworkDialog(this);
        }
    }

    @Override
    public void onTaskComplete(String result, int requestCode, int resultCode) {

        if(resultCode==Constant.RESULT_OK){
            if(requestCode==1){
                if(Utils.getWebServiceStatus(result,Constant.FORGET_PASSWORD_METHOD)){
                    Utils.showDialog(this, "Message", Utils.getWebServiceMessage(result, Constant.FORGET_PASSWORD_METHOD),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                }else{
                    Utils.showDialog(this,  "Message", Utils.getWebServiceMessage(result,Constant.FORGET_PASSWORD_METHOD));
                }
            }
        }else{
            Utils.showExceptionDialog(this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
