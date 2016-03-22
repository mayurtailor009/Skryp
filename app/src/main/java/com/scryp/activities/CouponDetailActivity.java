package com.scryp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scryp.R;
import com.scryp.dto.CouponDTO;
import com.scryp.fragments.CouponDetailFragment;
import com.scryp.fragments.WebViewFragment;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;
import com.scryp.utility.WebServiceCaller;
import com.scryp.utility.WebServiceListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CouponDetailActivity extends BaseActivity implements WebServiceListener{

    private CouponDTO couponDTO;
    private CouponDTO couponDetail;
    private DisplayImageOptions options;
    private String latitude, longitude;
    ImageView ivThumb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);

        init();



        getCouponDetails();
    }


    private void init(){
        setHeader("");
        setLeft(R.drawable.back);
        setRight(R.drawable.iconshare);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();

            latitude = bundle.getString("latitude");
            longitude = bundle.getString("longitude");

            couponDetail = (CouponDTO) bundle.getSerializable("couponDTO");
            //setViewEnable(R.id.btn_download, bundle.getBoolean("toDownload"));
            Button btnDownload = (Button) findViewById(R.id.btn_download);
            btnDownload.setOnClickListener(this);
            if(bundle.getBoolean("toDownload")){
                btnDownload.setText("DOWNLOAD COUPON");
            }else{
                btnDownload.setText("VIEW COUPON");
            }

            boolean isUsed = bundle.getBoolean("isUsed");
            if(isUsed){
                //btnDownload.setBackgroundColor(getResources().getColor(R.color.gray));
                btnDownload.setOnClickListener(null);
            }

            ivThumb = (ImageView) findViewById(R.id.iv_coupon);
            ivThumb.setDrawingCacheEnabled(true);
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            //int width = metrics.widthPixels;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, metrics.widthPixels-40
            );
            ivThumb.setLayoutParams(layoutParams);
            options = new DisplayImageOptions.Builder()
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
            ImageLoader.getInstance().displayImage(couponDetail.getThumb_url(), ivThumb,
                    options);

           /* ImageLoader.getInstance().loadImage(couponDetail.getThumb_url(), options,
                    new SimpleImageLoadingListener(){
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
                            // Do whatever you want with Bitmap
                            ivThumb.setImageBitmap(loadedImage);
                        }
                    });*/
        }
    }

    public void getCouponDetails(){

        if(Utils.isOnline(this)){
            try {
                JSONObject json = new JSONObject();
                    json.put("methodName", Constant.GET_COUPON_DETAILS_METHOD);
                    json.put("coupon_id", couponDetail.getId());
                    json.put("lat", latitude);
                    json.put("long", longitude);
                new WebServiceCaller(CouponDetailActivity.this, this, Constant.SERVICE_BASE_URL+"?"
                        +"jsonRequest="+json.toString(),
                        null, 1, true).execute();
            }catch (Exception e){
                e.printStackTrace();;
            }
        }else{
            Utils.showNoNetworkDialog(this);
        }
    }

    public void setupTab(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CouponDetailPagerAdapter(getSupportFragmentManager(), this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.btn_download:
                if(Utils.getObjectFromPref(this, Constant.USER_INFO)!=null) {
                    Intent i = new Intent(this, DownloadCouponActivity.class);
                    i.putExtra("couponId", couponDetail.getId());
                    startActivity(i);
                }
                else
                    startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                Uri bmpUri = getLocalBitmapUri(ivThumb);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, couponDetail.getTitle());
                sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                sendIntent.setType("image/*");
                startActivity(Intent.createChooser(sendIntent, "Share"));
                break;
        }
        super.onClick(arg0);
    }

    public void onTaskComplete(String result, int requestCode, int resultCode) {

        if(resultCode==Constant.RESULT_OK){
            if(requestCode==1){
                if(Utils.getWebServiceStatus(result,Constant.GET_COUPON_DETAILS_METHOD)){
                    couponDTO = new Gson().fromJson(Utils.getResponseInObject1(result, Constant.GET_COUPON_DETAILS_METHOD), CouponDTO.class);
                    setupTab();
                }else{
                    Utils.showDialog(CouponDetailActivity.this,  "Message", Utils.getWebServiceMessage(result,Constant.GET_COUPON_DETAILS_METHOD));
                }
            }
        }else{
            Utils.showExceptionDialog(this);
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = imageView.getDrawingCache();
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image.png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    class CouponDetailPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 4;
        private String tabTitles[] = new String[]{"Coupon", "Highlights", "Details", "Fine Print"};
        private Context context;

        public CouponDetailPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            switch (position){
                case 0:
                    fragment = new CouponDetailFragment();
                    bundle.putSerializable("couponDTO", couponDetail);

                    break;
                case 1:
                    fragment = new WebViewFragment();
                    bundle.putInt("type", position);
                    bundle.putSerializable("couponDTO", couponDTO);
                    break;
                case 2:
                    bundle.putInt("type", position);
                    bundle.putSerializable("couponDTO", couponDTO);
                    fragment = new WebViewFragment();
                    break;
                case 3:
                    bundle.putInt("type", position);
                    bundle.putSerializable("couponDTO", couponDTO);
                    fragment = new WebViewFragment();
                    break;
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
