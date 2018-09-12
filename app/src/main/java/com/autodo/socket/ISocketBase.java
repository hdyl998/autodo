package com.autodo.socket;

import android.os.Handler;

import com.autodo.tools.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * socket的基类，已将数据回调发送的主线程处理
 * create by liugd
 * socket消息订阅的基类
 */
public abstract class ISocketBase {
    protected Handler mHandler = new Handler();

    private Socket mSocket;// socket通信对象
    private final String LOG_TAG = getClass().getSimpleName();

    public Handler getHandler() {
        return mHandler;
    }

    /***
     * 设置socket的URL
     *
     * @return
     */
    protected abstract String setSocketUrl();

    /***
     * socket是否连接成功
     *
     * @return
     */
    public boolean isConnectSuccess() {
        if (mSocket != null && mSocket.connected()) {
            return true;
        }
        return false;
    }


    //向socket发送消息
    public void sendSocketMessage(String event, String message) {
        if (mSocket != null) {
            LogUtils.print("sendSocketMessage", event + ":" + message);
            mSocket.emit(event, message);
        }
    }

    /***
     *发送JSON消息
     * @param order 命令
     * @param creator 参数构建器
     */
    public void sendSockData(String order, MapCreator creator) {
        sendSocketMessage(order, creator.toString());
    }


    /***
     * 发送JSON消息
     * @param order 命令
     */
    public void sendSockData(String order) {
        sendSockData(order, MapCreator.create());
    }

    /**
     * 停止Socket
     */
    public final void stopSocket() {
        if (mSocket == null)
            return;
        mSocket.disconnect();// 先断开连接
        mSocket.off();
        mSocket = null;
        removeAllListener();
        LogUtils.print(LOG_TAG, "停止Socket" + getClass().getSimpleName());
    }


    /***
     * 初始化socket的配置
     */
    private void initSocketOpinion() {
        try {
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket", "flashsocket", "htmlfile", "xhr-multipart", "polling-xhr", "jsonp-polling"};
            options.reconnection = true;
            options.forceNew = true;
            options.upgrade = true;
            String url = setSocketUrl();
            int indexSymbol = url.lastIndexOf("/");
            if (indexSymbol > 7) {
                options.path = String.format("/%s/socket.io", url.substring(indexSymbol + 1));
                url = url.substring(0, indexSymbol);
            }
            mSocket = IO.socket(url, options);
            LogUtils.print(LOG_TAG + "地址", setSocketUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 销毁
     */
    public void onDestory() {
        stopSocket();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initLocalListener() {
        //定制本地默认协议
        String[] defaultDefines = {Socket.EVENT_CONNECT, Socket.EVENT_CONNECT_ERROR, Socket.EVENT_CONNECT_TIMEOUT, Socket.EVENT_DISCONNECT};
        for (String event : defaultDefines) {
            mSocket.on(event, new MyLocalEmitterListener(event));
        }
    }

    private void initServerLinstener() {
        //定制子类的协议
        List<String> userDefines = getAllPublicStaticFiledsValues(getClass());
        for (String string : userDefines) {
            mSocket.on(string, new MyServerEmitterListener(string));
        }
    }


    /***
     * 开启Socket通信
     */
    public void startSocket() {
        if (isConnectSuccess()) {
            LogUtils.print(LOG_TAG, "startSocket 已连接成功，无需再连接");
            return;
        }
        LogUtils.print(LOG_TAG, "startSocket 开始连接");
        stopSocket();
        if (mSocket == null) {
            initSocketOpinion();
        }
        initLocalListener();
        initServerLinstener();
        mSocket.connect();
    }


    protected List<SocketMessageListener> listeners;

    public void addOnGetSocketDataListener(SocketMessageListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>(3);//一般三个容量就足够了
        }
        if (listener != null && !this.listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnGetSocketDataListener(SocketMessageListener listener) {
        if (listeners != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void removeAllListener() {
        if (listeners != null) {
            listeners.clear();
            listeners = null;
        }
    }

    private class MyLocalEmitterListener implements Emitter.Listener {
        private String event;

        public MyLocalEmitterListener(String event) {
            this.event = event;
        }

        @Override
        public void call(Object... args) {
            LogUtils.print("Socket消息 -local-", event + "/// " + listeners);
            mHandler.post(() -> {
                try {
                    if (listeners != null) {
                        for (SocketMessageListener listener : listeners) {
                            listener.onLocalMessage(event);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private class MyServerEmitterListener implements Emitter.Listener {
        private String event;

        public MyServerEmitterListener(String event) {
            this.event = event;
        }

        @Override
        public void call(Object... args) {
            String backData;
            if (args != null && args.length > 0) {
                backData = args[0].toString();
            } else {
                backData = "";
            }
            LogUtils.print("socket消息 -server-", event + " " + backData);
            mHandler.post(() -> {
                if (listeners != null) {
                    for (SocketMessageListener listener : listeners) {
                        try {
                            listener.onServerMessage(event, backData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /***
     * 通过反射得到本类所有的公有静态字段(不包括父类)
     * 所表示的所有的协议
     *
     * @param clazz 类名
     * @return
     */
    public static List<String> getAllPublicStaticFiledsValues(Class clazz) {
        List<String> listString = new ArrayList<>();
        Field fields[] = clazz.getDeclaredFields();//获得本类所有的静态公有字段(不包括父类)
        try {
            for (int i = 0; i < fields.length; i++) {
                int modifiers = fields[i].getModifiers();
                if (fields[i].getType() == String.class && Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {//静态公有字段并且是String类型
                    listString.add(fields[i].get(null) + "");
                }
            }
        } catch (Exception e) {
        }
        return listString;
    }
}