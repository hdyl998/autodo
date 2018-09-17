package com.autodo.alipaycapture;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.autodo.tools.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Created by Administrator on 2018/9/17.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class AlipayDatas {

    List<Integer> lists = new ArrayList<>();

    int count = 50;

    //在这里写数据
    {
        lists.add(10);
        lists.add(20);
    }

    public List<PayInfoItem> listInfoItem = new ArrayList<>();


    private static final String TAG = "AlipayDatas";
    public int mCurrent = 0;

    public List<PayInfoItem> listAllFinished = new ArrayList<>();

    {
        String saveData = DataSaveUtils.readData();

        List<PayInfoItem> listHaveDo = null;
        if (saveData != null) {
            listHaveDo = JSON.parseArray(saveData, PayInfoItem.class);
            Iterator<PayInfoItem> iterator = listHaveDo.iterator();
            while (iterator.hasNext()) {
                PayInfoItem infoItem = iterator.next();
                if (TextUtils.isEmpty(infoItem.url)) {
                    iterator.remove();
                }
            }
            listAllFinished.addAll(listHaveDo);
        }
        for (Integer integer : lists) {
            for (int i = 0; i < count; i++) {
                String key = String.format("%.2f", (integer * 100 - i) / 100f);
                if (hasGetTheUrl(listHaveDo, key)) {
                    LogUtils.d(TAG, key + " has maked");
                    continue;
                }
                PayInfoItem infoItem = new PayInfoItem();
                infoItem.money = key;
                listInfoItem.add(infoItem);
            }
        }
    }

    /***
     * 是否已经获得URL
     * @param listHaveDo
     * @param key
     * @return
     */
    private boolean hasGetTheUrl(List<PayInfoItem> listHaveDo, String key) {
        if (listHaveDo == null) {
            return false;
        }
        for (PayInfoItem infoItem : listHaveDo) {
            if (key.equals(infoItem.money)) {
                return true;
            }
        }
        return false;
    }


    private final static AlipayDatas alipayDatas = new AlipayDatas();

    public static AlipayDatas getInstance() {
        return alipayDatas;
    }


    public PayInfoItem getCurrentItem() {
        if (mCurrent >= listInfoItem.size()) {
            return null;
        }
        return listInfoItem.get(mCurrent);
    }

    /***
     * 存档
     */
    public void saveNewData() {
        listAllFinished.add(getCurrentItem());
        Collections.sort(listAllFinished);
        DataSaveUtils.saveData(JSON.toJSONString(listAllFinished));
    }


    public boolean nextItem() {
        mCurrent++;
        if (mCurrent >= listInfoItem.size()) {
            return false;
        } else {
            return true;
        }
    }

}
