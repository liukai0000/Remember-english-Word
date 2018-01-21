package com.example.rememberword;

import java.util.ArrayList;
import java.util.HashMap;

import com.firstpeople.wordlearn.util.Config;

import android.R.bool;
import android.annotation.SuppressLint;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreen_Activity extends Activity implements OnTouchListener,
OnGestureListener {
	
	int index;	//测试用，以后删
	
	int basePage, offset;
		
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
	
	
	
	private ArrayList< int[] > wordsNoteArrayList;
	
	private HashMap<String, Object> currentWord;
	private int eachLessonWordCount;
	int lessonNo, strategyNo, disMode;
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		/*设置全屏，无标题*/
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
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

	@SuppressLint("LongLogTag")
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		
		if(isHorizontalMove) {			
			if( wordsNoteArrayList.size() > 0 ) {
			
				if (e1.getX()-e2.getX() > FLING_MIN_DISTANCE 
		                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
		            // Fling left 
					Log.e("onFling------------------->", "向左滑动");
		            showNext();
		            
		        } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE 
		                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
		            // Fling right 
		        	Log.e("onFling------------------->", "向右滑动");
		        	showPrev();
		        }
			}
		}
		else {
			if (e1.getY()-e2.getY() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityY) > FLING_MIN_VELOCITY) { 
	            Log.e("onFling------------------->", "向上滑动");
	            PowerWeightUP();
	        } else if (e2.getY()-e1.getY() > FLING_MIN_DISTANCE 
	                && Math.abs(velocityY) > FLING_MIN_VELOCITY) { 
	        	Log.e("onFling------------------->", "向下滑动");	        	
	        	PowerWeightDown();	            
	        }
			
		}
		
		isFirstMove = true;
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


	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("onStop------------------------>","");
		
		Editor mEditor = sp.edit();
		for (int i = 0; i < wordsNoteArrayList.size(); i++) {
		
			if(wordsNoteArrayList.get(i)[1] > 0) {
				mEditor.putInt(""+(basePage+wordsNoteArrayList.get(i)[0] ),wordsNoteArrayList.get(i)[1] );
			}				
			else {
				mEditor.remove(""+(basePage+wordsNoteArrayList.get(i)[0] ) );
			}
		}
		mEditor.commit();
	}
	
	
	
	/***********************************************************************/
	public void initVariable() {		
		sp = this.getSharedPreferences(Config.init().getCurrentUseDictName(), Context.MODE_PRIVATE);
		
		if( (lessonNo = sp.getInt("latestLesson", -1)) == -1) {
			lessonNo = 0;
		}
		  
		
		basePage = lessonNo*Integer.parseInt(Config.init().getEachLessonWordCount());
		
		
		
		thisWordList = Config.init().getWordsFromFileByLessonNo(lessonNo);	
		wordsNoteArrayList = getWordsNoteArrayList();
		
		myRandom = new MyRandom(5, wordsNoteArrayList);
		
		
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
		
		TextView infoTextView = (TextView) findViewById(R.id.info);
		
		//设置音标的显示模式
		Typeface font = Typeface.createFromAsset(getAssets(), Config.FONT_KINGSOFT_PATH);
		phonogram.setTypeface(font);
		
		knowWellButton = (Button) findViewById(R.id.knowWell);
		knowWellButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( ((Button)v).getText().equals("加生词") ){
					wordsNoteArrayList.get(myRandom.getCurrent())[1] = 1;
				}else {
					wordsNoteArrayList.get(myRandom.getCurrent())[1] = 0;
				}				
				showBanner();
			}
		});
		
		if(wordsNoteArrayList.size() > 0) {		
			infoTextView.setText("第"+(lessonNo+1)+"章，共"+wordsNoteArrayList.size()+"个生词");
			
			index = myRandom.getNext();
			currentWord = thisWordList.get(wordsNoteArrayList.get(index)[0]);
			updateViews();
		}else {
			rl.setOnClickListener(null);			
			word.setVisibility(View.INVISIBLE);
			phonogram.setText("当前没有生词,请先在全文模式下添加生词");
			paraphrase.setVisibility(View.INVISIBLE);
			infoTextView.setVisibility(View.INVISIBLE);
			knowWellButton.setVisibility(View.INVISIBLE);
			powerWeight.setVisibility(View.INVISIBLE);
			pageTextView.setVisibility(View.INVISIBLE);
		}
	}
	public void showNext() {
		index = myRandom.getNext();
		currentWord = thisWordList.get(  wordsNoteArrayList.get(index)[0] );
		updateViews();
	}
	
	public void showPrev() {
		int i;
		if( (i = myRandom.getPrev()) != -1 ) {
			index = i;
			currentWord = thisWordList.get(  wordsNoteArrayList.get(index)[0] );
			updateViews();
		}else {
			Toast.makeText(this, "已到最头", Toast.LENGTH_SHORT).show(); 
		}
	}
	
	public void  PowerWeightUP() {
		wordsNoteArrayList.get(index)[1]++;
		updateViews();
	}
	
	public void  PowerWeightDown() {
		if(wordsNoteArrayList.get(index)[1] > 1) {
			wordsNoteArrayList.get(index)[1]--;
		}
		updateViews();
	}
	
	public  ArrayList< int[] > getWordsNoteArrayList() {
		 ArrayList< int[] > wordsNoteArrayList = new ArrayList< int[] >();
			
			
					
			for (int i = 0; i < thisWordList.size(); i++) {
				int[] t = new int[2];
				if( (t[1] = sp.getInt(""+(basePage+i), -1) ) != -1) {				
					t[0] = i;
					wordsNoteArrayList.add(t);
	    		}
			}
						
			return wordsNoteArrayList;
	}
	
	public void updateViews() {
		word.setText( currentWord.get("单词").toString());
		phonogram.setText(currentWord.get("音标").toString());
		paraphrase.setText("");
		showBanner();		
	}
	
	public void showBanner() {
		pageTextView.setText("Page "+(basePage+wordsNoteArrayList.get(index)[0]));
		
		int power = wordsNoteArrayList.get(index)[1];
		
		if( power > 0 ) { 			
			pageTextView.setBackgroundColor(Color.RED);
			powerWeight.setText(""+power);
			knowWellButton.setText("已掌握");
		} else {
			pageTextView.setBackgroundColor(Color.GREEN);
			powerWeight.setText("");		
			knowWellButton.setText("加生词");
		}
		//判断是不是生词，是生词的话加红标显示
	}

}
