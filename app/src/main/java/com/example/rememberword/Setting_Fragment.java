package com.example.rememberword;




import com.firstpeople.wordlearn.dictmanager.DictManagerActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Setting_Fragment extends Fragment{
	
	Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("Setting_Fragment","------------------------------------>onCreate");
		mContext = this.getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.setting, container, false);
		
		Button dickManage = (Button)view.findViewById(R.id.dickManage);
		dickManage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(mContext, DictManagerActivity.class);
				mContext.startActivity(i);
			}
		});
		
		return view;
		
	}
	
	
}
