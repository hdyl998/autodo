package com.autodo.alipaycapture;

import android.os.Environment;
import android.util.Log;

import com.autodo.tools.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>Created by Administrator on 2018/9/17.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class DataSaveUtils {
    public static String readData() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/download/zhifubaomoney.txt");
        if (!file.exists()) {
            Log.d("tttt", "配置文件不存在");
            return null;
        }
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String str = reader.readLine();
            while (str != null) {
                builder.append(str);
                str = reader.readLine();
            }
            LogUtils.d("读档成功!");
            return builder.substring(0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tttt", e.toString());
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                }
        }
        return null;
    }

    public static void saveData(String string) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/download/zhifubaomoney.txt");
        if (!file.exists()) {
            Log.d("tttt", "配置文件不存在");
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, false); // 如果追加方式用true
            out.write(string.getBytes("utf-8"));// 注意需要转换对应的字符集
            out.close();

            LogUtils.d("存档成功!");
        } catch (IOException e) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


}
