package com.autodo.chongding;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.mainjob.BaseHandler;
import com.autodo.tools.LogUtils;

import java.util.HashSet;

/**
 * Created by Administrator on 2018/9/24.
 */

public class ChongdingDataHandlder extends BaseHandler {


    HashSet<String> hashSet;

    public ChongdingDataHandlder(AccessibilityService service, HashSet<String> hashSet) {
        super(service);
        this.hashSet = hashSet;
    }


    private static final String TAG = "ChongdingDataHandlder";

    @Override
    public boolean handle() {
        AccessibilityNodeInfo title = findFirstById("com.chongdingdahui.app:id/tvMessage");


        if (title == null) {
            LogUtils.d(TAG, "title AccessibilityNodeInfo is nulll");
            return false;
        }

        String titleStr = title.getText().toString();
        int index = titleStr.indexOf(".");
        if (index == -1) {
            LogUtils.d(TAG, "index is null");
            return false;
        }
        String strIndex = titleStr.substring(0, index);

        if (hashSet.contains(strIndex)) {
            LogUtils.d(TAG, String.format("strindex %s has been added ", strIndex));
            return false;
        }
        hashSet.add(strIndex);
        AccessibilityNodeInfo one = findFirstById("com.chongdingdahui.app:id/answer0");
        AccessibilityNodeInfo two = findFirstById("com.chongdingdahui.app:id/answer1");
        AccessibilityNodeInfo three = findFirstById("com.chongdingdahui.app:id/answer2");

        String oneStr = one.getText().toString();
        String twoStr = two.getText().toString();
        String threeStr = three.getText().toString();


        String url = "https://www.baidu.com/s?ie=UTF-8&wd=%s";

        String datas[] = {oneStr, twoStr, threeStr};

        int results[] = new int[3];
//
//        for (String str : datas) {
//            ViseHttp.GET("")
//                    .baseUrl(String.format(url, titleStr + str))
//                    .request(new ACallback<String>() {
//                        @Override
//                        public void onSuccess(String data) {
//                            String ddd = "百度为您找到相关结果约";
//                            int index = data.indexOf(ddd);
//
//                            data = data.substring(index);
//
//                            int index2 = data.indexOf("个");
//                            String result = data.substring(index, index2);
//                            LogUtils.d(TAG, str + "->" + result);
//
//                        }
//
//                        @Override
//                        public void onFail(int errCode, String errMsg) {
//
//                        }
//                    });


//        }
        return false;
    }


}
