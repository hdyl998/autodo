package com.autodo.lottery;

import com.autodo.StatusCode;
import com.autodo.tools.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 一个订单
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class OrderItem {


    public List<SelectItem> selectItems = new ArrayList<>();
    public List<Integer> listPassway = new ArrayList<>();//过关方式

    public int timeOutExceptionCount = 0;//超时次数,如果超过两次那么就不再处理了

    public int times;//倍数
    public int money;//总价
//    public int zhushu = 1;//注数

    public int statusCode = StatusCode.STATUS_NOT_DO;//状态码

    public String qrCode;//二维码

    public String orderId;//订单ID

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public OrderItem() {
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderItem(LotteryJSONBean bean) {
        this.orderId = bean.pid;
        this.money = (int) bean.money;
        this.times = bean.beishu;
        paraseGGTypes(bean.ggtype);
        paraseOrderItem(bean.fileorcode);
    }

    private void paraseGGTypes(String ggTypes) {
        String datas[] = ggTypes.split(",");
        for (String data : datas) {
            this.listPassway.add(Integer.parseInt(data));
        }
    }


    /***
     * for test
     * @param content
     */
    public OrderItem(String content) {
        paraseOrderItem(content);
    }


    public int getStatusCode() {
        return statusCode;
    }

    private void paraseOrderItem(String content) {
        String datas[] = content.split("\\/");
        HashMap<String, SelectItem> hashMap = new HashMap<>();
        for (String data : datas) {
            SelectItem item = createFromServerData(data);
            SelectItem old = hashMap.get(item.orderNum);
            if (old == null) {//第一次,记录
                hashMap.put(item.orderNum, item);
                selectItems.add(item);//添加新的
            } else {//已记录,直接合并记录
                old.merginSelections(item);
            }
        }
    }
    //8747|2001|1[3#1.52,1#3.40]/
    // 8747|2001|2[3#2.85]/
    // 8748|2019|1[3#3.00,1#2.97,0#2.18]/
    // 8748|2019|2[3#1.52,1#3.70,0#5.00]/
    // 8748|2019|4[1:0#8.00,2:0#13.00,2:1#9.50,3:0#35.00,3:1#27.00,3:2#39.00,4:0#100.00,4:1#70.00,4:2#100.00,5:0#300.00,5:1#250.00,5:2#300.00,胜其它#120.00,0:0#7.50,1:1#5.75,2:2#14.00,3:3#70.00,平其它#400.00,0:1#6.50,0:2#8.00,1:2#7.50,0:3#18.00,1:3#17.00,2:3#30.00,0:4#45.00,1:4#45.00,2:4#80.00,0:5#120.00,1:5#120.00,2:5#250.00,负其它#80.00]/
    // 8748|2019|3[0#7.50,1#3.70,2#3.05,3#3.70,4#6.40,5#12.00,6#23.00,7#34.00]/
    // 8748|2019|5[3-3#5.00,3-1#13.00,3-0#30.00,1-3#6.50,1-1#4.40,1-0#4.60,0-3#40.00,0-1#13.00,0-0#3.30]


    /***
     * 获取
     * @param orderNum
     * @return
     */
    private String getOrderNumText(String orderNum) {
        int value = orderNum.charAt(0) - '1';
        //1001转换成周一001
        return ConstDatas.weekArrays[value] + orderNum.substring(1);
    }


    public SelectItem createFromServerData(String data) {
        String datas[] = data.split("\\|");
        SelectItem item = new SelectItem();
        item.orderNum = getOrderNumText(datas[1]);

        String moreInfo = datas[2];
        int playWay = moreInfo.charAt(0) - '0';//玩法ID
        String selection = moreInfo.substring(2, moreInfo.length() - 1);
        String selections[] = getSelections(selection);
        for (String select : selections) {
            String realKey = select;
            switch (playWay) {
                case 1:
                case 2:
                    realKey = playWay + select;
                default:
                    Integer value = ConstDatas.serverKeys.get(realKey);
                    if (value == null) {
                        throw new RuntimeException("error " + data + " play way is null");
                    } else {
                        item.seletions.add(value);
                    }
                    break;
            }
        }
        return item;
    }


    private String[] getSelections(String selection) {
        String selections[] = selection.split(",");
        int count = 0;
        for (String str : selections) {
            int index = str.indexOf("#");
            if (index != -1) {
                selections[count] = str.substring(0, index);
            } else {//单关
                index = str.indexOf("@");
                if (index != -1) {
                    selections[count] = str.substring(0, index);
                } else {
                    selections[count] = str;
                }

            }
            count++;
        }
        return selections;

    }


    /***
     * 检查注数,钱,倍数,数字是否正确
     * @param money
     * @param times
     * @return
     */
    public boolean checkOrderInfoRight(int money, int times) {
        if (this.money != money || this.times != times) {
            return false;
        }
        return true;
    }


    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }


    /**
     * 检查是否合法
     *
     * @return
     */
    public int isCheckOK() {
//        money = times * zhushu * 2;
        if (listPassway.contains(1)) {
            return StatusCode.ERROR_NOT_SUPPORT_SINGLE_PASSWAY;
        }
        if (selectItems.size() > 8) {
            return StatusCode.ERROR_MATCH_MORE_THAN_8;
        }
        if (money > 10000) {
            return StatusCode.ERROR_MATCH_MORE_THAN_8;
        }
        return StatusCode.STATUS_SURE;
    }


    //用于测试
    public OrderItem createOne() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String[] tmp = new String[]{"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};//
        // 因为Canlendar的日期是从星期天开始,且对应的数值是从1开始
        String date = tmp[calendar.get(Calendar.DAY_OF_WEEK)];

        SelectItem selectItem = new SelectItem();
        selectItems.add(selectItem);
        selectItem.orderNum = date + "001";

        selectItem = new SelectItem();
        selectItems.add(selectItem);
        selectItem.orderNum = date + "002";

        selectItem = new SelectItem();
        selectItems.add(selectItem);
        selectItem.orderNum = date + "003";

        selectItem = new SelectItem();
        selectItems.add(selectItem);
        selectItem.orderNum = date + "004";


        listPassway.add(2);//2串1
        listPassway.add(3);//2串1
        listPassway.add(4);//2串1
        return this;
    }

    public OrderItem createTwo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        String[] tmp = new String[]{"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};//
        // 因为Canlendar的日期是从星期天开始,且对应的数值是从1开始
        String date = tmp[calendar.get(Calendar.DAY_OF_WEEK)];

        SelectItem selectItem = new SelectItem();
        selectItems.add(selectItem);
        selectItem.orderNum = date + "002";

        selectItem = new SelectItem();
        selectItems.add(selectItem);
        selectItem.orderNum = date + "003";

        selectItem = new SelectItem();
        selectItems.add(selectItem);
        selectItem.orderNum = date + "004";


        listPassway.add(2);//2串1
        listPassway.add(3);//2串1
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderItem) {
            if (orderId.equals(((OrderItem) obj).orderId)) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "selectItemsSize=" + selectItems.size() +
                ",selectItems=" + selectItems +
                ", listPassway=" + listPassway +
                ", times=" + times +
                ", money=" + money +
                ", statusCode=" + statusCode +
                ", qrCode='" + qrCode + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
