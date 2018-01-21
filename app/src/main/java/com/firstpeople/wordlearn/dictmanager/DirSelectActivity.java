package com.firstpeople.wordlearn.dictmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.rememberword.R;
import com.firstpeople.wordlearn.util.Config;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DirSelectActivity extends Activity {

        private List<String> items = null;

        private ListView listview = null;

        // 保存当前路径，以方便返回用。
        private String lastPath = null;

        // 选择目录类型
        private String select_type = null;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                select_type = getIntent().getStringExtra(Config.SELECTTYPE);

                lastPath = new String("/");
                fill(new File("/").listFiles());
                Toast.makeText(DirSelectActivity.this, R.string.dir_set_dict, Toast.LENGTH_SHORT).show();
        }

        // 列出目录
        private void fill(File[] files) {
                items = new ArrayList<String>();
                items.add(getString(R.string.dir_back));
                for (File file : files) {
                        if (file.isDirectory()) {
                                items.add(file.getPath() + "/");
                        } else {
                                items.add(file.getPath());
                        }
                }
                ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
                listview = new ListView(this);
                listview.setAdapter(fileList);

                setContentView(listview);

                // 单击事件，列出当前目录。
                listview.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
                                String path = av.getItemAtPosition(position).toString();
                                // Log.v("path", path);
                                // 如果不是返回键，则列出当前目录
                                if (path.equals(getString(R.string.dir_back))) {
                                        // 当前如果不是根目录，则返回父目录列表
                                        if (!lastPath.equals("/")) {
                                                File parent = new File(lastPath).getParentFile();
                                                lastPath = parent.getAbsolutePath();
                                                fill(parent.listFiles());
                                        } else {
                                                setResult(RESULT_CANCELED, null);
                                                finish();
                                        }
                                } else {
                                        File file = new File(path);
                                        if (file.isDirectory()) {
                                                lastPath = path;

                                                // 2.0以后的机器会出现文件夹查看权限不足的问题
                                                if (file.listFiles() != null) {
                                                        Log.i("files", file.listFiles().toString());
                                                        fill(file.listFiles());
                                                } else {
                                                        Toast.makeText(DirSelectActivity.this, getString(R.string.dir_no_open), Toast.LENGTH_SHORT).show();
                                                }

                                        } else {
                                                Toast.makeText(DirSelectActivity.this, getString(R.string.dir_no_open), Toast.LENGTH_SHORT).show();
                                        }
                                }
                        }
                });

                // 长按事件，将选择的目录返回。
                listview.setOnItemLongClickListener(new OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(AdapterView<?> av, View arg1, int position, long arg3) {
                                final String path = av.getItemAtPosition(position).toString();

                                if (!path.equals(getString(R.string.dir_back))) {// 返回按钮
                                        if (new File(path).listFiles() != null) {// 有读写权限的目录
                                                AlertDialog.Builder ad = new AlertDialog.Builder(av.getContext());
                                                ad.setIcon(android.R.drawable.ic_dialog_alert);
                                                ad.setTitle(path);
                                                ad.setMessage(getString(R.string.dir_set_dict_up));

                                                // 确定按钮
                                                ad.setPositiveButton(getString(R.string.dialog_positiveButton), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                                setResult(RESULT_OK, new Intent());// 返回数据给上层

                                                                if (select_type != null) {
                                                                        if (select_type.equalsIgnoreCase(Config.SELECT_DICTDIR)) {
                                                                                Config.init().setDictDir(path);
                                                                        } else if (select_type.equalsIgnoreCase(Config.SELECT_SOUNDDIR)) {
                                                                                Config.init().setSoundDir(path);
                                                                        }
                                                                }

                                                                Log.i("path", path);
                                                                finish();
                                                        }
                                                });

                                                // 取消按钮
                                                ad.setNegativeButton(getString(R.string.dialog_negativeButton), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                        }
                                                });

                                                ad.create();
                                                ad.show();
                                        } else {
                                                Toast.makeText(DirSelectActivity.this, getString(R.string.dir_no_open), Toast.LENGTH_SHORT).show();
                                        }

                                }

                                return true;
                        }
                });
        }

}

