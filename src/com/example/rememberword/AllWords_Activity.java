package com.example.rememberword;

import java.util.ArrayList;
import java.util.HashMap;


import com.firstpeople.wordlearn.util.Config;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AllWords_Activity extends Activity implements OnTouchListener,
OnGestureListener {
	
	SharedPreferences sp;
	
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	
	boolean isFirstMove = true, isHorizontalMove = true;
	
	GestureDetector mGestureDetector;
	
	TextView word,phonogram,paraphrase,powerWeight,pageTextView;
	
	//随机数产生器，还能获得之前几次产生的随机数
	//MyRandom myRandom;
	
	
	private ArrayList<HashMap<String, Object>> thisWordList;	
	private HashMap<String, Object> currentWord;
	
	int lessonNoo = 0;
	int basePage = 0;
	private int eachLessonWordCount = 0;
	int index = 0;
	private int powerWeightCount = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showword);
		
		Intent mIntent = getIntent();
		int lessonNo = mIntent.getExtras().getInt("lessonNo");
		
		thisWordList = Config.init().getWordsFromFileByLessonNo(getIntent().getExtras().getInt("lessonNo"));
		
		eachLessonWordCount = Integer.parseInt(Config.init().getEachLessonWordCount());		
		basePage = lessonNo*eachLessonWordCount;
		
		
		mGestureDetector = new GestureDetector(this);
		RelativeLayout rl=(RelativeLayout)findViewById(R.id.rl);
		rl.setOnTouchListener(this);
		rl.setLongClickable(true);
		
		word = (TextView) findViewById(R.id.word);
		phonogram = (TextView) findViewById(R.id.phonogram);
		paraphrase = (TextView) findViewById(R.id.paraphrase);
		powerWeight = (TextView) findViewById(R.id.powerWeight);
		pageTextView = (TextView) findViewById(R.id.Page);
		
		( (TextView)findViewById(R.id.info)).setText("第"+(lessonNo+1)+"章，共"+thisWordList.size()+"个单词");
		
		//设置音标的显示模式
		Typeface font = Typeface.createFromAsset(getAssets(), Config.FONT_KINGSOFT_PATH);
		phonogram.setTypeface(font);
		
		findViewById(R.id.knowWell).setVisibility(View.INVISIBLE);
		
		
		
		sp = this.getSharedPreferences(Config.init().getCurrentUseDictName(), Context.MODE_PRIVATE);
		
		
		currentWord = thisWordList.get(index);
		
		
		word.setText( currentWord.get("单词").toString());
		phonogram.setText(currentWord.get("音标").toString());
		paraphrase.setText("");
		
		showBanner();
		
		
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		paraphrase.setText(currentWord.get("解释").toString());
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		
		if(isHorizontalMove) {
			if (e1.getX()-e2.getX() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling left 	            
				index = (index+1)%thisWordList.size();
				currentWord = thisWordList.get(index);
	            word.setText( currentWord.get("单词").toString());
	    		phonogram.setText(currentWord.get("音标").toString() );
	    		
	    		
	    		//判断是不是生词，是生词的话加红标显示
	    		showBanner();	    		
	    		
	            
	        } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling right 
	        	
        		currentWord = thisWordList.get((--index)<0?index=(thisWordList.size()-1):index);
        		word.setText( currentWord.get("单词").toString());
        		phonogram.setText(currentWord.get("音标").toString() );        		
        		
        		showBanner();	
	        	
	        }
		}
		else {
			if (e1.getY()-e2.getY() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityY) > FLING_MIN_VELOCITY) { 
	            Log.e("onFling------------------->", "向上滑动,移出生词本"+(basePage+index));
	            	        	
	        	Editor editor = sp.edit();
	    		//editor.putInt(""+(LessonNo*eachLessonWordCount+index), 1);
	        	editor.remove(""+(basePage+index) );
	    		editor.commit();
	    		
	    		showBanner();
	            
	        } else if (e2.getY()-e1.getY() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityY) > FLING_MIN_VELOCITY) { 
	        	Log.e("onFling------------------->", "向下滑动,加入生词本"+(basePage+index));
	        	
	        	Editor editor = sp.edit();
	    		if( sp.getInt(""+(basePage+index), -1) == -1) {	    			
	    			editor.putInt(""+(basePage+index),1);
	    		}
	    		//Log.e("获得的数为",""+sp.getInt(""+(basePage+index), -1));
	        		
	    		editor.commit();
	    		
	    		showBanner();
	        	
	        }
		}
		
		 
		
		isFirstMove = true;
		
		
		
        return false; 
	}
	
	
	public void showBanner() {
		pageTextView.setText("Page "+(basePage+index));
		
		int power = sp.getInt(""+(basePage+index), -1);
		
		if( power != -1) {	    			
			pageTextView.setBackgroundColor(Color.RED);
			powerWeight.setText(""+power);
		} else {
			pageTextView.setBackgroundColor(Color.GREEN);
			powerWeight.setText("");			
		}
		//判断是不是生词，是生词的话加红标显示
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		
		float deltaX = e2.getX() - e1.getX();
		float deltaY = e2.getY() - e1.getY();
		
		
		if(isFirstMove) {
			if( Math.abs(deltaX) > 50 || Math.abs(deltaY) > 50) {
				isHorizontalMove = Math.abs(deltaX)>Math.abs(deltaY)?true:false;
				isFirstMove = false;
			} else {
				return false;
			}			
		}
		
		if(isHorizontalMove) {			
			setPos(word, (int)distanceX, 0);
			setPos(paraphrase, (int)distanceX, 0);
			setPos(phonogram, (int)distanceX, 0);
		}
		else {
			setPos(word, 0,(int)distanceY);
			setPos(paraphrase, 0,(int)distanceY);
			setPos(phonogram, 0,(int)distanceY);
		}
		
		return true;
	}
	
	public void setPos(TextView tv,int distanceX,int distanceY) {
		/*RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(tv.getHeight(),tv.getWidth());
		Log.e("left-------->",tv.getLeft()+"");
		Log.e("right-------->",tv.getRight()+"");
		Log.e("top-------->",tv.getTop()+"");
		Log.e("bottom-------->",tv.getBottom()+"");
		lp.setMargins(word.getLeft()+(int)distanceX, word.getTop()+distanceY, word.getRight()+(int)distanceX, word.getBottom()+distanceY);			
		tv.setLayoutParams(lp);*/
		
		tv.layout(tv.getLeft()-distanceX, tv.getTop()-distanceY, tv.getRight()-distanceX, tv.getBottom()-distanceY);
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_UP) {
			paraphrase.setText("");
		}		
		return mGestureDetector.onTouchEvent(event); 
	}

}
