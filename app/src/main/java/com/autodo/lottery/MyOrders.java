package com.autodo.lottery;

import com.autodo.MySocket;
import com.autodo.tools.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by Administrator on 2018/9/11.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class MyOrders {

    //缓存池
    public final static List<OrderItem> orderItems = new ArrayList<>();

    public final static List<String> handledOrderIds = new ArrayList<>();


    public static List<OrderItem> getOrderItems() {
        return orderItems;
    }

    /***
     * 添加orderItem
     * @param orderItem
     */
    public static void addOrderItem(OrderItem orderItem) {
        //去重处理,已经处理的订单不用处理了
        if (!handledOrderIds.contains(orderItem.getOrderId()) && !orderItems.contains(orderItem)) {
            orderItems.add(orderItem);
            LogUtils.d("新加比赛", orderItem.toString());
        }
    }

//    //TEST
//    {
//        orderItems.add(new OrderItem().createOne());
//        orderItems.add(new OrderItem().createTwo());
//    }

    /***
     * 获取第一条
     * @return
     */
    public static OrderItem getFirstOrderItem() {
        if (orderItems.isEmpty()) {
            return null;
        }
        return orderItems.get(0);
    }

    /***
     * 结束处理订单
     * @param orderItem
     * @param statusCode
     */
    public static void endHandleOrderItem(OrderItem orderItem, int statusCode) {
        //设置状态码
        orderItem.setStatusCode(statusCode);
        handledOrderIds.add(orderItem.getOrderId());
        MySocket.getInstance().sendMessage(orderItem);
        MyOrders.getOrderItems().remove(orderItem);
        //避免内存过大
        if (handledOrderIds.size() > 1000) {
            handledOrderIds.clear();
        }
        LogUtils.d("处理结果", orderItem.toString());
    }

}
