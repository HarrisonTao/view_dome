package com.dykj.youfeng.network.mall_api;

import com.dykj.youfeng.mode.AddressBean;
import com.dykj.youfeng.mode.CancelReasonBean;
import com.dykj.youfeng.mode.CarListBean;
import com.dykj.youfeng.mode.CollectListBean;
import com.dykj.youfeng.mode.EvaInfoBean;
import com.dykj.youfeng.mode.EvaListBean;
import com.dykj.youfeng.mode.ExpBean;
import com.dykj.youfeng.mode.ExpInfoBean;
import com.dykj.youfeng.mode.FreightBean;
import com.dykj.youfeng.mode.GoodsCateBean;
import com.dykj.youfeng.mode.GoodsEvaBean;
import com.dykj.youfeng.mode.GoodsInfoBean;
import com.dykj.youfeng.mode.GoodsListBean;
import com.dykj.youfeng.mode.HasEvaListBean;
import com.dykj.youfeng.mode.ImageInfoBean;
import com.dykj.youfeng.mode.MallHomeBean;
import com.dykj.youfeng.mode.MallUserInfo;
import com.dykj.youfeng.mode.OrderInfoBean;
import com.dykj.youfeng.mode.OrderListBean;
import com.dykj.youfeng.mode.PayWxBean;
import com.dykj.youfeng.mode.RefundInfoBean;
import com.dykj.youfeng.mode.RefundListBean;
import com.dykj.youfeng.mode.RefundReasonBean;
import com.dykj.youfeng.mode.SearchLogBean;
import com.dykj.youfeng.mode.SpecResultBean;
import com.dykj.youfeng.mode.SubmitOrderBean;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.view.bean.AreaBean;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lcjingon 2019/11/4.
 * description:
 */

public interface MallApi {
    /**
     * 自营首页信息
     */
    @POST("Sindex/index")
    Observable<HttpResponseData<MallHomeBean>> homeIndex();

    /**
     * 自营首页分类信息
     */
    @POST("Goods/category")
    Observable<HttpResponseData<GoodsCateBean>> homeGoodsCate();

    /**
     * 自营商品搜索
     * keyword	是	string	关键词
     * type	是	string	max：综合     finish：销量         price：价格
     * sort	是	string	desc：从大到小    asc：从小到大
     */
    @FormUrlEncoded
    @POST("Sindex/search_info")
    Observable<HttpResponseData<GoodsListBean>> searchInfo(@Field("token") String token, @Field("keyword") String keyword, @Field("type") String type
            , @Field("sort") String sort, @Field("page") int page);


    /**
     * 自营商品热搜
     * keyword	是	string	关键词
     * type	是	string	max：综合     finish：销量         price：价格
     * sort	是	string	desc：从大到小    asc：从小到大
     */
    @FormUrlEncoded
    @POST("Sindex/search_log")
    Observable<HttpResponseData<List<SearchLogBean>>> searchLog(@Field("token") String token, @Field("type") String type);


    /**
     * 自营商品搜索
     * keyword	是	string	关键词
     * type	是	string	max：综合     finish：销量         price：价格
     * sort	是	string	desc：从大到小    asc：从小到大
     */
    @FormUrlEncoded
    @POST("Sindex/category_goods_list")
    Observable<HttpResponseData<GoodsListBean>> categoryGoodsList(@Field("token") String token, @Field("gc_id") String gc_id, @Field("type") String type
            , @Field("sort") String sort, @Field("page") int page);

    /**
     * 商品详情
     *
     * @param token
     * @param goodsId
     * @return
     */
    @FormUrlEncoded
    @POST("Goods/goods_info")
    Observable<HttpResponseData<GoodsInfoBean>> goodsInfo(@Field("token") String token, @Field("goods_id") String goodsId);


    /**
     * 商品详情
     *
     * @param token
     * @param goodsId
     * @return
     */
    @FormUrlEncoded
    @POST("Goods/goods_info")
    Observable<HttpResponseData<JsonObject>> goodsInfoObj(@Field("token") String token, @Field("goods_id") String goodsId);


    /**
     * 规格选中价格库存信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("Goods/get_goods_spec")
    Observable<HttpResponseData<SpecResultBean>> getGoodsSpec(@Field("id") String id, @Field("goods_id") String goodsId);

    @FormUrlEncoded
    @POST("Goods/addcar")
    Observable<HttpResponseData> addCar(@FieldMap Map<String, String> params);

    /**
     * @param token
     * @param goodsId     商品id 多个用,拼接
     * @param collectType //1-收藏 0-取消收藏
     * @return
     */
    @FormUrlEncoded
    @POST("Goods/My_collect_goods")
    Observable<HttpResponseData> collectGood(@Field("token") String token, @Field("goods_id") String goodsId, @Field("collect_type") String collectType);


    @FormUrlEncoded
    @POST("Buy/order_info")
    Observable<HttpResponseData<JsonObject>> orderInfo(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("Buy/gobuy_dosubmit")
    Observable<HttpResponseData<SubmitOrderBean>> submitOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("ConfirmOrder/order_info")
    Observable<HttpResponseData<List<JsonObject>>> orderInfoCar(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("ConfirmOrder/gobuy_dosubmit")
    Observable<HttpResponseData<SubmitOrderBean>> submitOrderCar(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Pay/gopay")
    Observable<HttpResponseData> payGoods(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Pay/gopay")
    Observable<HttpResponseData<String>> payGoodsAli(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Pay/gopay")
    Observable<HttpResponseData<PayWxBean>> payGoodsWx(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("Goods/all_evaluate_goods")
    Observable<HttpResponseData<GoodsEvaBean>> allGoodsEva(@Field("token") String token, @Field("goods_id") String goodsId, @Field("page") int page);


    @FormUrlEncoded
    @POST("Buy/getFreight")
    Observable<HttpResponseData<FreightBean>> getFreight(@Field("address_id") String addressId, @Field("goods_num") int goodsNum, @Field("goods_id") String goodsId, @Field("goods_spec") String spec);

    @FormUrlEncoded
    @POST("Buy/getFreight")
    Observable<HttpResponseData<FreightBean>> getFreight(@Field("address_id") String addressId, @Field("goods_num") String goodsNum, @Field("goods_id") String goodsId, @Field("carid") String carid);

    @FormUrlEncoded
    @POST("Buy/getFreight")
    Observable<HttpResponseData<FreightBean>> getFreight(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("Goods/carlist")
    Observable<HttpResponseData<CarListBean>> carList(@Field("token") String token, @Field("page") int page);


    @FormUrlEncoded
    @POST("Goods/changecar")
    Observable<HttpResponseData> changecarNum(@Field("token") String token, @Field("carid") String carid, @Field("num") int num);

    @FormUrlEncoded
    @POST("Goods/delcar")
    Observable<HttpResponseData> delCar(@Field("token") String token, @Field("cardid") String carid);


    /**
     * 商城 用户信息
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("Suser/index")
    Observable<HttpResponseData<MallUserInfo>> userIndex(@Field("token") String token);

    /**
     * 收藏 的商品列表
     *
     * @param token
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("MyCollect/index")
    Observable<HttpResponseData<CollectListBean>> collectIndex(@Field("token") String token, @Field("page") int page);

    @FormUrlEncoded
    @POST("MyCollect/del")
    Observable<HttpResponseData> delCollect(@Field("token") String token, @Field("id") String id);

    @FormUrlEncoded
    @POST("memberAddress/area")
    Observable<HttpResponseData<List<AreaBean>>> getArea(@Field("pid") String pId);

    //    true_name	string	名字
//    tel_phone	string	手机号
//    province_id	string	省份ID
//    city_id	string	城市ID
//    area_id	string	区ID
//    area_info	string	详细地址
//    is_default	string	是否默认地址
    @FormUrlEncoded
    @POST("MemberAddress/add_address")
    Observable<HttpResponseData> addArea(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("MemberAddress/edit_address")
    Observable<HttpResponseData> editArea(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("MemberAddress/del")
    Observable<HttpResponseData> delArea(@Field("token") String token, @Field("address_id") String addressId);

    @FormUrlEncoded
    @POST("MemberAddress/index")
    Observable<HttpResponseData<List<AddressBean>>> getAreaList(@Field("token") String token, @Field("page") int page);


    //订单列表
    @FormUrlEncoded
    @POST("MemberOrder/index")
    Observable<HttpResponseData<OrderListBean>> getOrderList(@Field("token") String token, @Field("order_state") String orderState, @Field("page") int page);


    //订单取消原因
    @FormUrlEncoded
    @POST("MemberOrder/cancel_reason")
    Observable<HttpResponseData<CancelReasonBean>> getCancelReason(@Field("token") String token);

    //取消订单
    @FormUrlEncoded
    @POST("MemberOrder/cancel")
    Observable<HttpResponseData> getCancel(@Field("token") String token, @Field("order_sn") String orderSn, @Field("cancle_reason") String cancleReason);

    //删除订单
    @FormUrlEncoded
    @POST("MemberOrder/del_order")
    Observable<HttpResponseData> deleteOrder(@Field("token") String token, @Field("order_id") String orderId);

    //订单详情
    @FormUrlEncoded
    @POST("MemberOrder/order_info")
    Observable<HttpResponseData<OrderInfoBean>> orderInfo(@Field("token") String token, @Field("order_id") String orderId);

    //确认收货
    @FormUrlEncoded
    @POST("MemberOrder/doconfirm")
    Observable<HttpResponseData> confirmReceive(@Field("token") String token, @Field("order_id") String orderId);


    //待支付 立即支付
    @FormUrlEncoded
    @POST("Carpay/gopay")
    Observable<HttpResponseData> payOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Carpay/gopay")
    Observable<HttpResponseData<String>> payOrderAli(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Carpay/gopay")
    Observable<HttpResponseData<PayWxBean>> payOrderWx(@FieldMap Map<String, String> params);


    //退款原因
    @POST("MemberOrder/refund_reason")
    Observable<HttpResponseData<List<RefundReasonBean>>> refundReason();

    //申请退款
    @FormUrlEncoded
    @POST("MemberOrder/returns")
    Observable<HttpResponseData> applyReturns(@FieldMap Map<String, String> params);

    //退款订单列表
    @FormUrlEncoded
    @POST("MemberOrder/after_sale_b")
    Observable<HttpResponseData<RefundListBean>> refundOrderList(@Field("token") String token, @Field("page") int page);

    //退款订单详情
    @FormUrlEncoded
    @POST("MemberOrder/refund_order")
    Observable<HttpResponseData<RefundInfoBean>> refundOrderInfo(@FieldMap Map<String, String> params);

    //    申请退款，同意后进行退款退货
    @FormUrlEncoded
    @POST("MemberOrder/returns_ok")
    Observable<HttpResponseData> returnsOk(@FieldMap Map<String, String> params);

    @POST("MemberOrder/images")
    Observable<HttpResponseData<ImageInfoBean>> upImg(@Body RequestBody body);


    //物流列表
    @POST("MemberOrder/express")
    Observable<HttpResponseData<List<ExpBean>>> getExpress();


    @FormUrlEncoded
    @POST("Evaluate/index")
    Observable<HttpResponseData<EvaListBean>> unEvaluateIndex(@Field("token") String token, @Field("type") String type, @Field("page") int page);

    @FormUrlEncoded
    @POST("Evaluate/index")
    Observable<HttpResponseData<HasEvaListBean>> evaluateIndex(@Field("token") String token, @Field("type") String type, @Field("page") int page);

    @FormUrlEncoded
    @POST("MemberOrder/evaluation")
    Observable<HttpResponseData<List<EvaInfoBean>>> evaluation(@Field("token") String token, @Field("order_id") String orderId);

    @FormUrlEncoded
    @POST("MemberOrder/do_evaluation")
    Observable<HttpResponseData> doEvaluation(@Field("token") String token, @Field("order_id") String orderId, @Field("str") String str);


    @FormUrlEncoded
    @POST("memberOrder/getExpress")
    Observable<HttpResponseData<ExpInfoBean>> getExpress(@Field("token") String token, @Field("order_id") String orderId);

}
