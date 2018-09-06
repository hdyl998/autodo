package com.autodo.logic;

import com.autodo.tools.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 一组选项
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class GroupSelectItem {

    /**
     * 检查是否合法
     *
     * @return
     */
    public boolean isCheckOK() {
        money = times * zhushu * 2;
        if (listPassway.contains(1)) {
            LogUtils.d("不支持单关");
            return false;
        }
        if (selectItemList.size() > 8 || money > 10000) {
            return false;
        }
        return true;
    }

    public List<SelectItem> selectItemList = new ArrayList<>();

    public int times = 5;//倍数
    public int money;//总价
    public int zhushu = 1;//注数

    public String qrCode;//二维码


    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public List<Integer> listPassway = new ArrayList<>();//过关方式


    public GroupSelectItem createOne() {


        SelectItem selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周四001";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周四002";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周四003";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周四004";


        listPassway.add(2);//2串1
        listPassway.add(3);//2串1
        listPassway.add(4);//2串1
        return this;
    }

    public GroupSelectItem createTwo() {


        SelectItem selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五002";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五003";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五004";


        listPassway.add(2);//2串1
        listPassway.add(3);//2串1
        return this;
    }
}
