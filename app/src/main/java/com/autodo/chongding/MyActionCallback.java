//package com.autodo.chongding;
//
//import com.alibaba.fastjson.JSON;
//import com.vise.xsnow.http.callback.ACallback;
//
///**
// * Created by Administrator on 2018/4/15.
// */
//
//public abstract class MyActionCallback<T> extends ACallback<String> {
//    @Override
//    public final void onSuccess(String data) {
//        try {
//            NetEntity entity = JSON.parseObject(data, NetEntity.class);
//            System.out.println(data);
//            if (entity.code == 200) {
//                onSuccessFinal(entity);
//            } else {
//                onFail(entity.code, entity.msg);
//            }
//        } catch (Exception e) {
//            ToastUtils.makeTextAndShow(e.toString());
//            e.printStackTrace();
//        }
//    }
//
//    public abstract void onSuccessFinal( entity);
//
//
//    @Override
//    public void onFail(int errCode, String errMsg) {
//        ToastUtils.makeTextAndShow(errMsg);
//    }
//}
