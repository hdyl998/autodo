package com.autodo.alipaycapture;

import android.support.annotation.NonNull;

/**
 * <p>Created by Administrator on 2018/9/17.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class PayInfoItem implements Comparable<PayInfoItem> {
    public String money;
    public String url;

    @Override
    public int compareTo(@NonNull PayInfoItem o) {
        return this.money.compareTo(o.money);
    }
}
