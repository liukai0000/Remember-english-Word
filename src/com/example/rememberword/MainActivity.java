package com.example.rememberword;

import java.util.HashMap;

import com.firstpeople.wordlearn.util.Config;
import com.firstpeople.wordlearn.util.Utils;







import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

/**
 * This demonstrates how you can implement switching between the tabs of a
 * TabHost through fragments.  It uses a trick (see the code below) to allow
 * the tabs to switch between fragments instead of simple views.
 */
public class MainActivity extends FragmentActivity {
    TabHost mTabHost;
    TabManager mTabManager;
    
    public static Context mContext;
	public MainActivity() {
		mContext = this;
		
	}
	
	private boolean checkSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        /***********************************************************************************************//*
		if (checkSDCard()) {
			if (Config.init().isFirstRun()) {
				final ProgressDialog pd = new ProgressDialog(this);
				pd.setTitle(getString(R.string.dialog_wait_title));
				pd.setMessage(getString(R.string.dialog_wait_dis));
				pd.setIcon(android.R.drawable.ic_dialog_info);
				pd.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface arg0) {
						Toast.makeText(mContext,
								getString(R.string.dialog_wait_success),
								Toast.LENGTH_LONG).show();
					}
				});
				pd.show();
				new Thread() {
					@Override
					public void run() {
						Config.init().initInstall();// ��ʼ�����
						pd.dismiss();
					}
				}.start();
			}
			else
			{		
				boolean isDictFileExist
					=Utils.init().isExist(Config.init().getDictPath(Config.init().getCurrentUseTransDictName()))
					&&Utils.init().isExist(Config.init().getDictPath(Config.init().getCurrentUseDictName()));

	            if(!isDictFileExist)
	                {
		               	Toast.makeText(this, "���Ĵʵ��ļ��⵽�ٻ���������ϵͳ��", Toast.LENGTH_SHORT).show();                   
	                }
			}
		} else {
			Toast.makeText(mContext, "�������sdCard��װ�Ƿ���ȷ��", Toast.LENGTH_LONG)
					.show();
			finish();
		}
		*//***********************************************************************************************/
                
        setContentView(R.layout.activity_tab);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        mTabManager.addTab(mTabHost.newTabSpec("���ʱ�").setIndicator("���ʱ�"),
        		WordsNote_Fragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("������").setIndicator("������"),
        		KnowWell_Fragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("ȫ��").setIndicator("ȫ��"),
        		AllWords_Fragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("����").setIndicator("����"),
        		Setting_Fragment.class, null);
        
        
        Log.e("Init---------------------------->","");
        
        
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
            
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    /**
     * This is a helper class that implements a generic mechanism for
     * associating fragments with the tabs in a tab host.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between fragments.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabManager supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct fragment shown in a separate content area
     * whenever the selected tab changes.
     */
    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
            	
            	Log.d("TabActivity----------addTab-----tag---->",""+tag);
            	
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
        	
        	Log.e("TabActivity------------------->","onTabChanged");
        	
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
            	Log.d("TabActivity------------onTabChanged------->","There is a new tab coming");
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {						//ж�ؾ�tab
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                if (newTab != null) {						//װ����tab
                    if (newTab.fragment == null) {			//�����tab��fragment��û�д���
                        
                    	Log.d("TabActivity------------onTabChanged------->","The new tab"+ tabId +" has no fragment ,create it");
                    	newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {								//�����tab��fragment�Ѿ�����
                    	Log.d("TabActivity------------onTabChanged------->","The new tab"+ tabId +" has got fragment ,attached it");
                        ft.attach(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
    }
    
    public void init() {
    	Log.e("Init---------------------------->","");
    	if (checkSDCard()) {
			if (Config.init().isFirstRun()) {
				final ProgressDialog pd = new ProgressDialog(this);
				pd.setTitle(getString(R.string.dialog_wait_title));
				pd.setMessage(getString(R.string.dialog_wait_dis));
				pd.setIcon(android.R.drawable.ic_dialog_info);
				pd.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface arg0) {
						Toast.makeText(mContext,
								getString(R.string.dialog_wait_success),
								Toast.LENGTH_LONG).show();
					}
				});
				pd.show();
				new Thread() {
					@Override
					public void run() {
						Config.init().initInstall();// ��ʼ�����
						pd.dismiss();
					}
				}.start();
			}
			else
			{		
				boolean isDictFileExist
					=Utils.init().isExist(Config.init().getDictPath(Config.init().getCurrentUseTransDictName()))
					&&Utils.init().isExist(Config.init().getDictPath(Config.init().getCurrentUseDictName()));

	            if(!isDictFileExist)
	                {
		               	Toast.makeText(this, "���Ĵʵ��ļ��⵽�ٻ���������ϵͳ��", Toast.LENGTH_SHORT).show();                   
	                }
			}
		} else {
			Toast.makeText(mContext, "�������sdCard��װ�Ƿ���ȷ��", Toast.LENGTH_LONG)
					.show();
			finish();
		}
    }
}