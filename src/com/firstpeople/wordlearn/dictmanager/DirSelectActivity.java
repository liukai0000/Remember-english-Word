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

        // ���浱ǰ·�����Է��㷵���á�
        private String lastPath = null;

        // ѡ��Ŀ¼����
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

        // �г�Ŀ¼
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

                // �����¼����г���ǰĿ¼��
                listview.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
                                String path = av.getItemAtPosition(position).toString();
                                // Log.v("path", path);
                                // ������Ƿ��ؼ������г���ǰĿ¼
                                if (path.equals(getString(R.string.dir_back))) {
                                        // ��ǰ������Ǹ�Ŀ¼���򷵻ظ�Ŀ¼�б�
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

                                                // 2.0�Ժ�Ļ���������ļ��в鿴Ȩ�޲��������
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

                // �����¼�����ѡ���Ŀ¼���ء�
                listview.setOnItemLongClickListener(new OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(AdapterView<?> av, View arg1, int position, long arg3) {
                                final String path = av.getItemAtPosition(position).toString();

                                if (!path.equals(getString(R.string.dir_back))) {// ���ذ�ť
                                        if (new File(path).listFiles() != null) {// �ж�дȨ�޵�Ŀ¼
                                                AlertDialog.Builder ad = new AlertDialog.Builder(av.getContext());
                                                ad.setIcon(android.R.drawable.ic_dialog_alert);
                                                ad.setTitle(path);
                                                ad.setMessage(getString(R.string.dir_set_dict_up));

                                                // ȷ����ť
                                                ad.setPositiveButton(getString(R.string.dialog_positiveButton), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                                setResult(RESULT_OK, new Intent());// �������ݸ��ϲ�

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

                                                // ȡ����ť
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

