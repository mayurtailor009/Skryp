package com.scryp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.scryp.R;
import com.scryp.activities.CouponDetailActivity;
import com.scryp.activities.DownloadCouponActivity;
import com.scryp.activities.LoginActivity;
import com.scryp.dto.CategoryDTO;
import com.scryp.dto.CouponDTO;
import com.scryp.dto.FilterDTO;
import com.scryp.dto.UserDTO;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;
import com.scryp.utility.WebServiceCaller;
import com.scryp.utility.WebServiceListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Mayur on 03-11-2015.
 */
public class CouponListFragment extends BaseFragment implements WebServiceListener,
        SwipeRefreshLayout.OnRefreshListener{
    View view;
    private ListView lvCoupon;
    public CouponAdapter couponAdapter;
    private ArrayList<CouponDTO> couponList = new ArrayList<CouponDTO>();
    private CategoryDTO categoryDTO;
    private String latitude, longitude;
    private DisplayImageOptions options;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FilterDTO filterDTO;
    private AsyncTask task;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_coupon_list, container, false);

        init();

        /*if(couponList.size()==0)
        getCouponList();
        else
        setCouponList();*/



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getCouponList();
    }

    public void setCouponList(){
        setViewVisibility(R.id.progressBar, view, View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        if(couponList!=null && couponList.size()>0) {
            couponAdapter = new CouponAdapter(getActivity(), couponList);
            lvCoupon.setAdapter(couponAdapter);

            lvCoupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("couponDTO", couponList.get(i));
                    bundle.putString("latitude", latitude);
                    bundle.putString("longitude", longitude);
                    if(couponList.get(i).getIs_coupon_downloaded_by_user().equals("y"))
                        bundle.putBoolean("toDownload", false);
                    else
                        bundle.putBoolean("toDownload", true);
                    bundle.putBoolean("isUsed", false);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });
        }else{
            lvCoupon.setVisibility(View.GONE);
            setViewVisibility(R.id.tv_norecord, view, View.VISIBLE);
        }
    }
    private void init(){
        lvCoupon = (ListView) view.findViewById(R.id.lv_coupons);
        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);
        if(getArguments()!=null){
            categoryDTO = (CategoryDTO) getArguments().getSerializable("category");
            latitude = getArguments().getString("latitude");
            longitude = getArguments().getString("longitude");
        }
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    public void doSearch(String search){
        if(couponAdapter!=null)
            couponAdapter.filter(search);
    }
    @Override
    public void onTaskComplete(String result, int requestCode, int resultCode) {
        if(resultCode==Constant.RESULT_OK){
            if(requestCode==1){
                if(Utils.getWebServiceStatus(result,Constant.GET_COUPON_METHOD)){
                    Type listType = new TypeToken<ArrayList<CouponDTO>>() {}.getType();
                    couponList = new Gson().fromJson(Utils.getResponseInArray(result, Constant.GET_COUPON_METHOD), listType);
                    setCouponList();
                }else{
                    setViewVisibility(R.id.progressBar, view, View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    Utils.showDialog(getActivity(), "Message", Utils.getWebServiceMessage(result,Constant.GET_COUPON_METHOD));
                }
            }
        }else{
            setViewVisibility(R.id.progressBar, view, View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            Utils.showExceptionDialog(getActivity());
        }
    }

    public void refreshList(FilterDTO filterDTO){
        this.filterDTO = filterDTO;
        getCouponList();
    }

    @Override
    public void onRefresh() {

        getCouponList();
    }

    public class CouponAdapter extends BaseAdapter {
        private LayoutInflater inflator;
        private Context context;
        private ArrayList<CouponDTO> localList;
        DisplayMetrics metrics;
        public CouponAdapter(Context context, ArrayList<CouponDTO> list) {
            this.context = context;
            if(context!=null) {
                inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                localList = new ArrayList<CouponDTO>();
                localList.addAll(list);
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                metrics = new DisplayMetrics();
                display.getMetrics(metrics);
            }
        }

        @Override
        public int getCount() {
            return localList.size();
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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            final ViewHolderItem viewHolder;
            final int position = i;
            if(convertView==null){

                // inflate the layout
                convertView = inflator.inflate(R.layout.item_coupon, viewGroup, false);
                viewHolder = new ViewHolderItem();
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tvValue = (TextView) convertView.findViewById(R.id.tv_value);
                viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
                viewHolder.tvDownload = (TextView) convertView.findViewById(R.id.tv_download);
                viewHolder.ivThumb = (ImageView) convertView.findViewById(R.id.iv_coupon);
                viewHolder.ivDownload = (ImageView) convertView.findViewById(R.id.iv_download);
                // store the holder with the view.
                convertView.setTag(viewHolder);

            }else{
                // we've just avoided calling findViewById() on resource everytime
                // just use the viewHolder
                viewHolder = (ViewHolderItem) convertView.getTag();
            }



            // object item based on the position
            final CouponDTO dto = localList.get(i);

            // assign values if the object is not null
            if(dto != null) {
                viewHolder.tvTitle.setText(Html.fromHtml(dto.getTitle()));
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, metrics.widthPixels-40
                );
                viewHolder.ivThumb.setLayoutParams(layoutParams);
                ImageLoader.getInstance().displayImage(dto.getThumb_url(), viewHolder.ivThumb,
                        options);

                viewHolder.tvValue.setText("$" + Utils.get2DecimalValue(dto.getValue()));
                viewHolder.tvValue.setPaintFlags(viewHolder.tvValue.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.tvPrice.setText("$"+Utils.get2DecimalValue(dto.getPrice()));
                viewHolder.tvDownload.setText(dto.getDownload() + " ");

                viewHolder.tvDistance.setText(Utils.get2DecimalValue(dto.getDist_min())+" Km");
                viewHolder.ivDownload.setTag(i);
                final ImageView ivDownload = viewHolder.ivDownload;
                if(dto.getIs_coupon_downloaded_by_user()!=null &&
                        dto.getIs_coupon_downloaded_by_user().equals("y")){
                    viewHolder.ivDownload.setImageResource(R.drawable.view_btn);
                }else{
                    viewHolder.ivDownload.setImageResource(R.drawable.download);
                }

                viewHolder.ivDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.hideKeyboard(getActivity());
                        int pos = (int) view.getTag();
                        UserDTO userDTO = Utils.getObjectFromPref(getActivity(), Constant.USER_INFO);
                        if (userDTO != null) {

                            if(dto.getIs_coupon_downloaded_by_user()!=null &&
                                    dto.getIs_coupon_downloaded_by_user().equals("y")){
                                Intent intent = new Intent(getActivity(), DownloadCouponActivity.class);
                                intent.putExtra("couponId", localList.get(pos).getId());
                                startActivity(intent);
                            }else{
                                String url = "http://scryp.sg/ajaxHandeler.php?user_id="+userDTO.getID()+"&coupon_id="+dto.getId()+"&func=insertVoucherMobile";

                                dto.setIs_coupon_downloaded_by_user("y");
                                ivDownload.setImageResource(R.drawable.view_btn);
                            }

                        } else
                            startActivity(new Intent(getActivity(), LoginActivity.class));


                        /*if(dto.getIs_coupon_downloaded_by_user()!=null &&
                                dto.getIs_coupon_downloaded_by_user().equals("y")){
                            Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("couponDTO", couponList.get(position));
                            bundle.putBoolean("toDownload", false);
                            bundle.putBoolean("isUsed", false);
                            bundle.putString("latitude", latitude);
                            bundle.putString("longitude", longitude);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        }else {
                            UserDTO userDTO = Utils.getObjectFromPref(getActivity(), Constant.USER_INFO);
                            if (userDTO != null) {
                                Intent intent = new Intent(getActivity(), DownloadCouponActivity.class);
                                intent.putExtra("couponId", localList.get(pos).getId());
                                startActivity(intent);
                            } else
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                        }*/
                    }
                });

            }

            return convertView;
        }

        public void filter(String search){

            localList.clear();
            if (search.length() == 0) {
                localList.addAll(couponList);
            } else {
                for (CouponDTO wp : couponList) {
                    if (wp.getTitle().toLowerCase(Locale.getDefault())
                            .contains(search)) {
                        localList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }
    static class ViewHolderItem {
        TextView tvPrice;
        TextView tvValue;
        TextView tvDistance;
        TextView tvDownload;
        TextView tvTitle;
        ImageView ivThumb;
        ImageView ivDownload;
    }
    public void getCouponList(){
        if(Utils.isOnline(getActivity())){
            swipeRefreshLayout.setRefreshing(true);
            if(couponList.size()==0)
            setViewVisibility(R.id.progressBar, view, View.VISIBLE);
            setViewVisibility(R.id.tv_norecord, view, View.GONE);
            lvCoupon.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<String, String>();

            try {
                JSONObject json = new JSONObject();
                UserDTO userDTO = Utils.getObjectFromPref(getActivity(), Constant.USER_INFO);
                if(filterDTO==null) {
                    json.put("methodName", Constant.GET_COUPON_METHOD);
                    if(categoryDTO.getName().equalsIgnoreCase("nearby")){
                        json.put("categories", "");
                        json.put("filterRadius", "radius");
                        json.put("distance", "1.0");
                        json.put("sortvar","dist_min");
                        json.put("asc","0");
                    }else {
                        json.put("categories", categoryDTO.getTerm_id());
                    }
                    if(userDTO!=null)
                        json.put("user_id", userDTO.getID());
                    json.put("lat", latitude);
                    json.put("long", longitude);
                }else{
                    json = getFilterJsonRequest(filterDTO);
                }

                params.put("jsonRequest", json.toString());
               task = new WebServiceCaller(getActivity(), CouponListFragment.this, Constant.SERVICE_BASE_URL,
                        params, 1, false);
                task.execute();
            }catch (Exception e){
                e.printStackTrace();;
                swipeRefreshLayout.setRefreshing(false);
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

    public JSONObject getFilterJsonRequest(FilterDTO filterDTO){
        try{
            JSONObject json = new JSONObject();
            UserDTO userDTO = Utils.getObjectFromPref(getActivity(), Constant.USER_INFO);
            if(userDTO!=null)
                json.put("user_id", userDTO.getID());
            json.put("methodName", Constant.GET_COUPON_METHOD);
            json.put("categories", categoryDTO.getTerm_id());
            json.put("lat", filterDTO.getLat());
            json.put("long", filterDTO.getLng());
            if(!filterDTO.getPriceMin().equals("") || !filterDTO.getPriceMax().equals("")){
                json.put("filterPrice", filterDTO.getFilterPrice());
                json.put("priceMax", filterDTO.getPriceMax());
                json.put("priceMin", filterDTO.getPriceMin());
            }
            if(!filterDTO.getDiscountMin().equals("") || !filterDTO.getDiscountMax().equals("")){
                json.put("filterDiscount", filterDTO.getFilterDiscount());
                json.put("discountMax", filterDTO.getDiscountMax());
                json.put("discountMin", filterDTO.getDiscountMin());
            }
            if(!filterDTO.getDistance().equals("")){
                json.put("filterRadius", filterDTO.getFilterRadius());
                json.put("distance", filterDTO.getDistance());
            }
            json.put("sortvar",filterDTO.getSortvar());
            json.put("asc",filterDTO.getAsc());
            return json;
        }catch (Exception e){
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public long getLat(){

        return 0;
    }
}
