package com.example.test1.fragmentadd.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class SelectItem {
    public String orderId;
    public List<Integer> seletions = new ArrayList<>();


    /***
     * 是否是全部是胜平负,及让球胜平负
     * @return
     */
    public boolean isAllSpfRqSpf() {
        //胜平负,让球胜平负 在6以下
        for (int data : seletions) {
            if (data >= 6) {
                return false;
            }
        }
        return true;
    }

    //test
    {
        orderId = "周五001";
        seletions.add(10);
        seletions.add(12);
        seletions.add(22);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("orderId: ");
        builder.append(orderId);
        builder.append(" seletions: ");
        for (Integer integer : seletions) {
            builder.append(supportSelections.get(integer));
            builder.append(" ");
        }
        return builder.substring(0);
    }


    public final static List<String> supportSelections = Arrays.asList(new String[]{
            "胜", "平", "负",
            "让胜", "让平", "让负",
            "胜胜", "胜平", "胜负",
            "平胜", "平平", "平负",
            "负胜", "负平", "负负",
            "0", "1", "2", "3", "4", "5", "6", "7+",
            "1:0", "2:0", "2:1", "3:0", "3:1",
            "3:2", "4:0", "4:1", "4:2", "5:0",
            "5:1", "5:2", "胜其它",
            "0:0", "1:1", "2:2", "3:3", "平其它",

            "0:1", "0:2", "1:2", "0:3", "1:3",
            "2:3", "0:4", "1:4", "2:4", "0:5",
            "1:5", "2:5", "负其它"});


}
