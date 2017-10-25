package com.honglu.future.events;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhuaibing on 2017/8/28
 */

public class HomeNotifyRefreshEvent {
    public int type ;
    public final static int TYPE_REFRESH_FINISH = 0;
    public final static int TYPE_LOAD_MORE_FINISH = 1;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_REFRESH_FINISH,
            TYPE_LOAD_MORE_FINISH,
    })
    public @interface refreshType{}
    public HomeNotifyRefreshEvent(@refreshType int type){
        this.type = type;
    }
    /**
     * 加载更多完成
     * @return
     */
    public boolean isLoadMore(){
        return type == TYPE_LOAD_MORE_FINISH;
    }
    /**
     * 刷新完成
     * @return
     */
    public boolean isRefresh(){
        return type == TYPE_REFRESH_FINISH;
    }

}
