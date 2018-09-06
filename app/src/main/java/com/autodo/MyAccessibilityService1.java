package com.autodo;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.autodo.tools.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangzn on 17/2/6.
 */
public class MyAccessibilityService1 extends AccessibilityService {


    String description;

    ArrayList<Integer> topList = new ArrayList<>();

    List<AccessibilityNodeInfo> lvs;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {

            //微信UI界面的根节点，开始遍历节点
            AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
            if (rootNodeInfo == null) {
                return;
            }
            description = "";
            if (rootNodeInfo.getContentDescription() != null) {
                description = rootNodeInfo.getContentDescription().toString();
            }

            //自动点赞流程
            if (mUserName.equals("")) {
                //Lv
                lvs = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cn0");
                LogUtils.d("找到的Lv数量: " + lvs.size());
                //如果size不为0，证明当前在朋友圈页面下,开始执行逻辑
                if (lvs.size() != 0) {
                    //1.先记录用户名
                    List<AccessibilityNodeInfo> userNames =
                            rootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/afa");
                    if (userNames.size() != 0) {
                        if (userNames.get(0).getParent() != null && userNames.get(0).getParent().getChildCount() == 4) {
                            mUserName = userNames.get(0).getText().toString();
                            if (!mUserName.equals("") && !ifOnce) {
                                LogUtils.d("初始化，只会执行一次");
                                LogUtils.d("当前的用户名:" + mUserName);
                                ifOnce = true;
                                //测试朋友圈点赞
                                test3(rootNodeInfo);
                            }
                        }
                    }
                } else {
                    ifOnce = false;
                    mUserName = "";
                }

            }


        } catch (Exception e) {
            if (e != null && e.getMessage() != null) {
                LogUtils.d("报错:" + e.getMessage().toString());
            }
        }

    }

    String mUserName = "";
    private boolean ifOnce = false;

    /**
     * com.tencent.mm:id/cn0
     * 朋友圈点赞 (目前实现手动滚动全部点赞)
     * 上方固定显示的名字：com.tencent.mm:id/afa
     * 下方点赞：显示id：com.tencent.mm:id/cnn
     * 每发现一个【评论按钮】，就去搜索当前同父组件下的点赞区域有没有自己的ID。
     * 如果有就不点赞，如果没有就点赞
     * 这里要改成不通过Id抓取提高稳定性
     *
     * @param rootNodeInfo
     */
    private synchronized void test3(AccessibilityNodeInfo rootNodeInfo) {
        LogUtils.d("当前线程:" + Thread.currentThread());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        topList.clear();

        if (!mUserName.equals("")) {

            //测试获得评论按钮的父节点，再反推出点赞按钮
            List<AccessibilityNodeInfo> fuBtns = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/co0");
            LogUtils.d("fuBtns数量：" + fuBtns.size());
            if (fuBtns.size() != 0) {

                //删掉超出屏幕的fuBtn
                AccessibilityNodeInfo lastFuBtn = fuBtns.get(fuBtns.size() - 1);
                Rect lastFuBtnOutBound = new Rect();
                lastFuBtn.getBoundsInScreen(lastFuBtnOutBound);
                if (lastFuBtnOutBound.top > Config.height) {
                    fuBtns.remove(lastFuBtn);
                }

                for (int i = 0; i < fuBtns.size(); i++) {
                    AccessibilityNodeInfo fuBtn = fuBtns.get(i);
                    LogUtils.d("fuBtn的子节点数量:" + fuBtn.getChildCount());//3-4个
                    List<AccessibilityNodeInfo> plBtns = fuBtn.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cj9");
                    LogUtils.d("从这里发现评论按钮:" + plBtns.size());

                    if (plBtns.size() == 0) {
                        if (lvs.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
                            test3(getRootInActiveWindow());
                        }
                        return;
                    }

                    AccessibilityNodeInfo plbtn = plBtns.get(0);    //评论按钮
                    List<AccessibilityNodeInfo> zanBtns = fuBtn.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cnn");
                    LogUtils.d("从这里发现点赞文字显示区域:" + zanBtns.size());
                    if (zanBtns.size() != 0) {
                        //2.如果不为空，则查找有没有自己点过赞，有则不点，没有则点
                        AccessibilityNodeInfo zanbtn = zanBtns.get(0);
                        LogUtils.d("点赞的人是:" + zanbtn.getText().toString());
                        if (zanbtn != null && zanbtn.getText() != null &&
                                zanbtn.getText().toString().contains(mUserName)) {
                            LogUtils.d("*********************这一条已经被赞过辣");
                            //判断是否需要翻页，如果当前所有页面的父节点都没点过了，就需要翻页
                            boolean ifxuyaofanye = false;
                            LogUtils.d("Ｏ(≧口≦)Ｏ: i=" + i + "  fuBtns.size():" + fuBtns.size());
                            if (i == fuBtns.size() - 1) {
                                ifxuyaofanye = true;
                            }
                            if (ifxuyaofanye) {
                                //滑动前检测一下是否还有没有点过的点
                                if (jianceIfLou()) {
                                    LogUtils.d("还有遗漏的点！！！！再检查一遍!!!!!!!!!!");
                                    test3(getRootInActiveWindow());
                                } else {
                                    if (lvs.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
                                        test3(getRootInActiveWindow());
                                        return;
                                    }
                                }
                            }

                        } else {
                            LogUtils.d("**************************:自己没有赞过!");
                            //开始执行点赞流程
                            if (plBtns.size() != 0) {
                                Rect outBounds = new Rect();
                                plbtn.getBoundsInScreen(outBounds);
                                int top = outBounds.top;

                                //根据top判断如果已经点开了就不重复点开了
                                if (topList.contains(top)) {
                                    return;
                                }
                                //com.tencent.mm:id/cj5 赞
                                if (plbtn.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                                    List<AccessibilityNodeInfo> zanlBtns = rootNodeInfo.
                                            findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cj3");
                                    if (zanlBtns.size() != 0) {
                                        if (!topList.contains(top) && zanlBtns.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                                            topList.add(top);
                                            LogUtils.d("topList:" + topList.toString());

                                            //判断是否需要翻页，如果当前所有页面的父节点都没点过了，就需要翻页
                                            boolean ifxuyaofanye = false;
                                            LogUtils.d("Ｏ(≧口≦)Ｏ: i=" + i + "  fuBtns.size():" + fuBtns.size());
                                            if (i == fuBtns.size() - 1) {
                                                ifxuyaofanye = true;
                                            }
                                            if (ifxuyaofanye) {
                                                //滑动前检测一下是否还有没有点过的点
                                                if (jianceIfLou()) {
                                                    LogUtils.d("还有遗漏的点！！！！再检查一遍!!!!!!!!!!");
                                                    test3(getRootInActiveWindow());
                                                } else {
                                                    if (lvs.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
                                                        test3(getRootInActiveWindow());
                                                        return;
                                                    }
                                                }


                                            }

                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        LogUtils.d("**************************:点赞区域为空!plBtns.size() :" + plBtns.size());

                        //开始执行点赞流程
                        if (plBtns.size() != 0) {

                            Rect outBounds = new Rect();
                            plbtn.getBoundsInScreen(outBounds);
                            int top = outBounds.top;

                            //根据top判断如果已经点开了就不重复点开了
                            if (topList.contains(top)) {
                                return;
                            }
                            //com.tencent.mm:id/cj5 赞
                            if (plbtn.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                                List<AccessibilityNodeInfo> zanlBtns = rootNodeInfo.
                                        findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cj3");
                                if (zanlBtns.size() != 0) {
                                    if (!topList.contains(top) && zanlBtns.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                                        topList.add(top);
                                        LogUtils.d("topList:" + topList.toString());

                                        //判断是否需要翻页，如果当前所有页面的父节点都没点过了，就需要翻页
                                        boolean ifxuyaofanye = false;
                                        LogUtils.d("Ｏ(≧口≦)Ｏ: i=" + i + "  fuBtns.size():" + fuBtns.size());
                                        if (i == fuBtns.size() - 1) {
                                            ifxuyaofanye = true;
                                        }
                                        if (ifxuyaofanye) {
                                            //滑动前检测一下是否还有没有点过的点
                                            if (jianceIfLou()) {
                                                LogUtils.d("还有遗漏的点！！！！再检查一遍!!!!!!!!!!");
                                                test3(getRootInActiveWindow());
                                            } else {
                                                if (lvs.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
                                                    test3(getRootInActiveWindow());
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private boolean jianceIfLou() {
        boolean result = false;
        List<AccessibilityNodeInfo> fuBtns =
                getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/co0");
        LogUtils.d("检查的父节点数量:" + fuBtns.size());
        if (fuBtns.size() != 0) {
            for (AccessibilityNodeInfo fuBtn : fuBtns) {
                //点赞区域
                List<AccessibilityNodeInfo> zanBtns = fuBtn.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cnn");
                LogUtils.d("检查的父节点的点赞区域数量:" + zanBtns.size());
                if (zanBtns.size() != 0) {
                    AccessibilityNodeInfo zanbtn = zanBtns.get(0);
                    LogUtils.d(" zanbtn.getText().toString():" + zanbtn.getText().toString());
                    if (zanbtn != null && zanbtn.getText() != null &&
                            zanbtn.getText().toString().contains(mUserName)) {
                        result = false;
                    } else {
                        result = true;
                    }
                } else {
                    result = true;
                }
            }
        }

        return result;
    }


    @Override
    public void onInterrupt() {
        LogUtils.d("onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.packageNames = new String[]{"com.tencent.mm"};
        serviceInfo.notificationTimeout = 100;
        setServiceInfo(serviceInfo);
    }

}
