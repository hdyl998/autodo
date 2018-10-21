package com.autodo.jiakaocapture;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autodo.alipaycapture.DataSaveUtils;
import com.autodo.mainjob.BaseHandler;
import com.autodo.tools.LogUtils;
import com.autodo.utils.Tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018/9/24.
 */

public class CollectAllDataHandlder extends BaseHandler {
    public final static HashMap<String, QuestionItem> hashMap = new HashMap<>();
    static final String fileName = "jiakao";

    static {
        String s = DataSaveUtils.readData(fileName);
        if (s != null) {
            HashMap<String, JSONObject> arrays = JSON.parseObject(s, HashMap.class);
            for (Map.Entry<String, JSONObject> set : arrays.entrySet()) {
                QuestionItem daite = JSON.parseObject(set.getValue().toJSONString(), QuestionItem.class);
                hashMap.put(daite.getKey(), daite);
            }
        }
        LogUtils.d("读取到！" + hashMap.size());
    }

    public CollectAllDataHandlder(AccessibilityService service) {
        super(service);
    }


    @Override
    public boolean handle() {
        String text = null;
        int count = 0;

        AccessibilityNodeInfo rootViewPager = findFirstById("com.handsgo.jiakao.android:id/main_panel");

        for (int i = 0; i < 10000; i++) {


            LogUtils.d("rootViewPager.getChildCount()" + rootViewPager.getChildCount());


            AccessibilityNodeInfo nodeInfoNum = findFirstById("com.handsgo.jiakao.android:id/practice_indicator_text");


            String strNum = nodeInfoNum.getText().toString();

            strNum = strNum.substring(0, strNum.indexOf("/"));


         try {
             for (int j = 0; j < rootViewPager.getChildCount(); j++) {
                 AccessibilityNodeInfo rootNodeInfo = rootViewPager.getChild(j);
                 if (rootNodeInfo == null) {
                     continue;
                 }
                 AccessibilityNodeInfo nodeInfo = Tools.findFirstById(rootNodeInfo, "com.handsgo.jiakao.android:id/practice_content_text");

                 if (nodeInfo == null) {
                     continue;
                 }
                 QuestionItem questionItem = new QuestionItem();
                 questionItem.title = nodeInfo.getText().toString();

                 LogUtils.d(questionItem.title);

                 questionItem.num = strNum;

                 AccessibilityNodeInfo nodeInfo1 = Tools.findFirstById(rootNodeInfo, ("com.handsgo.jiakao.android:id/practice_option_a"));
                 questionItem.listAnswer.add(nodeInfo1.getText().toString());


                 AccessibilityNodeInfo nodeInfo2 = Tools.findFirstById(rootNodeInfo, ("com.handsgo.jiakao.android:id/practice_option_b"));
                 questionItem.listAnswer.add(nodeInfo2.getText().toString());

                 AccessibilityNodeInfo nodeInfo3 = Tools.findFirstById(rootNodeInfo, ("com.handsgo.jiakao.android:id/practice_option_c"));
                 AccessibilityNodeInfo nodeInfo4 = null;
                 if (nodeInfo3 != null) {
                     questionItem.listAnswer.add(nodeInfo3.getText().toString());
                     nodeInfo4 = Tools.findFirstById(rootNodeInfo, ("com.handsgo.jiakao.android:id/practice_option_d"));
                     questionItem.listAnswer.add(nodeInfo4.getText().toString());
                 }

                 AccessibilityNodeInfo answerNodeInfo = Tools.findFirstById(rootNodeInfo, ("com.handsgo.jiakao.android:id/answer_text"));
                 if (answerNodeInfo != null) {
                     String answer = answerNodeInfo.getText().toString();
                     questionItem.answer = answer.charAt(answer.length() - 1) - 'A';
                 }
                 AccessibilityNodeInfo miji = Tools.findFirstById(rootNodeInfo, ("com.handsgo.jiakao.android:id/practice_summary_text"));
                 if (miji != null) {
                     questionItem.miji = miji.getText().toString();
                 }
                 AccessibilityNodeInfo guanfang = Tools.findFirstById(rootNodeInfo, ("com.handsgo.jiakao.android:id/practice_explain_text"));
                 if (guanfang != null) {
                     questionItem.guanfang = guanfang.getText().toString();
                 }
                 AccessibilityNodeInfo rate = Tools.findFirstById(rootNodeInfo, ("com.handsgo.jiakao.android:id/error_rate_progress"));
                 if (rate != null) {
                     questionItem.errorRate = rate.getText().toString();
                 }
                 QuestionItem item = hashMap.get(questionItem.getKey());
                 int answer = 0;
                 if (item == null) {
                     if (nodeInfo3 == null) {
                         answer = new Random().nextInt(2);
                     } else
                         answer = new Random().nextInt(4);
                     LogUtils.d(strNum + "不会做");
                 } else {
                     answer = item.answer;
                 }
                 AccessibilityNodeInfo[] infos = {nodeInfo1, nodeInfo2, nodeInfo3, nodeInfo4};
                 Tools.doClick(infos[answer]);
             }
         }
         catch (Exception e){

         }
//            SuSystemUtils.execShellCmd("input swipe 400 400 100 400");
            sleep(2000);
//            LogUtils.d("任务进度！" + hashMap.size());
//            if (size != hashMap.size())
//                DataSaveUtils.saveData(JSON.toJSONString(hashMap), fileName);
//            else {
//                LogUtils.d("没有添加" + strNum);
//            }
        }
        return false;
    }


}
