package com.autodo;

import com.autodo.lottery.OrderItem;
import com.autodo.socket.ISocketBase;
import com.autodo.socket.MapCreator;

/**
 * <p>Created by Administrator on 2018/9/10.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class MySocket extends ISocketBase {
    @Override
    protected String setSocketUrl() {
        return "http://112.74.180.32:8090";
    }


    private final static MySocket socket = new MySocket();

    public static MySocket getInstance() {
        return socket;
    }

    public final static String LISTEN_KEY_DATA = "data";
    public final static String LISTEN_KEY_HANDLEREPLY = "handleReply";

    /**
     * 处理这个消息
     *
     * @param item
     */
    public void sendMessage(OrderItem item) {
        sendSockData("handle", MapCreator.create().add("code", item.getStatusCode())
                .add("message", StatusCode.code2String(item.getStatusCode()))
                .add("data", MapCreator.create().add("pid", item.getOrderId()).add("codeStr", item.getQrCode()))
        );
    }
}
