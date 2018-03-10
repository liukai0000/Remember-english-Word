package com.firstpeople.wordlearn.dictmanager;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.rememberword.R;
import com.firstpeople.wordlearn.util.Config;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DictManagerActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictmanager);
        this.setTitle("设置");
        Log.e("aaaaaaaa", ""+Config.init().getCurrentUseTransDictName());

        showContent();
    }


    private ArrayList<Map<String, Object>> list = null;

    public void showContent() {
        list = new ArrayList<Map<String, Object>>();



        Map<String, Object> map;
        map = new HashMap<String, Object>();
        map.put("标题", getString(R.string.dictmanager_title_dictpath));
        map.put("描述", Config.init().getDictDir());
        map.put("图标", R.drawable.dictmanager_icon_dictpath);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("标题", getString(R.string.dictmanager_title_dictsetup));
        map.put("描述", Config.init().getCurrentUseDictName());
        map.put("图标", R.drawable.dictmanager_icon_dictsetup);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("标题", getString(R.string.dictmanager_title_dictransetup));
        map.put("描述", Config.init().getCurrentUseTransDictName());
        map.put("图标", R.drawable.dictmanager_icon_dictransetup);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("标题", getString(R.string.dictmanager_title_dictcode));
        map.put("描述", Config.init().getDictCharset());
        map.put("图标", R.drawable.dictmanager_icon_dictcode);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("标题", getString(R.string.dictmanager_title_dictdownload));
        map.put("描述", getString(R.string.dictmanager_dis_dictdownload));
        map.put("图标", R.drawable.dictmanager_icon_dictdownload);
        list.add(map);

        SimpleAdapter adapter
                = new SimpleAdapter(this, list, R.layout.list_dictmanager_adapter, new String[] { "标题",
                "描述", "图标" }, new int[] { R.id.dictmanager_adapter_tv_title, R.id.dictmanager_adapter_tv_dis,
                R.id.dictmanager_adapter_iv });

        setListAdapter(adapter);

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        switch (position) {
            case 0: {

                Intent i = new Intent();
                i.putExtra(Config.SELECTTYPE, Config.SELECT_DICTDIR);
                i.setClass(DictManagerActivity.this, DirSelectActivity.class);
                startActivityForResult(i, 0);// 要获取返回值，必需用此方法
                break;
            }
            case 1: {

                Intent i = new Intent();
                i.putExtra(Config.DICTTYPE, Config.DICTTYPE_CSV);
                i.setClass(DictManagerActivity.this, DictListActivity.class);
                startActivityForResult(i, 0);// 要获取返回值，必需用此方法
                break;
            }
            case 2: {
                Intent i = new Intent();
                i.putExtra(Config.DICTTYPE, Config.DICTTYPE_STARDICT);
                i.setClass(DictManagerActivity.this, DictListActivity.class);
                startActivityForResult(i, 0);// 要获取返回值，必需用此方法
                break;
            }
            case 3: {

                String charset=Config.init().getDictCharset();
                int initPosition=0;
                if(charset.equals("UTF-8")){initPosition=0;}
                else if(charset.equals("GB2312")){initPosition=1;}
                else if(charset.equals("GBK")){initPosition=2;}
                else if(charset.equals("Big5")){initPosition=3;}


                new AlertDialog.Builder(this).setTitle("词库编码").setSingleChoiceItems(
                        new String[] { "UTF-8", "GB2312" ,"GBK","Big5"}, initPosition,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                String newValue=null;
                                switch (which) {
                                    case 0:
                                        newValue="UTF-8";
                                        break;
                                    case 1:
                                        newValue="GB2312";
                                        break;
                                    case 2:
                                        newValue="GBK";
                                        break;
                                    case 3:
                                        newValue="Big5";
                                        break;
                                    default:
                                        newValue="UTF-8";
                                        break;
                                }
                                Config.init().setDictCharset(newValue);
                                showContent();

                            }
                        }).setNegativeButton("确定", null).show();
                break;
            }
            case 4: {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY,"英语词库下载");
                startActivity(intent);
                break;
            }
            default: {
            }
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK: {
                this.showContent();
                break;
            }
            case RESULT_CANCELED:
                break;
        }
    }

}