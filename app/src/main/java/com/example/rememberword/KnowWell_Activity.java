package com.example.rememberword;

import java.util.ArrayList;
import java.util.HashMap;


import com.firstpeople.wordlearn.util.Config;

import android.R.bool;
import android.R.integer;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KnowWell_Activity extends Activity implements OnTouchListener,
OnGestureListener {
	
	int lessonNo, strategyNo, disMode;
	
	int basePage, index;
	
	SharedPreferences sp;
	
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	
	boolean isFirstMove = true, isHorizontalMove = true;
	
	GestureDetector mGestureDetector;
	
	TextView word,phonogram,paraphrase,powerWeight,pageTextView;
	Button knowWellButton ;
	
	//随机数产生器，还能获得之前几次产生的随机数
	MyRandom myRandom;
	
	private ArrayList<HashMap<String, Object>> thisWordList;
	
	
	
	private ArrayList< int[] > knowWellArrayList;
	
	private HashMap<String, Object> currentWord;
	private int eachLessonWordCount;
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showword);
		
		initVariable();
		setupViews();
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		if( disMode == 0)
			paraphrase.setText(currentWord.get("解释").toString());
		else 
			word.setText( currentWord.get("单词").toString());
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
				showNext();
	        } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling right 
	        	showPrev();
	        }
		}
		else {
			if (e1.getY()-e2.getY() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityY) > FLING_MIN_VELOCITY) { 	            
	            outOfWordsNote();
	        } else if (e2.getY()-e1.getY() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityY) > FLING_MIN_VELOCITY) {	        	
	        	inToWordsNote();	        	
	        }
		}
		
		 
		
		isFirstMove = true;
		
		
		
        return false; 
	}
	
	
	
	
	public void updateAll() {
		word.setText( currentWord.get("单词").toString());
		phonogram.setText(currentWord.get("音标").toString());
		paraphrase.setText("");
		
		showBanner();
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
			if( disMode == 0) 
				paraphrase.setText("");
			else 
				word.setText("");
		}		
		return mGestureDetector.onTouchEvent(event); 
	}
	
	
	/*********************************************************************************/
	public void showNext() {
		index = myRandom.getNext();
		currentWord = thisWordList.get(  knowWellArrayList.get(index)[0] );
		updateViews();
	}
	
	public void showPrev() {
		int i;
		if( (i = myRandom.getPrev()) != -1 ) {
			index = i;
			currentWord = thisWordList.get(  knowWellArrayList.get(index)[0] );
			updateViews();
		}else {
			Toast.makeText(this, "已到最头", Toast.LENGTH_SHORT).show(); 
		}
	}
	
	public void inToWordsNote() {
		knowWellArrayList.get(index)[1] = 1;
		showBanner();
	}
	
	public void outOfWordsNote() {
		Log.e("onFling------------------->", "向上滑动,移出生词本");
		knowWellArrayList.get(index)[1] = 0;
		showBanner();
	}
	
	public void showBanner() {
		pageTextView.setText("Page "+(basePage+knowWellArrayList.get(index)[0]));
		
		if( knowWellArrayList.get(index)[1] == 1) {
			pageTextView.setBackgroundColor(Color.RED);
		}else {
			pageTextView.setBackgroundColor(Color.GREEN);
		}		
		//判断是不是生词，是生词的话加红标显示
	}
	
	public void initVariable() {		
		sp = this.getSharedPreferences(Config.init().getCurrentUseDictName(), Context.MODE_PRIVATE);
		
		Intent mIntent = getIntent();
		if( mIntent.getData() != null) {
			lessonNo = mIntent.getExtras().getInt("lessonNo");
			/*  */
			/*  */
		}else {
			lessonNo = 13;
		}
		
		
		basePage = lessonNo*Integer.parseInt(Config.init().getEachLessonWordCount());
		
		
		
		thisWordList = Config.init().getWordsFromFileByLessonNo(lessonNo);	
		knowWellArrayList = getKnowWellList();
		
		myRandom = new MyRandom(5, knowWellArrayList.size());
		
		
	}
	
	public void setupViews() {
		mGestureDetector = new GestureDetector(this);
		RelativeLayout rl=(RelativeLayout)findViewById(R.id.rl);
		rl.setOnTouchListener(this);
		rl.setLongClickable(true);
		
		word = (TextView) findViewById(R.id.word);
		phonogram = (TextView) findViewById(R.id.phonogram);
		paraphrase = (TextView) findViewById(R.id.paraphrase);
		powerWeight = (TextView) findViewById(R.id.powerWeight);
		pageTextView = (TextView) findViewById(R.id.Page);
		findViewById(R.id.knowWell).setVisibility(View.INVISIBLE);
		//设置音标的显示模式
		Typeface font = Typeface.createFromAsset(getAssets(), Config.FONT_KINGSOFT_PATH);
		phonogram.setTypeface(font);
		
		if(knowWellArrayList.size() > 0) {		
			( (TextView)findViewById(R.id.info)).setText("第"+(lessonNo+1)+"章，已掌握共"+knowWellArrayList.size()+"个单词");
			
			index = myRandom.getNext();
			currentWord = thisWordList.get(knowWellArrayList.get(index)[0]);
			updateViews();
		}
	}
	
	public ArrayList< int[] > getKnowWellList() {
		ArrayList< int[] > mArrayList = new ArrayList< int[] >();
		
		for (int i = 0; i < thisWordList.size(); i++) {
			int[] t = new int[2];
			if( sp.getInt(""+(basePage+i), -1) == -1) {				
				t[0] = i;
				t[1] = 0;
				mArrayList.add(t);
    		}
		}
		/*
		for (int i = 0; i < mArrayList.size(); i++) {
			Log.e("=====================================>",""+mArrayList.get(i)[1] );
		}*/
		
		return mArrayList;		
	}
	
	public void updateViews() {
		word.setText( currentWord.get("单词").toString());
		phonogram.setText(currentWord.get("音标").toString());
		paraphrase.setText("");
		showBanner();		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("onStop------------------------>","");
		
		Editor mEditor = sp.edit();
		for (int i = 0; i < knowWellArrayList.size(); i++) {
		
			if(knowWellArrayList.get(i)[1] == 1) {
				mEditor.putInt(""+(basePage+knowWellArrayList.get(i)[0] ),1 );
			}					
		}
		mEditor.commit();
	}
	
	

}
