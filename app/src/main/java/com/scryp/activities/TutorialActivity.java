package com.scryp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scryp.R;
import com.scryp.utility.Constant;
import com.scryp.utility.Utils;
import com.viewpagerindicator.CirclePageIndicator;


public class TutorialActivity extends BaseActivity {

    CheckBox cbTutorial;
    ViewPager mPager;
    LinearLayout llTuto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        init();
    }

    private void init(){
        setHeader("Tutorial");
        setClose();

        llTuto = (LinearLayout) findViewById(R.id.llTuto);
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==4){
                    llTuto.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        cbTutorial = (CheckBox) findViewById(R.id.cb_tutorial);

        ImageAdapter adapter = new ImageAdapter(this);
        mPager.setAdapter(adapter);


        CirclePageIndicator mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()){
            case R.id.iv_close:
                finish();
                Utils.putBooleanIntoPref(this, Constant.PREF_DONT_SHOW, cbTutorial.isChecked());
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }

    class ImageAdapter extends PagerAdapter {
        private Context mContext;

        private Integer[] mImageIds = { R.drawable.tutorial1, R.drawable.tutorial2,
                R.drawable.tutorial3, R.drawable.tutorial4, R.drawable.tutorial5
        };

        public ImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater inflater = (LayoutInflater) container.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View convertView = inflater.inflate(R.layout.image_item, null);

            ImageView view_image = (ImageView) convertView
                    .findViewById(R.id.iv_image);

            view_image.setImageResource(mImageIds[position]);
            view_image.setScaleType(ImageView.ScaleType.FIT_XY);

            ((ViewPager) container).addView(convertView, 0);

            return convertView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ViewGroup) object);
        }
    }
}
