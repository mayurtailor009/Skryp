package com.scryp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scryp.R;
import com.scryp.dto.CategoryDTO;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;
import com.scryp.utility.WebServiceCaller;
import com.scryp.utility.WebServiceListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements WebServiceListener{

    View view;
    public ArrayList<CategoryDTO> categoryList;
    public HashMap<Integer, CouponListFragment> fragmentList = new HashMap<Integer, CouponListFragment>();
    public ViewPager viewPager;
    private String latitude, longitude;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        if(getArguments()!=null){
            latitude = getArguments().getString("latitude");
            longitude = getArguments().getString("longitude");
        }

        getCategories();
        return view;
    }

    public void getCategories(){
        if(Utils.isOnline(getActivity())){
            try {
                JSONObject json = new JSONObject();
                json.put("methodName", Constant.GET_CATEGORY_METHOD);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("jsonRequest", json.toString());
                new WebServiceCaller(getActivity(), this, Constant.SERVICE_BASE_URL,
                        params, 1).execute();
            }catch (Exception e){
                e.printStackTrace();;
            }
        }else{
            Utils.showNoNetworkDialog(getActivity());
        }
    }

    public void setupTab(){
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new HomeFragmentPagerAdapter(getActivity().getSupportFragmentManager(),getActivity(), categoryList));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onTaskComplete(String result, int requestCode, int resultCode) {
        if(resultCode==Constant.RESULT_OK){
            if(requestCode==1){
                if(Utils.getWebServiceStatus(result,Constant.GET_CATEGORY_METHOD)){
                    Type listType = new TypeToken<ArrayList<CategoryDTO>>() {}.getType();
                    categoryList = new Gson().fromJson(Utils.getResponseInArray(result, Constant.GET_CATEGORY_METHOD), listType);
                    setupTab();
                }else{
                    Utils.showDialog(getActivity(), "Message", Utils.getWebServiceMessage(result,Constant.GET_CATEGORY_METHOD));
                }
            }
        }else{
            Utils.showExceptionDialog(getActivity());
        }
    }

    class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<CategoryDTO> categoryList;
        private Context context;

        public HomeFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<CategoryDTO> categoryList) {
            super(fm);
            this.context = context;
            this.categoryList = categoryList;
        }

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public Fragment getItem(int position) {
            CouponListFragment fragment = null;
            if(fragmentList.get(position)==null) {
                fragment = new CouponListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", categoryList.get(position));
                bundle.putString("latitude", latitude);
                bundle.putString("longitude", longitude);
                fragment.setArguments(bundle);
                fragmentList.put(position, fragment);
            }else
                fragment = fragmentList.get(position);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return categoryList.get(position).getName();
        }
    }
}
