package com.honglu.future.http;

import com.google.gson.JsonNull;
import com.honglu.future.bean.ActivityPopupBean;
import com.honglu.future.bean.BaseResponse;
import com.honglu.future.bean.CheckAccountBean;
import com.honglu.future.bean.MaidianReturn;
import com.honglu.future.bean.UpdateBean;
import com.honglu.future.ui.circle.bean.ArewardListBean;
import com.honglu.future.ui.circle.bean.AttentionBean;
import com.honglu.future.ui.circle.bean.BBS;
import com.honglu.future.ui.circle.bean.CircleDetailBean;
import com.honglu.future.ui.circle.bean.CircleMineBean;
import com.honglu.future.ui.circle.bean.CommentAllBean;
import com.honglu.future.ui.circle.bean.CommentBean;
import com.honglu.future.ui.circle.bean.CommentBosAllBean;
import com.honglu.future.ui.circle.bean.SignBean;
import com.honglu.future.ui.circle.bean.TopicFilter;
import com.honglu.future.ui.details.bean.ConsultDetailsBean;
import com.honglu.future.ui.details.bean.InformationCommentBean;
import com.honglu.future.ui.home.bean.BannerData;
import com.honglu.future.ui.home.bean.HomeIcon;
import com.honglu.future.ui.home.bean.HomeMarketCodeBean;
import com.honglu.future.ui.home.bean.HomeMessageItem;
import com.honglu.future.ui.home.bean.MarketData;
import com.honglu.future.ui.home.bean.NewsFlashData;
import com.honglu.future.ui.live.bean.LiveListBean;
import com.honglu.future.ui.main.bean.AuditedBean;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.msg.bean.CircleMsgBean;
import com.honglu.future.ui.msg.bean.HasUnreadMsgBean;
import com.honglu.future.ui.msg.bean.PraiseMsgListBean;
import com.honglu.future.ui.msg.bean.SystemMsgBean;
import com.honglu.future.ui.msg.bean.TradeMsgBean;
import com.honglu.future.ui.recharge.bean.AssesData;
import com.honglu.future.ui.recharge.bean.RechangeDetailData;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.CloseBuiderBean;
import com.honglu.future.ui.trade.bean.ConfirmBean;
import com.honglu.future.ui.trade.bean.EntrustBean;
import com.honglu.future.ui.trade.bean.HistoryBuiderPositionBean;
import com.honglu.future.ui.trade.bean.HistoryClosePositionBean;
import com.honglu.future.ui.trade.bean.HistoryMissPositionBean;
import com.honglu.future.ui.trade.bean.HistoryTradeBean;
import com.honglu.future.ui.trade.bean.HoldDetailBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.KLineBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.RealTimeBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.bean.TickChartBean;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.ui.usercenter.bean.BindCardBean;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
    Observable<BaseResponse<UserInfoBean>> register(@Field("code") String code,
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

    //检查用户是否填过
    @FormUrlEncoded
    @POST("futures-system-web-api/futures/account/check")
    Observable<BaseResponse<CheckAccountBean>> accountCheck(@Field("userId") String userId);

    //期货账户退出
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/user/login/out")
    Observable<BaseResponse> accountLogout(@Field("userId") String userId,
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
                                                            @Field("token") String token,
                                                            @Field("company") String company);

    //获取产品列表
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/product/list")
    Observable<BaseResponse<List<ProductListBean>>> getProductList(@Field("company") String company);

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
                                                                         @Field("token") String token,
                                                                         @Field("company") String company);

    //查询委托中列表
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/entrust/list")
    Observable<BaseResponse<List<EntrustBean>>> getEntrustList(@Field("userId") String userId,
                                                               @Field("token") String token,
                                                               @Field("company") String company);

    //委托撤单
    @FormUrlEncoded
    @POST("/futures-mobile-api/app/future/exchange/trade/cancel/order")
    Observable<BaseResponse> cancelOrder(@Field("orderRef") String orderRef,
                                         @Field("instrumentId") String instrumentId,
                                         @Field("sessionId") String sessionId,
                                         @Field("frontId") String frontId,
                                         @Field("userId") String userId,
                                         @Field("token") String token,
                                         @Field("company") String company);

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

    //快速建仓
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/create/order")
    Observable<BaseResponse> fastTransaction(@Field("orderNumber") String orderNumber,
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
                                        @Field("company") String company);

    //快速平仓
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/close/order")
    Observable<BaseResponse> ksCloseOrder(@Field("todayPosition") String todayPosition,
                                          @Field("userId") String userId,
                                          @Field("token") String token,
                                          @Field("orderNumber") String orderNumber,
                                          @Field("type") String type,
                                          @Field("price") String price,
                                          @Field("instrumentId") String instrumentId,
                                          @Field("holdAvgPrice") String holdAvgPrice,
                                          @Field("company") String company);


    //获取产品详情
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/trade/product/detail")
    Observable<BaseResponse<ProductListBean>> getProductDetail(@Field("instrumentId") String instrumentId,
                                                               @Field("company") String company);

    //行情
    //http://192.168.85.126:8083/futures-data-mobile/quotation/realTime/main?deviceType=2
    @POST("futures-data-mobile/quotation/realTime/main")
    Observable<BaseResponse<MarketnalysisBean>> getMarketData();

    //获取k线行情
    @GET("futures-data-mobile/quotation/kChart")
    Observable<BaseResponse<KLineBean>> getKLineData(@Query("excode") String excode,
                                                     @Query("code") String code,
                                                     @Query("type") String type);

    //获取分时图数据
    @GET("futures-data-mobile/quotation/tickChart")
    Observable<BaseResponse<TickChartBean>> getTickData(@Query("excode") String excode,
                                                        @Query("code") String code);

    //查询商品行情详情
    @GET("futures-data-mobile/quotation/realTime")
    Observable<BaseResponse<RealTimeBean>> getProductRealTime(@Query(value = "codes", encoded = true) String codes);

    //app版本更新查询接口
    @FormUrlEncoded
    @POST("futures-mobile-api/appVer/queryAppVersion")
    Observable<BaseResponse<UpdateBean>> getUpdateVersion(@Field("appType") String appType,
                                                          @Field("versionNumber") String versionNumber);

    //app活动弹窗
    @GET("futures-mobile-api/appBanner/loadAppPopupWin.do")
    Observable<BaseResponse<ActivityPopupBean>> loadAppPopupWin(@Query("clientType") String clientType,
                                                                @Query("currentVersion") String currentVersion,
                                                                @Query("phone") String phone);


    /*******************************    上传图片   *****************************************/
    /*******************************
     * 上传图片
     *****************************************/
    @Multipart
    @POST("futures-mobile-api/user/info/update/avatar")
    Observable<BaseResponse> uploadUserAvatar(
            @Part MultipartBody.Part File, @Part("userId") RequestBody userId);

    //首页banner
    @POST("futures-mobile-api/appBanner/loadBannerInfo")
    Observable<BaseResponse<List<BannerData>>> getBannerData();

    //首页市场情codes
    @GET("futures-mobile-api/news/api/startup/init/v3")
    Observable<BaseResponse<HomeMarketCodeBean>> getMarketCodesData(@Query("sourceId") Integer account,
                                                                    @Query("versionNo") String version,
                                                                    @Query("userId") String userId);

    //首页市场行情List
    @GET("futures-data-mobile/quotation/realTime")
    Observable<BaseResponse<MarketData>> getMarketCodesData(
            @Query(value = "codes", encoded = true) String codes, @Query("deviceType") int deviceType
    );

    //首页icon
    @POST("futures-mobile-api/app/homeIcon/iconList")
    Observable<BaseResponse<List<HomeIcon>>> getHomeIcon();

    //首页新闻
    @FormUrlEncoded
    @POST("futures-mobile-api/app/information/informationList")
    Observable<BaseResponse<List<HomeMessageItem>>> getNewsColumnData(@Field("userId") String userId);

    //首页24小时
    @POST("futures-mobile-api/app/index/newsList")
    Observable<BaseResponse<List<NewsFlashData>>> geFlashNewData(@Query("pageIndex") int page, @Query("pageSize") int pageSize);

    //渠道是否过审  http://192.168.90.130:8080/
    @FormUrlEncoded
    @POST("futures-mobile-api/appChannel/isAudited")
    Observable<BaseResponse<AuditedBean>> getAudited(@Query("appType") int appType,
                                                     @Field("marketCode") String marketCode
            , @Field("versionNumber") String versionNumber);

    //银行卡列表
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/transfer/bank/list")
    Observable<BaseResponse<List<BindCardBean>>> geBindCardData(@Field("userId") String userId,
                                                                @Field("token") String token,
                                                                @Field("company") String company);

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
                                                     @Field("userId") String userId,
                                                     @Field("company") String company);

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
                                                      @Field("flag") int flag,
                                                      @Field("userId") String userId,
                                                      @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15533135
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/information/informationDetail")
    Observable<BaseResponse<ConsultDetailsBean>> getMessageData(@Field("informationId") String informationId);

    /**
     * https://www.showdoc.cc/1673161?page_id=15533135
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/information/informationComment")
    Observable<BaseResponse<List<InformationCommentBean>>> getInformationComment(@Field("informationId") String informationId);

    /**
     * https://www.showdoc.cc/1673161?page_id=15533135
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/information/informationReply")
    Observable<BaseResponse<JsonNull>> getInformationReply(
            @Field("informationId") String informationId,
            @Field("userId") String userId,
            @Field("postManId") String postManId,
            @Field("commentContent") String commentContent);

    /**
     * https://www.showdoc.cc/1673161?page_id=15533135
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/information/informationPraise")
    Observable<BaseResponse<List<String>>> praiseMessage(@Field("informationId") String informationId,
                                                         @Field("userId") String userID);


    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 历史订单订单统计
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/orders/history/stat")
    Observable<BaseResponse<HistoryTradeBean>> getHistoryTradeBean(
            @Field("dayStart") String dayStart,
            @Field("userId") String userId,
            @Field("token") String token,
            @Field("dayEnd") String dayEnd,
            @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 历史已撤单列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/orders/history/cancel/list")
    Observable<BaseResponse<List<HistoryMissPositionBean>>> getHistoryMissBean(
            @Field("dayStart") String dayStart,
            @Field("userId") String userId,
            @Field("token") String token,
            @Field("dayEnd") String dayEnd,
            @Field("page") int page,
            @Field("pageSize") int pageSize,
            @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 历史订单平仓列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/orders/history/close/list")
    Observable<BaseResponse<List<HistoryClosePositionBean>>> getHistoryCloseBean(
            @Field("dayStart") String dayStart,
            @Field("userId") String userId,
            @Field("token") String token,
            @Field("dayEnd") String dayEnd,
            @Field("page") int page,
            @Field("pageSize") int pageSize,
            @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 历史订单平仓列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/orders/history/open/list")
    Observable<BaseResponse<List<HistoryBuiderPositionBean>>> getHistoryBuilderBean(
            @Field("dayStart") String dayStart,
            @Field("userId") String userId,
            @Field("token") String token,
            @Field("dayEnd") String dayEnd,
            @Field("page") int page,
            @Field("pageSize") int pageSize,
            @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 平仓详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/orders/close/details")
    Observable<BaseResponse<List<CloseBuiderBean>>> getCloseBuiderBean(
            @Field("userId") String userId,
            @Field("id") String id,
            @Field("token") String token,
            @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 入金(充值)接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/transfer/recharge")
    Observable<BaseResponse<JsonNull>> getRechargeAsses(
            @Field("userId") String userId,
            @Field("brokerBranchId") String brokerBranchId,
            @Field("password") String password,
            @Field("bankId") String bankId,
            @Field("bankBranchId") String bankBranchId,
            @Field("bankAccount") String bankAccount,
            @Field("bankPassword") String bankPassword,
            @Field("amount") String amount,
            @Field("token") String token,
            @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 出金(提现)接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/transfer/cashout")
    Observable<BaseResponse<JsonNull>> getCashoutAsses(
            @Field("userId") String userId,
            @Field("brokerBranchId") String brokerBranchId,
            @Field("password") String password,
            @Field("bankId") String bankId,
            @Field("bankBranchId") String bankBranchId,
            @Field("bankAccount") String bankAccount,
            @Field("bankPassword") String bankPassword,
            @Field("amount") String amount,
            @Field("token") String token,
            @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 出入金明细(充值明细)接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/transfer/detail")
    Observable<BaseResponse<List<RechangeDetailData>>> getDetail(
            @Field("userId") String userId,
            @Field("page") int page,
            @Field("pageSize") int pageSize,
            @Field("token") String token,
            @Field("company") String company);

    /**
     * https://www.showdoc.cc/1673161?page_id=15438333
     * 银行卡余额查询接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-mobile-api/app/future/exchange/transfer/select/balance")
    Observable<BaseResponse<AssesData>> getBalanceAsses(
            @Field("userId") String userId,
            @Field("brokerBranchId") String brokerBranchId,
            @Field("password") String password,
            @Field("bankId") String bankId,
            @Field("bankBranchId") String bankBranchId,
            @Field("bankAccount") String bankAccount,
            @Field("bankPassword") String bankPassword,
            @Field("token") String token,
            @Field("company") String company);

    /** ********************************* 牛圈 ******************************************/

    /**
     * https://www.showdoc.cc/1673161?page_id=15679072
     * 加载个人首页接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circleFriend/loadHomepage.do")
    Observable<BaseResponse<CircleMineBean>> loadCircleHome(
            @Field("userId") String userId,
            @Field("loginUserId") String loginUserId,
            @Field("rows") String rows,
            @Field("rowsSize") String rowsSize);

    /**
     * https://www.showdoc.cc/1673161?page_id=15717824
     * 加载个人首页上半部分接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circleFriend/loadHomepageFirstPart.do")
    Observable<BaseResponse<CircleMineBean>> loadHomepageFirstPart(
            @Field("userId") String userId,
            @Field("loginUserId") String loginUserId);

    /**
     * https://www.showdoc.cc/1673161?page_id=15678695
     * 分圈接口
     *
     * @return
     */
    @GET("futures-communtiy-api/app/circle/circleType")
    Observable<BaseResponse<List<TopicFilter>>> getTopicFilter();

    /**
     * https://www.showdoc.cc/1673161?page_id=15678695
     * 分圈
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/list")
    Observable<BaseResponse<List<BBS>>> getCircleType(
            @Field("userId") String userId,
            @Field("circleTypeId") String type,
            @Field("circleTypeName") String circleTypeName,
            @Field("rows") int rows
    );


    /**
     * 收到的评论
     *
     * @param replyUserId
     * @param rows
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/BBSMessage/getCircleRevert.do")
    Observable<BaseResponse<List<CircleMsgBean>>> getCircleRevert(
            @Field("beReplyUserId") String replyUserId,
            @Field("rows") int rows);

    /**
     * 清空收到的评论
     *
     * @param replyUserId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/BBSMessage/getClearReply.do")
    Observable<BaseResponse<JsonNull>> getClearReply(
            @Field("beReplyUserId") String replyUserId);

    /**
     * 发出的评论
     *
     * @param replyUserId
     * @param rows
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/BBSMessage/getCircleCommentaries.do")
    Observable<BaseResponse<List<CircleMsgBean>>> getCircleCommentaries(
            @Field("beReplyUserId") String replyUserId,
            @Field("rows") int rows);


    /**
     * 清空 发出的评论
     *
     * @param replyUserId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/BBSMessage/getClearComments.do")
    Observable<BaseResponse<JsonNull>> getClearComments(
            @Field("beReplyUserId") String replyUserId);

    /**
     * 关注
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/focus")
    Observable<BaseResponse<JsonNull>> focus(
            @Field("postUserId") String postUserId,
            @Field("userId") String userId,
            @Field("attentionState") String type);

    /**
     * 点赞
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/praise")
    Observable<BaseResponse<JsonNull>> praise(
            @Field("postUserId") String postUserId,
            @Field("userId") String userId,
            @Field("praiseFlag") boolean praiseFlag,
            @Field("circleId") String circleId,
            @Field("nickName") String nickName);

    /**
     * 加载我的关注列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circleFriend/loadMyFocusList.do")
    Observable<BaseResponse<AttentionBean>> loadMyFocusList(
            @Field("userId") String userId,
            @Field("rows") String rows,
            @Field("rowsSize") String rowsSize);

    /**
     * 加载我的粉丝列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circleFriend/loadMyBeFocusList.do")
    Observable<BaseResponse<AttentionBean>> loadMyBeFocusList(
            @Field("userId") String userId,
            @Field("rows") String rows,
            @Field("rowsSize") String rowsSize);

    /**
     * 帖子详情-head 数据
     *
     * @param userId
     * @param circleId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/detail")
    Observable<BaseResponse<CircleDetailBean>> getClearDetailHead(
            @Field("userId") String userId,
            @Field("circleId") String circleId);

    /**
     * 发帖
     *
     * @param userId
     * @param circleId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/publish")
    Observable<BaseResponse<JsonNull>> push(
            @Field("postUserId") String userId,
            @Field("nickName") String nickName,
            @Field("content") String content,
            @Field("picOne") String picOne,
            @Field("picTwo") String picTwo,
            @Field("picThree") String picThree,
            @Field("picFour") String picFour,
            @Field("picFive") String picFive,
            @Field("picSix") String picSix,
            @Field("circleTypeId") String circleId);


    /**
     * 查看全部评论
     *
     * @param userId
     * @param circleId
     * @param postUserId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/cirleCommentList")
    Observable<BaseResponse<CommentAllBean>> getCirleCommentList(
            @Field("userId") String userId,
            @Field("circleId") String circleId,
            @Field("postUserId") String postUserId,
            @Field("rows") int rows,
            @Field("circleReplyId") String circleReplyId);

    /**
     * 只看楼主评论
     *
     * @param userId
     * @param circleId
     * @param postUserId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/cirleCommentAuth")
    Observable<BaseResponse<List<CommentBean>>> getCirleCommentAuth(
            @Field("userId") String userId,
            @Field("circleId") String circleId,
            @Field("postUserId") String postUserId,
            @Field("rows") int rows);

    /**
     * 评论/回复
     *
     * @param userId
     * @param circleId
     * @param content
     * @param beReplyUserId       被回复人id|
     * @param replyType           |int|类型 1 帖子回复 2 评论回复|
     * @param fatherCircleReplyId 是|int|主评论id 1.2.1及以上版本必传|
     * @param layCircleReplyId    没存在层级关系时与  fatherCircleReplyId 相同 反之传层级id
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/comment")
    Observable<BaseResponse<String>> getCommentContent(
            @Field("userId") String userId,
            @Field("circleId") String circleId,
            @Field("content") String content,
            @Field("beReplyUserId") String beReplyUserId,
            @Field("replyType") int replyType,
            @Field("replyNickName") String replyNickName,
            @Field("postUserId") String postUserId,
            @Field("fatherCircleReplyId") String fatherCircleReplyId,
            @Field("layCircleReplyId") String layCircleReplyId);

    /**
     * 点赞列表
     *
     * @param userId
     * @param circleId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/imageList")
    Observable<BaseResponse<List<com.honglu.future.ui.circle.praisesandreward.UserList>>> getPraiseImageList(
            @Field("userId") String userId,
            @Field("circleId") String circleId,
            @Field("rows") int rows);

    /**
     * 查询用户签到信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-signScore-api/signScore/getCusSignInfo")
    Observable<BaseResponse<SignBean>> getSignData(
            @Field("mobileNum") String mobile);

    /**
     * 保存签到积分接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-signScore-api/signScore/saveSingInfo")
    Observable<BaseResponse<JsonNull>> saveSignData(
            @Field("mobileNum") String mobile,
            @Field("userId") String userId,
            @Field("consDays") int consDays
    );


    /**
     * 打赏列表
     *
     * @param userId 当前用户id
     * @param postId 帖子id
     * @param rows   行数
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/scoreDetails/getScoreDetails.do")
    Observable<BaseResponse<List<ArewardListBean>>> getArewardList(
            @Field("userId") String userId,
            @Field("postId") String postId,
            @Field("rows") int rows);


    /**
     * 获取打赏积分
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/scoreDetails/getUserScore.do")
    Observable<BaseResponse<Integer>> getArewardScore(
            @Field("userId") String userId);


    /**
     * 打赏
     *
     * @param userId   当前登陆用户id
     * @param postId   帖子id
     * @param beUserId 被打赏用户id
     * @param score    牛币
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/scoreDetails/getReward.do")
    Observable<BaseResponse<JsonNull>> getReward(
            @Field("userId") String userId,
            @Field("postId") String postId,
            @Field("beUserId") String beUserId,
            @Field("score") int score,
            @Field("type") int type,
            @Field("exceptionalType") int exceptionalType);


    /**
     * 视频直播
     *
     * @return
     */
    @FormUrlEncoded
    @POST("futures-userlive-api/userLive/getLiveListInfo")
    Observable<BaseResponse<List<LiveListBean>>> getLiveData(
            @Field("userId") String userId);

    /**
     * 埋点
     * 开发环境 http://192.168.6.103:8000/apis/v1/dataprobe/
     * 生产地址 http://open.xiaoniu.com/apis/v1/dataprobe/
     * 测试环境 http://testopen.xnshandai.net/apis/v1/dataprobe/
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("http://open.xiaoniu.com/apis/v1/dataprobe/")
    Observable<MaidianReturn> postMaiDian(@Body RequestBody route);


    /**
     * circle 查看更多回复
     *
     * @param userId
     * @param fatherCircleReplyId 父评论id
     * @param rows
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/getLayComment")
    Observable<BaseResponse<List<CommentBosAllBean>>> getLayComment(
            @Field("userId") String userId,
            @Field("fatherCircleReplyId") String fatherCircleReplyId,
            @Field("rows") int rows);


    /**
     * 系统消息列表
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("http://192.168.90.139:8080/futures-messages-api/message/notice")
    Observable<BaseResponse<List<SystemMsgBean>>> getTradeMsgList(
            @Field("userId") String userId
            , @Field("messageType") int messageType, @Field("rows") int rows);


    /**
     * 点赞消息
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/getPraiseList")
    Observable<BaseResponse<List<PraiseMsgListBean>>> getPraiseList(
            @Field("userId") String userId
            , @Field("rows") int rows);


    /**
     * 清空赞列表
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("futures-communtiy-api/app/circle/getUpdateEmpty")
    Observable<BaseResponse<JsonNull>> getClearPraiseMsg(
            @Field("userId") String userId);

    /**
     * 未读消息红点提示
     *
     * @param userId
     * @return
     */
    @GET("futures-messages-api/message/hasUnreadMsg")
    Observable<BaseResponse<HasUnreadMsgBean>> getHasUnreadMsg(@Query("userId") String userId);

    /**
     * 消息红点显示逻辑
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("http://192.168.90.139:8080/futures-messages-api/message/index")
    Observable<BaseResponse<Boolean>> getMsgRed(
            @Field("userId") String userId);


    /**
     * 系统消息列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("http://192.168.90.139:8080/futures-messages-api/message/detail")
    Observable<BaseResponse<SystemMsgBean>> getSystemMsgDetial(@Field("messageId") String messageId);

    /**
     * 系统消息列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("http://192.168.90.139:8080/futures-messages-api/message/clear")
    Observable<BaseResponse<JsonNull>> getMsgClear(@Field("userId") String userId
            , @Field("messageType") int messageType);


}
