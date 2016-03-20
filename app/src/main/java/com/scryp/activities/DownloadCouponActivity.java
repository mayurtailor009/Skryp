package com.scryp.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.scryp.R;
import com.scryp.dto.UserDTO;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;

public class DownloadCouponActivity extends BaseActivity {

    private WebView wvCoupon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_coupon);

        init();
    }

    private void init(){
        setHeader("");
        setLeft(R.drawable.back);

        wvCoupon = (WebView) findViewById(R.id.wb_coupon);
        long couponId = getIntent().getLongExtra("couponId",1);
        UserDTO userDTO = Utils.getObjectFromPref(this, Constant.USER_INFO);
        String url = "http://scryp.sg/ajaxHandeler.php?user_id="+userDTO.getID()+"&coupon_id="+couponId+"&func=insertVoucherMobile";
        wvCoupon.loadUrl(url);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.iv_left:
                finish();
                break;
        }
        super.onClick(arg0);
    }
}
