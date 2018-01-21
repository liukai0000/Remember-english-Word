package com.firstpeople.wordlearn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetStarDictInfo{

        private String dictPath = null;
        private StringBuilder sb = null;
        private Pattern p = null;
        private Matcher m = null;

        public GetStarDictInfo(String dictPath) {
                this.dictPath = dictPath;

                //读取描述文件
                sb = new StringBuilder();
                BufferedReader br = null;
                try {
                        br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(dictPath)), "utf-8"));
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }

                String line = null;
                try {
                        while( (line = br.readLine()) != null) {
                                sb.append(line + "\n");
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }finally{
                        try {
                                br.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        public String getDictName() {
                p = Pattern.compile("bookname=(.*?)\\n");
                m = p.matcher(sb.toString());

                if(m.find()) {
                        String result = m.group();
                        result = result.substring(result.indexOf("=") + 1).replace("\n", "");
                        return result;
                }
                return "未命名词典";
        }

        public String getFileNameWithoutExtension() {
                String dictName = new File(dictPath).getName();
                return dictName.substring(0, dictName.lastIndexOf("."));
        }

        public String getFileSize() {
                return String.valueOf(new File(dictPath.replaceAll(".ifo", ".dict.dz")).length() / 1000 + "KB");
        }

        public String getWordCount() {
                p = Pattern.compile("wordcount=(.*?)\\n");
                m = p.matcher(sb.toString());

                if(m.find()) {
                        String result = m.group();
                        result = result.substring(result.indexOf("=") + 1).replace("\n", "");
                        return result;
                }
                return "0";
        }

}
