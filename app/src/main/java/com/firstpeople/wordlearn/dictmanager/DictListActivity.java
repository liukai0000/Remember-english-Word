package com.firstpeople.wordlearn.dictmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.rememberword.R;


import com.firstpeople.wordlearn.util.Config;
import com.firstpeople.wordlearn.util.GetCsvInfo;
import com.firstpeople.wordlearn.util.GetDictList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DictListActivity extends Activity {

        private ListView listview = null;
        private ArrayList<HashMap<String, Object>> items = null;
        // 保存当前路径，以方便返回用
        private boolean isDialogShow=false;
        
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                String dictType = this.getIntent().getStringExtra(Config.DICTTYPE);
                updateDictList(dictType);
        }

        // 更新词典列表
        private void updateDictList(final String dictType) {

                // 进度框
                final ProgressDialog pd = new ProgressDialog(this);
                pd.setIcon(android.R.drawable.ic_dialog_info);
                pd.setTitle(getString(R.string.dialog_wait_title));
                pd.setMessage(getString(R.string.dialog_wait_dis));
                pd.show();
                // 关闭对话框后出发的事件
                pd.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface arg0) {
                        	if(!isDialogShow)
                                setContent();// 设置界面内容
                        	else {
                        			finish();
							}
                        }
                });

                new Thread() {
                        @Override
                        public void run() {
                        		isDialogShow=true;
                        		Log.e("aa", "1111");
                                new GetDictList().getList();
                        		Log.e("aa", "2222");
                                items = Config.init().getDictList(dictType);
                                if(items==null)items=new ArrayList<HashMap<String, Object>>();
                                isDialogShow=false;
                                pd.dismiss();
                        }
                }.start();

        }

        // 填充列表到界面上
        private void setContent() {
                SimpleAdapter adapter = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, new String[] {
                		"标题", "描述" }, new int[] { android.R.id.text1, android.R.id.text2 });
                listview = new ListView(this);
                listview.setAdapter(adapter);

                setContentView(listview);

                Toast.makeText(DictListActivity.this, R.string.dict_set, Toast.LENGTH_SHORT).show();

                // 单机事件
                listview.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {

                                @SuppressWarnings("rawtypes")
								Map map = (Map) av.getItemAtPosition(position);
                                dialogProcess((String)map.get("标题"));
                        }
                });

                // 长按事件，将选择的项目返回
                listview.setOnItemLongClickListener(new OnItemLongClickListener() {

                        @SuppressWarnings("rawtypes")
						@Override
                        public boolean onItemLongClick(AdapterView<?> av, View v, int position, long arg3) {

                                Map map = (Map) av.getItemAtPosition(position);
                                dialogProcess((String)map.get("标题"));
                                return false;
                        }
                });

        }

        private void dialogProcess(final String dictName) {
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setIcon(android.R.drawable.ic_dialog_alert);
                ad.setTitle(dictName);
                String dictType = Config.init().getDictType( dictName);

                //csv类型词库
                if (dictType.equalsIgnoreCase(Config.DICTTYPE_CSV)) {
                        ad.setMessage(getString(R.string.dict_set_current_remember_dict));
                }else if(dictType.equalsIgnoreCase(Config.DICTTYPE_STARDICT)){
                        ad.setMessage(getString(R.string.dict_set_current_remember_dict));
                }
                

                // 确定按钮
                ad.setPositiveButton(getString(R.string.dialog_positiveButton), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                setDictCurrentUse(dictName);
                        }
                });

                // 取消按钮
                ad.setNegativeButton(getString(R.string.dialog_negativeButton), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                        }
                });

                ad.show();      
        }
        
        // 计算单词数并完成相应处理
        private void setDictCurrentUse(final String dictName) {
                String dictType = Config.init().getDictType( dictName);

                //csv类型词库
                if (dictType.equalsIgnoreCase(Config.DICTTYPE_CSV)) {

                        // 保存配置，当前使用的词库。
                        final ProgressDialog pd = new ProgressDialog(DictListActivity.this);
                        pd.setIcon(android.R.drawable.ic_dialog_info);
                        pd.setTitle(getString(R.string.dict_count_words));
                        pd.setMessage(getString(R.string.dialog_wait_dis));
                        pd.show();
                        pd.setOnDismissListener(new OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface arg0) {
                                        // 保存配置，当前使用的词库。
                                        Config.init().setCurrentUseDictName( dictName);
                                        
                                        // 清空记忆曲线表
//                                        Config.init().cleanRememberLine();
                                        
                                        Toast.makeText(DictListActivity.this, DictListActivity.this.getString(R.string.dict_set_up_success), Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK, new Intent());// 返回数据给上层
                                        finish();
                                }
                        });

                        // 从文件计算单词数
                        new Thread() {
                                @Override
                                public void run() {
                                        Config.init().setCurrentUseDictWordCount(new GetCsvInfo(Config.init().getDictPath( dictName)).getWordCount());
                                        pd.dismiss();
                                        
                                }
                        }.start();

                
                }else if(dictType.equalsIgnoreCase(Config.DICTTYPE_STARDICT)){
                        //StarDict类型词典
                        Config.init().setCurrentUseTransDictName(dictName);
                        Toast.makeText(this, this.getString(R.string.dialog_wait_success), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK, new Intent());// 返回数据给上层
                        finish();
                }

        }

}
