package com.honglu.future.ui.live.player;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.live.bean.LivePointBean;

import java.util.List;

/**
 * Created by zq on 2018/1/6.
 */

public interface MainPointContract {
    interface View extends BaseView {
        void getLivePointListSuccess(List<LivePointBean> list);

    }

    interface Presenter {
        void getLivePointList(String tbLiveRoomId);

    }
}
