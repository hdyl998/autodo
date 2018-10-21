package com.autodo.jiakaocapture;

import com.autodo.utils.MD5Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/9/24.
 */

public class QuestionItem {

    public String title;
    public List<String> listAnswer = new ArrayList<>(4);
    public int answer;
    public String miji;
    public String guanfang;
    public String errorRate;
    public String num;

    public String getKey() {
        StringBuilder builder = new StringBuilder(title);
        String ans = listAnswer.get(answer);
        Collections.sort(listAnswer);
        answer = listAnswer.indexOf(ans);
        for (String string :
                listAnswer) {
            builder.append(",");
            builder.append(string);
        }
        return MD5Utils.encrypt(builder.substring(0));
    }
}
