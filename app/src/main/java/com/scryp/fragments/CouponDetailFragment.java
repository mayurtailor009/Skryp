package com.scryp.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scryp.R;
import com.scryp.dto.CouponDTO;
import com.scryp.utility.Utils;

public class CouponDetailFragment extends BaseFragment {

    View view;
    private CouponDTO dto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_coupon_detail, container, false);

        init();

        return view;
    }

    private void init(){
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            dto = (CouponDTO) bundle.getSerializable("couponDTO");

            setViewText(R.id.tv_title,dto.getTitle(), view);

            TextView tvValue = (TextView) view.findViewById(R.id.tv_value);
            tvValue.setText("$" + Utils.get2DecimalValue(dto.getValue()));
            tvValue.setPaintFlags(tvValue.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            setViewText(R.id.tv_price, "$" + Utils.get2DecimalValue(dto.getPrice()), view);
            setViewText(R.id.tv_discount, dto.getDiscount(), view);
            double diff=0;
            if(dto.getPrice()!=null && dto.getValue()!=null && !dto.getPrice().equals("") && !dto.getValue().equals("")){
                diff = Double.parseDouble(dto.getValue())-Double.parseDouble(dto.getPrice());
            }
            setViewText(R.id.tv_save, "$" + Utils.get2DecimalValue(diff+""), view);
            setViewText(R.id.tv_download, dto.getDownload() + " Downloaded", view);

            int download = Integer.parseInt(dto.getDownload());
            int max = Integer.parseInt(dto.getMax_purchases());
            int left = max-download;
            setViewText(R.id.tv_left, left + "/" + max + " left", view);
        }

    }

}
