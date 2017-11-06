package com.honglu.future.events;

import com.xulu.mpush.message.RequestMarketMessage;

/**
 * Created by hefei on 2017/8/28
 * 收到行情数据消息的事件
 */

public class ReceiverMarketMessageEvent {
    public RequestMarketMessage marketMessage;
    public ReceiverMarketMessageEvent(RequestMarketMessage marketMessage){
        this.marketMessage = marketMessage;
    }
}
