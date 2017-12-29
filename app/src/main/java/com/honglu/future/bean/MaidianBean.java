package com.honglu.future.bean;

import com.honglu.future.app.App;
import com.honglu.future.config.Constant;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by hefei on 2017/12/29
 *
 * 埋点的数据结构
 *
 *     {
 "uuid":"7uhajkduqwo398xjdjakdkDGf",
 "product_name":"qihuo",
 "message_id":"200010",
 "user_id":"13918405190",
 "page_name":"激活",
 "even_name":"激活",
 "data":{
 "buriedName":"激活",
 "buriedRemark":"首次下载并打开APP的用户    注册日期相同的首次下载并打开APP的用户人数合计",
 "clickNum": 10,
 "deviceId" :"TYUIOYUIKL" //设备编号
 "key": "jihuo_shoucidakai",
 "userId" : "JLSDFKDJFLSDFKJSLDF"
 }
 }
 */

public class MaidianBean implements Serializable{
    public String uuid = UUID.randomUUID().toString();
    public String product_name = "qihuo";
    public String message_id = "200010";
    public String user_id = SpUtil.getString(Constant.CACHE_TAG_UID);
    public String page_name;
    public String even_name;
    public Data data;

   public class Data{
        public String buriedName;
        public int clickNum;
        public String buriedRemark;
        public String deviceId = AndroidUtil.getIMEI(App.getContext());
        public String key;
        public String mobile = SpUtil.getString(Constant.CACHE_TAG_MOBILE);

    }
}
