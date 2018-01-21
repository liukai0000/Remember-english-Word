package com.example.rememberword;

import java.util.ArrayList;

import android.R.integer;
import android.util.Log;



public class MyRandom {
	
	ArrayList< int[] > mArrayList = null;
	
	int [] array;
	int front,count;
	int current;
	
	int range;
	
	public MyRandom(int size, int range) {
		// TODO Auto-generated constructor stub
		if( size > 0)
			array = new int[size];
		else {
			array = new int[4];
		}
		front = count = 0;
		current = -1;
		this.range = range;
	}
	
	public MyRandom(int size, ArrayList< int[] > mArrayList) {
		// TODO Auto-generated constructor stub
		if(mArrayList.size() > 0) {
			array = new int[size];
			Log.e("MyRandom------------------------>",""+mArrayList.size());
		}
			
		else {
			array = new int[4];
		}
		front = count = 0;
		current = -1;
		this.mArrayList = mArrayList;
		
	}
		
	
	public int getPrev() {
		if(current > 0) {
			current--;
			return array[ (front+current)%array.length ];
		}
		return -1;
	}
	
	public int getNext() {
		
		int rtn;
		
		if( current < count-1) {
			current++;
			rtn = array[ (front+current)%array.length ];
			
		}
		else if(count < array.length){
			current++;
			count++;
			rtn = array[ (front+current)%array.length ] = getRandom();
		}
		else {
			front = (front+1)%array.length;
			rtn = array[ (front+current)%array.length ] = getRandom();
		}
		
		Log.d("getNext--------------->", ""+rtn);
		return rtn;
			
	}
	
	public int getCurrent() {
		return array[ (front+current)%array.length ];
	}
	
	public int  getRandomOld() {
		
		int rtn = 11;
		if( mArrayList != null ) {		//带权随机数
			int sumPower = 0;
			for (int i = 0; i < mArrayList.size(); i++) {
				sumPower = sumPower + mArrayList.get(i)[1];
			}
			Log.d("The sum PowerWeight--------------->", ""+sumPower);
			
			int ramdom = (int)Math.random()*sumPower;
			
			for (int i = 0; i < mArrayList.size(); i++) {
				ramdom = ramdom - mArrayList.get(i)[1];
				if(ramdom < 0) {
					rtn = mArrayList.get(i)[1];
					break;
				}
			}
			
			
		} else {
			rtn = (int) (Math.random()*range);
		}
		
		Log.d("The getRandom--------------->", "");
		return rtn;
	}
	
	public int  getRandom() {
		
		int rtn = 11;
		if( mArrayList != null ) {		//带权随机数
			int sumPower = 0;
			for (int i = 0; i < mArrayList.size(); i++) {
				sumPower +=  mArrayList.get(i)[1];
			}
			
			System.out.println("The sumPower--------------->"+sumPower);
			
			int ramdom = (int) (Math.random()*sumPower);
			
			System.out.println("The random--------------->"+ramdom);
			
			int i;
			for (i = 0; i < mArrayList.size(); i++) {
				ramdom = ramdom - mArrayList.get(i)[1];
				if(ramdom < 0) {					
					break;
				}
			}
			rtn = i;			
		} else {
			System.out.println("mArrayList = null");
			rtn = (int) (Math.random()*range);
		}
		System.out.println("The getRandom===============================>"+rtn);
		return rtn;
	}

}
