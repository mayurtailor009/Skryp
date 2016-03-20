package com.scryp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.scryp.R;
import com.scryp.dto.CouponDTO;
import com.scryp.utility.Constant;

/**
 * Created by Mayur on 29-11-2015.
 */
public class WebViewFragment extends BaseFragment implements OnMapReadyCallback {
    View view;
    CouponDTO couponDTO;
    int type;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_webview, container, false);

        init();

        return view;
    }

    private void init(){
        if(getArguments()!=null){
            WebView wb = (WebView) view.findViewById(R.id.wb);
            type = getArguments().getInt("type");
            couponDTO = (CouponDTO) getArguments().getSerializable("couponDTO");

            StringBuffer webContent=new StringBuffer();
            String css = "<link rel=\\\"stylesheet\\\"  href=\\\" "+ Constant.APP_URL+"wp-content/themes/WPGroupbuy/style/styles.css\\\" />";
            if(type==1){    // FOR HIGHLIGHTS
                webContent.append(css).append(couponDTO.getJquery())
                        .append(couponDTO.getHighlights());
            }
            else if(type==2){    // FOR DETAILS
                webContent.append("<h2>Where to use voucher?</h2>")
                .append(css).append(couponDTO.getJquery()).append(couponDTO.getPost_content())
                .append(couponDTO.getCoupon_location());

                FrameLayout mapLayout = (FrameLayout) view.findViewById(R.id.frame_map);
                mapLayout.setVisibility(View.VISIBLE);
                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                getChildFragmentManager().beginTransaction().add(R.id.frame_map, mapFragment).commit();
                mapFragment.getMapAsync(this);

            }
            else if(type==3){    // FOR FINE PRINT
                webContent.append(css).append(couponDTO.getJquery())
                        .append(couponDTO.getFine_print());
            }
            wb.loadData(webContent.toString(), "text/html", "UTF-8");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(couponDTO!=null && couponDTO.getArr_lat()!=null && couponDTO.getArr_lat().length>0) {
            LatLng loc = new LatLng(Double.parseDouble(couponDTO.getArr_lat()[0]),Double.parseDouble(couponDTO.getArr_long()[0]));
            googleMap.addMarker(new MarkerOptions().position(loc).title(couponDTO.getArr_loc()[0]));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,
                    15));
        }
    }
}
