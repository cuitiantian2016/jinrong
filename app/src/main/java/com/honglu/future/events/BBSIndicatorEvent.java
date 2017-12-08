package com.honglu.future.events;

/**
 * 用于切换到指定BBS分类入口
 */
public class BBSIndicatorEvent {

    boolean isBackTop = false;

    String topicType;

    public BBSIndicatorEvent(String topicType) {
        this.topicType = topicType;
    }

    public BBSIndicatorEvent(String topicType, boolean isBackTop) {
        this.isBackTop = isBackTop;
        this.topicType = topicType;
    }

    public String getTopicType() {
        return topicType;
    }

    public boolean isBackTop() {
        return isBackTop;
    }
}
