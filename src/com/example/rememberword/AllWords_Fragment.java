package com.example.rememberword;

import com.firstpeople.wordlearn.util.Config;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView; 

public class AllWords_Fragment extends Fragment{
	
	private Context mContext;	
	int lessonNo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.allwords, container, false);		
		
		Spinner spinner=(Spinner)view.findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,getLessons());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				lessonNo = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub				
			}
		});
        
        Button startButton = (Button)view.findViewById(R.id.start);
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();				
				Bundle mBundle = new Bundle();
				mBundle.putInt("lessonNo", lessonNo);
				i.putExtras(mBundle);				
				i.setClass(mContext, AllWords_Activity.class);
				mContext.startActivity(i);
			}
		});
		
        
        TextView mTextView = (TextView) view.findViewById(R.id.textView1);
		mTextView.setText(Config.init().getCurrentUseDictName()+" "+
				Config.init().getCurrentUseDictWordCount()+"�� "+
				"��"+Config.init().getLessonCount()+"��,ÿ��"+
				Config.init().getEachLessonWordCount()+"��");        
		
		return view;
		
	}
	
	public String[] getLessons() {		
		int lessonCount = Integer.parseInt(Config.init().getLessonCount());
		String[] lessons = new String[lessonCount];		
		for (int i = 0; i < lessonCount; i++) {
			lessons[i] = "��"+(i+1)+"��";
		}		
		return lessons;
	}
	
}
