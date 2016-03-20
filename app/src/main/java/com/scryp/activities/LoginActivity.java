package com.scryp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.scryp.R;
import com.scryp.dto.UserDTO;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;
import com.scryp.utility.WebServiceCaller;
import com.scryp.utility.WebServiceListener;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginActivity extends BaseActivity implements WebServiceListener{

    private boolean isLogout;
    private CallbackManager callbackmanager;
    private LoginButton fbLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);
        showHashKey(this);
        init();
    }

    private void init(){
        setTouchNClick(R.id.btn_login);
        setTouchNClick(R.id.btn_signup);
        setTouchNClick(R.id.btn_tuto);
        setClick(R.id.tv_forgetpassword);

        setLeft(R.drawable.back);
        isLogout = getIntent().getBooleanExtra("isLogout", false);

        setClick(R.id.login_button);
        fbLogin = (LoginButton) findViewById(R.id.login_button);
        fbLogin.setBackgroundResource(R.drawable.backgound_fill);
        fbLogin.setText("Hi");
        fbLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.btn_login:
                if(validateForm()){
                    doLogin();
                }
                //startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.btn_signup:
                finish();
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.tv_forgetpassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.iv_left:
                finish();
                break;
            case R.id.login_button:
                onFblogin();
                break;
            case R.id.btn_tuto:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
        }
    }

    public void doLogin(){
        if(Utils.isOnline(this)){
            try {
            JSONObject json = new JSONObject();
            json.put("methodName", Constant.LOGIN_METHOD);
            json.put("user_loginname", getViewText(R.id.et_usename));
            json.put("user_password", getViewText(R.id.et_password));
            json.put("user_device_token", "0f744707bebcf74f9b7c25d48e3358945f6aa01da5ddb387462c7eaf61bbad78");
            new WebServiceCaller(LoginActivity.this, LoginActivity.this, Constant.SERVICE_BASE_URL+"?"
                    +"jsonRequest="+json.toString(),
                    null, 1).execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Utils.showNoNetworkDialog(this);
        }
    }

    public void doFbRegistration(JSONObject jsonObj, String fbToken){
        if(Utils.isOnline(this)){
            try {

                JSONObject json = new JSONObject();
                json.put("methodName", Constant.LOGIN_FB_METHOD);
                json.put("id", jsonObj.get("id"));
                json.put("name", jsonObj.get("name"));
                if(jsonObj.has("first_name"))
                    json.put("first_name", jsonObj.get("first_name"));
                if(jsonObj.has("last_name"))
                    json.put("last_name", jsonObj.get("last_name"));
                if(jsonObj.has("email"))
                    json.put("email", jsonObj.get("email"));
                if(jsonObj.has("link"))
                    json.put("link", jsonObj.get("link"));

                json.put("device_type", "android");
                json.put("fbToken", fbToken);
                json.put("user_device_token", "0f744707bebcf74f9b7c25d48e3358945f6aa01da5ddb387462c7eaf61bbad78");
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("jsonRequest", json.toString());
                new WebServiceCaller(LoginActivity.this, LoginActivity.this, Constant.SERVICE_BASE_URL,
                        params, 2).execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Utils.showNoNetworkDialog(this);
        }
    }

    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();

        //fbLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        fbLogin.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                System.out.println("Success");
                GraphRequest req = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json,
                                                    GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                    Log.i("info", "onCompleted Error.");
                                } else {
                                    System.out.println("Success");
                                    String jsonresult = String.valueOf(json);
                                    doFbRegistration(json, loginResult.getAccessToken().getToken());
                                }
                            }
                        }

                );
                Bundle param = new Bundle();
                param.putString("fields", "id,name,email,gender,birthday,first_name,last_name,link");
                req.setParameters(param);
                req.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("", "");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("","");
            }
        });

   /*     // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json,
                                                            GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            Log.i("info", "onCompleted Error.");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_firstname = json.getString("first_name");
                                                String str_lastname = json.getString("last_name");
                                                Toast.makeText(LoginActivity.this,
                                                        "Frist Name : " + str_firstname,
                                                        Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }
                        ).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Info", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Info", error.toString());
                    }
                }
        );*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        }
    }
    public boolean validateForm(){
        if(getViewText(R.id.et_usename).equals("")){
            Toast.makeText(LoginActivity.this, "Please enter Username", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(getViewText(R.id.et_password).equals("")){
            Toast.makeText(LoginActivity.this, "Please enter Password", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void onTaskComplete(String result, int requestCode, int resultCode) {

        if(resultCode==Constant.RESULT_OK){
            if(requestCode==1){
                if(Utils.getWebServiceStatus(result,Constant.LOGIN_METHOD)){
                    UserDTO userDTO = new Gson().fromJson(Utils.getResponseInObject(result,Constant.LOGIN_METHOD), UserDTO.class);
                    Utils.putObjectIntoPref(LoginActivity.this, userDTO, Constant.USER_INFO);
                    if(isLogout){
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    }else {
                        finish();
                    }
                }else{
                    Utils.showDialog(LoginActivity.this,  "Message", Utils.getWebServiceMessage(result,Constant.LOGIN_METHOD));
                }
            }

            if(requestCode==2){
                if(Utils.getWebServiceStatus(result,Constant.LOGIN_FB_METHOD)){
                    UserDTO userDTO = new Gson().fromJson(Utils.getResponseInObject(result,Constant.LOGIN_FB_METHOD), UserDTO.class);
                    Utils.putObjectIntoPref(LoginActivity.this, userDTO, Constant.USER_INFO);
                    if(isLogout){
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    }else {
                        finish();
                    }
                }else{
                    Utils.showDialog(LoginActivity.this,  "Message", Utils.getWebServiceMessage(result,Constant.LOGIN_FB_METHOD));
                }
            }
        }else{
            Utils.showExceptionDialog(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    public void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.scryp",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", sign);
                //  Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
            Log.d("KeyHash:", "****------------***");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
