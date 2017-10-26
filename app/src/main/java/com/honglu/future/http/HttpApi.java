package com.honglu.future.http;

import com.honglu.future.bean.BaseResponse;
import com.honglu.future.ui.home.bean.BannerData;
import com.honglu.future.ui.home.bean.HomeIcon;
import com.honglu.future.ui.home.bean.HomeMessageItem;
import com.honglu.future.ui.home.bean.MarketData;
import com.honglu.future.ui.home.bean.NewsFlashData;
import com.honglu.future.ui.register.bean.RegisterBean;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;

import java.util.Map;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;


/**
 *
 */
public interface HttpApi {

    //所有需要的泛型 添加：BaseResponse<UserInfo>
    //用户注册
    @FormUrlEncoded
    @POST("mobileApi/user/info/register")
    Observable<BaseResponse<RegisterBean>> register(@Field("code") String code,
                                                    @Field("sourceId") String sourceId,
                                                    @Field("mobileNum") String mobileNum,
                                                    @Field("password") String password,
                                                    @Field("nickName") String nickName);

    //用户注册发送验证码
    @FormUrlEncoded
    @POST("mobileApi/user/info/register/send/sms")
    Observable<BaseResponse> getCode(@Field("sourceId") String sourceId,
                                     @Field("mobileNum") String mobileNum);

    //找回密码(重置密码)发送验证码
    @FormUrlEncoded
    @POST("mobileApi/user/info/retrieve/password/send/sms")
    Observable<BaseResponse> getResetCode(@Field("sourceId") String sourceId,
                                          @Field("mobileNum") String mobileNum);

    //找回密码
    @FormUrlEncoded
    @POST("mobileApi/user/info/retrieve/password")
    Observable<BaseResponse> resetPwd(@Field("mobileNum") String mobileNum,
                                      @Field("password") String password,
                                      @Field("code") String code);

    //用户登录
    @FormUrlEncoded
    @POST("mobileApi/user/info/login")
    Observable<BaseResponse<UserInfoBean>> login(@Field("mobileNum") String mobileNum,
                                                 @Field("password") String password);

    //修改昵称
    @FormUrlEncoded
    @POST("mobileApi/user/info/update/nickName")
    Observable<BaseResponse> updateNickName(@Field("nickName") String nickName,
                                      @Field("userId") String userId);

    /*******************************    上传图片   *****************************************/
    @Multipart
    @POST("mobileApi/user/info/update/avatar")
    Observable<BaseResponse> uploadUserAvatar(
            @Part MultipartBody.Part file , @Part("userId") RequestBody userId);

    //首页banner
    @GET("credit-user/register")
    Observable<BaseResponse<BannerData>> getBannerData();

    //首页市场行情
    @GET("credit-user/register")
    Observable<BaseResponse<MarketData>> getMarketData();

    //首页icon
    @GET("credit-user/register")
    Observable<BaseResponse<HomeIcon>> getHomeIcon();

    //首页新闻
    @GET("credit-user/register")
    Observable<BaseResponse<HomeMessageItem>> getNewsColumnData();

    //首页24小时
    @GET("credit-user/register")
    Observable<BaseResponse<NewsFlashData>> geFlashNewData(@Query("page") int page);

}
