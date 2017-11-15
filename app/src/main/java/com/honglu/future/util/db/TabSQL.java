package com.honglu.future.util.db;

/**
 * 创建表SQL类
 *
 * @author Administrator
 */
public class TabSQL {

    /**
     * 极光推送消息表
     */
    public static final String JPUSH_TABLE = "create table jpush_table("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT," // 主键
            + "id TEXT  NOT NULL," // 消息ID
            + "phone TEXT  NOT NULL," // 手机号
            + "tag TEXT  NOT NULL," // 未读标志0未读,1已读
            + "message TEXT  NOT NULL" // 消息
            + ")"; // 时间


}
