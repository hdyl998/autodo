package com.example.test1.fragmentadd.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * 一组选项
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class GroupSelectItem {
    public List<SelectItem> selectItemList = new ArrayList<>();

    public int times = 895;//倍数
    public int money;//总价

    public int zhushu = 1;//注数

    public List<Integer> listPassway = new ArrayList<>();//过关方式

    {

        SelectItem selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五027";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五002";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五023";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五004";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五025";

        selectItem = new SelectItem();
        selectItemList.add(selectItem);
        selectItem.orderId = "周五006";


        listPassway.add(2);//2串1
        listPassway.add(3);//2串1
        listPassway.add(4);//2串1
    }
}
