package com.scryp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.scryp.R;
import com.scryp.activities.CouponDetailActivity;
import com.scryp.dto.CouponDTO;
import com.scryp.dto.UserDTO;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;
import com.scryp.utility.WebServiceCaller;
import com.scryp.utility.WebServiceListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MyCouponFragment extends BaseFragment implements WebServiceListener{

    private ListView lvCoupon;
    private MyCouponAdapter couponAdapter;
    private View view;
    private String type;
    private List<CouponDTO> couponList = new ArrayList<CouponDTO>();
    private DisplayImageOptions options;
    private AsyncTask task;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mycoupon, container, false);

        init();

        getCouponList();
        return view;
    }
    private void init(){
        lvCoupon = (ListView) view.findViewById(R.id.lv_mycoupons);
        if(getArguments()!=null){
            type = getArguments().getString("type");
        }
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }

    public void getCouponList(){
        if(Utils.isOnline(getActivity())){
            setViewVisibility(R.id.progressBar, view, View.VISIBLE);
            setViewVisibility(R.id.tv_norecord, view, View.GONE);
            lvCoupon.setVisibility(View.VISIBLE);
            try {
                UserDTO userDTO = Utils.getObjectFromPref(getActivity(), Constant.USER_INFO);
                JSONObject json = new JSONObject();
                    json.put("methodName", Constant.GET_MY_COUPON_METHOD);
                    json.put("user_id", userDTO.getID());
                    json.put("type", type);
                task = new WebServiceCaller(getActivity(), MyCouponFragment.this, Constant.SERVICE_BASE_URL+"?"
                        +"jsonRequest="+json.toString(),
                        null, 1, false);
                task.execute();
            }catch (Exception e){
                e.printStackTrace();;
            }
        }else{
            Utils.showNoNetworkDialog(getActivity());
        }
    }

    public void onStop() {
        super.onStop();

        //check the state of the task
        if(task != null && task.getStatus() == AsyncTask.Status.RUNNING)
            task.cancel(true);
    }

    public void setCouponList(){
        setViewVisibility(R.id.progressBar, view, View.GONE);
        if(couponList!=null && couponList.size()>0) {
            couponAdapter = new MyCouponAdapter(getActivity());
            lvCoupon.setAdapter(couponAdapter);

            lvCoupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("couponDTO", couponList.get(i));
                    bundle.putBoolean("toDownload", false);
                    if(type.equalsIgnoreCase("used") || type.equalsIgnoreCase("Expired")){
                        bundle.putBoolean("isUsed", true);
                    }else{
                        bundle.putBoolean("isUsed", false);
                    }
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });
        }else {
            lvCoupon.setVisibility(View.GONE);
            setViewVisibility(R.id.tv_norecord, view, View.VISIBLE);
        }
    }
    @Override
    public void onTaskComplete(String result, int requestCode, int resultCode) {
        if(resultCode==Constant.RESULT_OK){
            if(requestCode==1){
                if(Utils.getWebServiceStatus(result,Constant.GET_MY_COUPON_METHOD)){
                    Type listType = new TypeToken<ArrayList<CouponDTO>>() {}.getType();
                    couponList = new Gson().fromJson(Utils.getResponseInArray(result, Constant.GET_MY_COUPON_METHOD), listType);
                    setCouponList();
                }else{
                    Utils.showDialog(getActivity(), "Message", Utils.getWebServiceMessage(result,Constant.GET_MY_COUPON_METHOD));
                }
            }
        }else{
            Utils.showExceptionDialog(getActivity());
        }
    }

    public class MyCouponAdapter extends BaseAdapter {
        private LayoutInflater inflator;
        private Context context;

        public MyCouponAdapter(Context context) {
            this.context = context;
            if(context!=null)
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return couponList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vi = inflator.inflate(R.layout.item_mycoupon, null);

            CouponDTO dto = couponList.get(i);
            setViewText(R.id.tv_title, dto.getTitle(), vi);
            setViewText(R.id.tv_expire, dto.getExpiration(), vi);
            ImageView ivCoupon = (ImageView) vi.findViewById(R.id.iv_coupon);
            ImageLoader.getInstance().displayImage(dto.getThumb_url(), ivCoupon,
                    options);
            return vi;
        }
    }
}