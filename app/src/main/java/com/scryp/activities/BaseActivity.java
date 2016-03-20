package com.scryp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.scryp.R;
import com.scryp.dto.UserDTO;
import com.scryp.utility.Constant;
import com.scryp.utility.TouchEffect;
import com.scryp.utility.Utils;


public class BaseActivity extends AppCompatActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener{

	public static final TouchEffect TOUCH = new TouchEffect();
	public Toolbar toolbar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(BaseFragment.this));
	}

	public View setTouchNClick(int id) {

		View v = findViewById(id);
		v.setOnClickListener(this);
		v.setOnTouchListener(TOUCH);
		return v;
	}
	
	public View setClick(int id) {

		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}
	
	public void setViewEnable(int id, boolean flag){
		View v = findViewById(id);
		v.setEnabled(flag);
	}
	
	public void setViewVisibility(int id, int flag){
		View v = findViewById(id);
		v.setVisibility(flag);
	}
	
	public void setTextViewText( int id, String text){
		((TextView)findViewById(id)).setText(text);
	}
	
	
	public void setTextViewHtmlText( int id, String text){
		TextView tv = ((TextView)findViewById(id));
		tv.setText(Html.fromHtml(text));
	}
	
	public void setTextViewTextColor(int id, int color){
		((TextView)findViewById(id)).setTextColor(color);
	}
	
	public void setEditText( int id, String text){
		((EditText)findViewById(id)).setText(text);
	}
	
	public String getEditTextText(int id){
		return ((EditText)findViewById(id)).getText().toString();
	}
	
	public String getTextViewText(int id){
		return ((TextView)findViewById(id)).getText().toString();
	}
	
	public String getButtonText(int id){
		return ((Button)findViewById(id)).getText().toString();
	}
	
	public void setButtonText( int id, String text){
		((Button)findViewById(id)).setText(text);
	}

	public void replaceButtoImageWith(int replaceId, int drawable){
		((Button)findViewById(replaceId)).setBackgroundResource(drawable);
	}
	
	public void setButtonSelected(int id, boolean flag){
		((Button)findViewById(id)).setSelected(flag);
	}
	public boolean isButtonSelected(int id){
		return ((Button)findViewById(id)).isSelected();
	}
	
	/**
	 * Method use to set view selected
	 * @param id resource id of view.
	 * @param flag true if view want to selected else false
	 */
	public void setViewSelected(int id, boolean flag){
		View v = findViewById(id);
		v.setSelected(flag);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent i = null;
		UserDTO userDTO = Utils.getObjectFromPref(this, Constant.USER_INFO);
		switch (arg0.getId()) {
			case R.id.tv_home:
				i = new Intent(this, HomeActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				finish();
				break;
			case R.id.tv_coupons:
				if(userDTO!=null) {
					i = new Intent(this, CouponActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);
					finish();
				}else{
					i  = new Intent(this, LoginActivity.class);
					startActivity(i);
				}

				break;
			case R.id.tv_profile:
				if(userDTO!=null) {
					i = new Intent(this, ProfileActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);
					finish();
				}
				else{
					i  = new Intent(this, LoginActivity.class);
					startActivity(i);
				}

				break;
		}
	}
	
	/**
	 * Method use to set text on TextView, EditText, Button.
	 * @param id resource of TextView, EditText, Button.
	 * @param text string you want to set on TextView, EditText, Button
	 */
	public void setViewText(int id, String text){
		View v = ((View)findViewById(id));
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			tv.setText(text);
		}
		if(v instanceof EditText){
			EditText et = (EditText)v;
			et.setText(text);
		}
		if(v instanceof Button){
			Button btn = (Button)v;
			btn.setText(text);
		}
		
	}
	
	public void setViewText(View view, int id, String text){
		View v = ((View)view.findViewById(id));
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			tv.setText(text);
		}
		if(v instanceof EditText){
			EditText et = (EditText)v;
			et.setText(text);
		}
		if(v instanceof Button){
			Button btn = (Button)v;
			btn.setText(text);
		}
		
	}
	
	/**
	 * Method use to get Text from TextView, EditText, Button.
	 * @param id resource id TextView, EditText, Button
	 * @return string text from view
	 */
	public String getViewText(int id){
		String text="";
		View v = ((View)findViewById(id));
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			text = tv.getText().toString().trim();
		}
		if(v instanceof EditText){
			EditText et = (EditText)v;
			text = et.getText().toString().trim();
		}
		if(v instanceof Button){
			Button btn = (Button)v;
			text = btn.getText().toString().trim();
		}
		return text;
	}
	
	public boolean isCheckboxChecked(int id){
		CheckBox cb = (CheckBox) findViewById(id);
		return cb.isChecked();
	}

	public void setCheckboxChecked(int id, boolean flag){
		CheckBox cb = (CheckBox) findViewById(id);
		cb.setChecked(flag);
	}

	public void setOnCheckbox(int id){
		CheckBox cb = (CheckBox) findViewById(id);
		cb.setOnCheckedChangeListener(this);
	}

	public void setImageResourseBackground(int id, int resId){
		ImageView iv = (ImageView) findViewById(id);
		iv.setImageResource(resId);
	}

	public void setLeft(int resId){
		ImageView iv = (ImageView) findViewById(R.id.iv_left);
		iv.setVisibility(View.VISIBLE);
		setClick(R.id.iv_left);
		iv.setImageResource(resId);
	}

	public void setRight(){
		ImageView iv = (ImageView) findViewById(R.id.iv_right);
		iv.setVisibility(View.VISIBLE);
		setClick(R.id.iv_right);
	}

	public void setRight(int rid){
		ImageView iv = (ImageView) findViewById(R.id.iv_right);
		iv.setVisibility(View.VISIBLE);
		setClick(R.id.iv_right);
		iv.setImageResource(rid);
	}

	public void setClose(){
		ImageView iv = (ImageView) findViewById(R.id.iv_right);
		iv.setVisibility(View.GONE);
		setViewVisibility(R.id.iv_close, View.VISIBLE);
		setClick(R.id.iv_close);
	}

	public void setHeader(String title){
		TextView tv = (TextView) findViewById(R.id.tv_header);
		tv.setText(title);
		tv.setVisibility(View.VISIBLE);
		ImageView ivLogo = (ImageView) findViewById(R.id.iv_header);
		ivLogo.setVisibility(View.GONE);
	}

	public void setFooterClick(){
		setClick(R.id.tv_home);
		setClick(R.id.tv_coupons);
		setClick(R.id.tv_profile);
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

	}

	public void setHomeSelected(){
		TextView tvHome = (TextView) findViewById(R.id.tv_home);
		tvHome.setTextColor(getResources().getColor(R.color.selected_color));
		tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabhomeselected, 0, 0);
	}

	public void setProfileSelected(){
		TextView tvHome = (TextView) findViewById(R.id.tv_profile);
		tvHome.setTextColor(getResources().getColor(R.color.selected_color));
		tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabprofileselected, 0, 0);
	}

	public void setCouponSelected(){
		TextView tvHome = (TextView) findViewById(R.id.tv_coupons);
		tvHome.setTextColor(getResources().getColor(R.color.selected_color));
		tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tabcouponsselected, 0, 0);
	}
}
