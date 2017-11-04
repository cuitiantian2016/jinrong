package com.honglu.future.ui.home.bean;


/**
 * Banner实体类
 *
 * {
 "serialNum": "0",
 "pic": "http://qihuo-test.oss-cn-shanghai.aliyuncs.com/information/2017-10-31/1509438797804-20171031163317.jpg",
 "beginTime": "2017-10-31",
 "endTime": "2017-11-11",
 "informationColumnId": "1",
 "url": "http://stgnew.xnsudai.com/operateHd/application/view/activity/fallPackets.html"
 }
 */
public class BannerData  {

  public String pic;
  public String informationColumnId;
  public String url;
  public String beginTime;
  public String endTime;

  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }

  public String getInformationColumnId() {
    return informationColumnId;
  }

  public void setInformationColumnId(String informationColumnId) {
    this.informationColumnId = informationColumnId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(String beginTime) {
    this.beginTime = beginTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
}
