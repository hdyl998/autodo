package com.autodo.mainjob;

import com.autodo.logic.GroupSelectItem;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class GlobalControl {
    private final static GlobalControl instance = new GlobalControl();


    public static GlobalControl getInstance() {
        return instance;
    }

    boolean isStart = true;

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    private GroupSelectItem groupItem;


    public GroupSelectItem getGroupItem() {
        return groupItem;
    }
}
