package com.example.rememberword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.firstpeople.wordlearn.util.Config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class testGesture extends Activity implements OnTouchListener,
		OnGestureListener {
	
	private ArrayList<HashMap<String, Object>> thisWordList;
	
	private HashMap<String, Object> currentWord;
	
	
	TextView danci,yinbiao,jieshi;
	int eachLessonWordCount;
	
	GestureDetector mGestureDetector;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	/** Called when the activity is first created. */
	@SuppressLint("LongLogTag")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
		mGestureDetector = new GestureDetector(this);
		LinearLayout ll=(LinearLayout)findViewById(R.id.ll);
		ll.setOnTouchListener(this);
		ll.setLongClickable(true);
		
		//这里初始化
		String count = Config.init().getCurrentUseDictWordCount();
		String lessonCount = Config.init().getLessonCount();
		//String EachLessonWordCount = Config.init().getEachLessonWordCount();
		eachLessonWordCount = Integer.parseInt(Config.init().getEachLessonWordCount());
		
		Log.e("The dict  count is -------->",count);
		Log.e("The lesson count is -------->",lessonCount);
		Log.e("The EachLessonWordCount count is -------->",""+eachLessonWordCount);
		
		thisWordList = Config.init().getWordsFromFileByLessonNo(10);
		
		int i = (int) (Math.random()*(eachLessonWordCount-1));
		
		currentWord = thisWordList.get(i);
		
		danci = (TextView) findViewById(R.id.danci);
		yinbiao = (TextView) findViewById(R.id.yinbiao);
		jieshi = (TextView) findViewById(R.id.jieshi);
		
		Typeface font = Typeface.createFromAsset(getAssets(), Config.FONT_KINGSOFT_PATH);
		//Typeface font = Typeface.createFromAsset(getAssets(), Config.FONT_segeo_PATH);
		yinbiao.setTypeface(font);
		
		
		
		danci.setText( currentWord.get("单词").toString());
		yinbiao.setText(currentWord.get("音标").toString());
		//jieshi.setText( thisWordList.get(i).get("解释").toString() );
		

		
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		//Log.i("touch","touch");
		
		if(event.getAction() == MotionEvent.ACTION_UP)
			jieshi.setText("");
		
		 return mGestureDetector.onTouchEvent(event); 
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("down------------>","down");
		jieshi.setText(currentWord.get("解释").toString());
		
		return false;
	}
	
	
	
	
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		 if (e1.getX()-e2.getX() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling left 
	            Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show(); 
	            
	            int i = (int) (Math.random()*(eachLessonWordCount-1));
	            currentWord = thisWordList.get(i);
	            danci.setText( currentWord.get("单词").toString());
	    		yinbiao.setText(currentWord.get("音标").toString() );		
	    		
	    		
	            
	            
	        } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling right 
	            Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show(); 
	        } 
	        return false; 
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.e("up------------>","轻触弹起");
		
		return false;
	}
}
