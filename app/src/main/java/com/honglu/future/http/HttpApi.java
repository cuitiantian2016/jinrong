package com.honglu.future.http;

import com.honglu.future.bean.BaseResponse;
import com.honglu.future.ui.home.bean.BannerData;
import com.honglu.future.ui.register.bean.RegisterBean;

import java.util.Map;
import java.util.Objects;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;


/**
 *
 */
public interface HttpApi {

    //所有需要的泛型 添加：BaseResponse<UserInfo>

    //上传用户信息
    @FormUrlEncoded
    @POST("credit-app/device-report")
    Observable<BaseResponse> deviceReport(@Field("device_id") String device_id,
                                          @Field("installed_time") String installed_time,
                                          @Field("uid") String uid,
                                          @Field("username") String username,
                                          @Field("net_type") String net_type,
                                          @Field("identifyID") String identifyID,
                                          @Field("appMarket") String appMarket);

    //注册
    @FormUrlEncoded
    @POST("credit-user/register")
    Observable<BaseResponse<RegisterBean>> register(@Field("phone") String phone,
                                                    @Field("code") String code,
                                                    @Field("password") String password,
                                                    @Field("source") String source,
                                                    @Field("invite_code") String invite_code,
                                                    @Field("user_from") String user_from,
                                                    @Field("captcha") String captcha);

    //
    @FormUrlEncoded
    @GET("credit-user/register")
    Observable<BaseResponse<BannerData>> getBannerData();

}
