package com.honglu.future.bean;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.honglu.future.app.App;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.SpUtil;

import java.io.Serializable;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

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

    public static void postMaiDian(MaidianBean maidianBean){
        if (maidianBean==null){
            return;
        }
        Gson gson = new Gson();
        String route= gson.toJson(maidianBean);//通过Gson将Bean转化为Json字符串形式
        Log.d("MaidianBean", "initPresenter: route--->"+route);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"),route);
        HttpManager.getApi().postMaiDian(body).subscribe(new Subscriber<JsonNull>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JsonNull jsonNull) {

            }
        });
    }
}
