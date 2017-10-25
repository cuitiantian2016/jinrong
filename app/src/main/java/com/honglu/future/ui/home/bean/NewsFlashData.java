package com.honglu.future.ui.home.bean;

import android.text.TextUtils;
import android.util.Log;


import java.util.List;

/**
 * Created by hefei on 2017/2/17.
 *
 * 24小时快讯的接口
 * https://www.showdoc.cc/12403?page_id=15306492
 */

public class NewsFlashData{
   public NewsFlashDataX data;

  public static   class NewsFlashDataX{
      public List<NewsFlashBean> data;
      public static   class NewsFlashBean{
          public int actType;//"1 普通文字、图片  2 标红文字  "
          public String time;//时间
          public String month;//月份
          public String day;//天
          public String content;//内容
          public String img;//图片
          public String date;//年月日
          /**
           * 判断当前的item的颜色
           * @return
           */
          public boolean isRedText(){
              return actType == 2;
          }

          public String getTime() {
              if (!TextUtils.isEmpty(time)){
                  if (time.length()>=8){
                      time = time.substring(0,time.length()-3);
                      Log.d("xx", "getTime: "+time);
                  }
              }
              return time;
          }

          public String getMonth() {
              return month;
          }

          public String getDay() {
              return day;
          }

          public String getContent() {
              return content;
          }

          public String getImg() {
              return img;
          }

          public String getDate() {
              return date;
          }
      }
    }

}
