package com.honglu.future.http;

import com.google.gson.JsonNull;
import com.honglu.future.bean.BaseResponse;
import com.honglu.future.ui.details.bean.ConsultDetailsBean;
import com.honglu.future.ui.home.bean.BannerData;
import com.honglu.future.ui.home.bean.HomeIcon;
import com.honglu.future.ui.home.bean.HomeMarketCodeBean;
import com.honglu.future.ui.home.bean.HomeMessageItem;
import com.honglu.future.ui.home.bean.MarketData;
import com.honglu.future.ui.home.bean.NewsFlashData;
import com.honglu.future.ui.main.bean.AuditedBean;
import com.honglu.future.ui.register.bean.RegisterBean;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.ClosePositionListBean;
import com.honglu.future.ui.trade.bean.ConfirmBean;
import com.honglu.future.ui.trade.bean.EntrustBean;
import com.honglu.future.ui.trade.bean.HoldDetailBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;


/**
 *
 */
public interface HttpApi {

    //所有需要的泛型 添加：BaseResponse<UserInfo>
    //用户注册
    @FormUrlEncoded
    @POST("futures-mobile-api/user/info/register")
    Observable<BaseResponse<RegisterBean>> register(@Field("code") String code,
                                                    @Field("sourceId") String sourceId,
                                                    @Field("mobileNum") String mobileNum,
                                                    @Field("password") String password,
                                                    @Field("nickName") String nickName);

    //用户注册发送验证码
    @FormUrlEncoded
    @POST("futures-mobile-api/user/info/register/send/sms")
    Observable<BaseResponse> getCode(@Field("sourceId") String sourceId,
                                     @Field("mobileNum") String mobileNum);

    //找回密码(重置密码)发送验证码
    @FormUrlEncoded
    @POST("futures-mobile-api/user/info/retrieve/password/send/sms")
    Observable<BaseResponse> getResetCode(@Field("sourceId") String sourceId,
                                          @Field("mobileNum") String mobileNum);

    //找回密码
    @FormUrlEncoded
    @POST("futures-mobile-api/user/info/retrieve/password")
    Observable<BaseResponse> resetPwd(@Field("mobileNum") String mobileNum,
                                      @Field("password") String password,
                                      @Field("code") String code);

    //用户登录
    @FormUrlEncoded
    @POST("futures-mobile-api/user/info/login")
    Observable<BaseResponse<UserInfoBean>> login(@Field("mobileNum") String mobileNum,
                                                 @Field("password") String password);

    //修改昵称
    @FormUrlEncoded
    @POST("futures-mobile-api/user/info/update/nickName")
    Observable<BaseResponse> updateNickName(@Field("nickName") String nickName,
                                            @Field("userId") String userId);

    //交易所用户登录
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/user/login")
    Observable<BaseResponse<AccountBean>> loginAccount(@Field("account") String account,
                                                       @Field("password") String password,
                                                       @Field("userId") String userId,
                                                       @Field("company") String company);

    //获取用户账户基本信息
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/user/account/info")
    Observable<BaseResponse<AccountInfoBean>> getAccountInfo(@Field("userId") String userId,
                                                             @Field("token") String token,
                                                             @Field("company") String company);

    //获取用户结算单
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/user/settlement/info")
    Observable<BaseResponse<SettlementInfoBean>> querySettlementInfo(@Field("userId") String userId,
                                                                     @Field("token") String token,
                                                                     @Field("company") String company);

    //根据日期获取用户结算单
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/user/settlement/info/select")
    Observable<BaseResponse<SettlementInfoBean>> querySettlementInfoByDate(@Field("userId") String userId,
                                                                           @Field("token") String token,
                                                                           @Field("company") String company,
                                                                           @Field("day") String day);

    //结算单确认
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/user/settlement/info/confirm")
    Observable<BaseResponse<ConfirmBean>> settlementConfirm(@Field("userId") String userId,
                                                            @Field("token") String token);

    //获取产品列表
    @POST("futures-mobile-api/app/future/exchange/trade/product/list")
    Observable<BaseResponse<List<ProductListBean>>> getProductList();

    //查询持仓列表
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/holdPosition/list")
    Observable<BaseResponse<List<HoldPositionBean>>> getHoldPositionList(@Field("userId") String userId,
                                                                         @Field("token") String token,
                                                                         @Field("company") String company);

    //查询持仓列表明细
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/holdPosition/detail")
    Observable<BaseResponse<List<HoldDetailBean>>> getHoldPositionDetail(@Field("instrumentId") String instrumentId,
                                                                         @Field("type") String type,
                                                                         @Field("todayPosition") String todayPosition,
                                                                         @Field("userId") String userId,
                                                                         @Field("token") String token);

    //查询委托中列表
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/entrust/list")
    Observable<BaseResponse<List<EntrustBean>>> getEntrustList(@Field("userId") String userId,
                                                               @Field("token") String token);

    //委托撤单
    @FormUrlEncoded
    @POST("/futures-mobile-api/app/future/exchange/trade/cancel/order")
    Observable<BaseResponse> cancelOrder(@Field("orderRef") String orderRef,
                                         @Field("instrumentId") String instrumentId,
                                         @Field("sessionId") String sessionId,
                                         @Field("frontId") String frontId,
                                         @Field("userId") String userId,
                                         @Field("token") String token);

    //查询已平仓列表
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/orders/history/close/list")
    Observable<BaseResponse<List<ClosePositionListBean>>> getCloseList(@Field("dayStart") String dayStart,
                                                                       @Field("dayEnd") String dayEnd,
                                                                       @Field("userId") String userId,
                                                                       @Field("token") String token,
                                                                       @Field("startTime") String startTime,
                                                                       @Field("endTime") String endTime);

    //委托建仓
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/create/order/v2")
    Observable<BaseResponse> buildTransaction(@Field("orderNumber") String orderNumber,
                                              @Field("type") String type,
                                              @Field("price") String price,
                                              @Field("instrumentId") String instrumentId,
                                              @Field("userId") String userId,
                                              @Field("token") String token,
                                              @Field("company") String company);

    //委托平仓
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/close/order/v2")
    Observable<BaseResponse> closeOrder(@Field("todayPosition") String todayPosition,
                                        @Field("userId") String userId,
                                        @Field("token") String token,
                                        @Field("orderNumber") String orderNumber,
                                        @Field("type") String type,
                                        @Field("price") String price,
                                        @Field("instrumentId") String instrumentId,
                                        @Field("holdAvgPrice") String holdAvgPrice,
                                        @Field("company") String company
    );


    /*******************************    上传图片   *****************************************/
    /*******************************
     * 上传图片
     *****************************************/
    @Multipart
    @POST("futures-mobile-api/user/info/update/avatar")
    Observable<BaseResponse> uploadUserAvatar(
            @Part MultipartBody.Part File, @Part("userId") RequestBody userId);

    //首页banner
    @POST("http://192.168.90.130:8080/futures-mobile-api/appBanner/loadBannerInfo")
    Observable<BaseResponse<List<BannerData>>> getBannerData();

    //首页市场情codes
    @GET("http://192.168.85.126:8081/futures-mobile-api/news/api/startup/init/v3")
    Observable<BaseResponse<HomeMarketCodeBean>> getMarketCodesData(@Query("sourceId") Integer account,
                                                                    @Query("versionNo") String version,
                                                                    @Query("userId") String userId);
    //首页市场行情List
    @GET("http://192.168.85.126:8083/futures-data-mobile/quotation/realTime")
    Observable<BaseResponse<MarketData>> getMarketCodesData(
            @Query(value = "codes" ,encoded = true) String codes,@Query("deviceType") int deviceType
    );

    //首页icon
    @GET("credit-user/register")
    Observable<BaseResponse<HomeIcon>> getHomeIcon();

    //首页新闻
    @POST("http://192.168.90.139:8080/futures-mobile-api/app/information/informationList")
    Observable<BaseResponse<List<HomeMessageItem>>> getNewsColumnData();

    //首页24小时
    @POST("http://192.168.90.130:8080/futures-mobile-api/app/index/newsList")
    Observable<BaseResponse<List<NewsFlashData>>> geFlashNewData(@Query("pageIndex") int page, @Query("pageSize") int pageSize);

    //渠道是否过审  http://192.168.90.130:8080/
    @FormUrlEncoded
    @POST("futures-mobile-api/appChannel/isAudited")
    Observable<BaseResponse<AuditedBean>> getAudited(@Query("appType") int appType,
                                                     @Field("marketCode") String marketCode
            , @Field("versionNumber") String versionNumber);

    //修改资金密码接口 测试环境：

    /**
     * 开发环境： http://192.168.90.162:8080/mobileApi/app/future/exchange/user/update/trade/password
     * 测试环境： http://192.168.85.126:8081/futures-mobile-api/app/future/exchange/user/update/trade/password
     * 待调试
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/user/update/trade/password")
    Observable<BaseResponse<JsonNull>> resetAssesPwd(@Field("account") String account,
                                                     @Field("oldPassword") String oldPassword,
                                                     @Field("token") String token,
                                                     @Field("newPassword") String newPassword,
                                                     @Field("userId") String userId);

    /**
     * 开发环境： http://192.168.90.162:8080/mobileApi/app/future/exchange/user/update/password
     * 测试环境： http://192.168.85.126:8081/futures-mobile-api/app/future/exchange/user/update/password
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/user/update/password")
    Observable<BaseResponse<JsonNull>> resetMarketPwd(@Field("account") String account,
                                                      @Field("oldPassword") String oldPassword,
                                                      @Field("token") String token,
                                                      @Field("newPassword") String newPassword,
                                                      @Field("userId") String userId);
    /**
     * https://www.showdoc.cc/1673161?page_id=15533135
     *
     * @return
     */
    @FormUrlEncoded
    @POST("http://192.168.90.139:8080/futures-mobile-api/app/information/informationDetail")
    Observable<BaseResponse<ConsultDetailsBean>> getMessageData(@Field("informationId") String informationId);

    /**
     * https://www.showdoc.cc/1673161?page_id=15533135
     *
     * @return
     */
    @FormUrlEncoded
    @POST("http://192.168.90.139:8080/futures-mobile-api/app/information/informationPraise")
    Observable<BaseResponse<List<String>>> praiseMessage(@Field("informationId") String informationId,
                                                                @Field("userId") String userID);
}
