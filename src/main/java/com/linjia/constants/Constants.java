package com.linjia.constants;

import java.math.BigDecimal;


/** 
 * 业务常量类
 * 
 * @author  lixinling: 
 * @date 2016年7月1日 下午2:51:25 
 * @version 1.0 
 */
public class Constants {
	
	/**
	 * 系统资源根路径
	 */
	public final static BigDecimal LIMIT_SEND_AMOUNT = new BigDecimal("1.00");

	/**
	 * 状态(0失败，1成功)
	 * 
	 * @author lxl
	 * 
	 */
	public interface STATUS{
		/** 失败*/
        int FAIL = 0;
		/** 成功*/
        int SUCCESS = 1;
	}
	
	/**
	 * 商品类型
	 * 
	 * @author lxl
	 * 
	 */
	public interface PRO_TYPE{
		/** 现实商品*/
        int XSSP = 0;
		/** 虚拟商品*/
        int XJZF = 1;
	}
	
	/**
	 * 秒杀时间点
	 * 
	 * @author lxl
	 * 
	 */
	public interface MIAOSHA_TIME{
		/** 9点*/
        int NINE = 9;
		/** 12点*/
        int TWELVE = 12;
		/** 15点*/
        int FIFTEEN = 15;
		/** 18点*/
        int EIGHTEEN = 18;
	}
	
	/**
	 * 秒杀抢购状态:抢购中；即将开始；已结束
	 * 
	 * @author lxl
	 * 
	 */
	public interface MIAOSHA_PANIC_STATUS{
		/** 即将开始*/
        String UNSTART = "即将开始";
		/** 抢购中*/
        String PANICING = "抢购中";
		/** 15点*/
        String ENDED = "已结束";
	}
	
	/**
	 * 显示信息1:即将开始；抢购已开始；抢购结束，敬请期待；
	 * 
	 * @author lxl
	 * 
	 */
	public interface MIAOSHA_SHOW_LABEL1{
		/** 即将开始*/
        String UNSTART = "即将开始";
		/** 抢购已开始 */
        String PANICING = "抢购已开始";
		/** 抢购结束，敬请期待 */
        String ENDED = "抢购结束，敬请期待";
	}
	
	/**
	 * 显示信息2:距离开始；距离结束；
	 * 
	 * @author lxl
	 * 
	 */
	public interface MIAOSHA_SHOW_LABEL2{
		/** 距离开始 */
        String UNSTART = "距离开始";
		/** 距离结束 */
        String PANICING = "距离结束";
	}
	
	/**
	 * 配送方式
	 * 
	 * @author lxl
	 * 
	 */
	public interface SEND_TYPE{
		/** 送货上门 */
        int SHSM = 0;
		/** 上门自提 */
        int SMZT = 1;
	}
	
	/**
	 * 是否符合订单满减
	 * 
	 * @author lxl
	 * 
	 */
	public interface FULL_SUBTRACT{
		/** 否 */
        boolean NO = false;
		/** 是 */
        boolean YES = true;
	}
	
	/**
	 * 库存状态(0:正常；1：库存不足)
	 * 
	 * @author lxl
	 * 
	 */
	public interface QTY_STATUS{
		/** 0:正常 */
        int NORMAL = 0;
		/** 1：库存不足 */
        int LOW = 1;
	}
	
	/**
	 * 支付方式(0:微信支付；1：钱包支付;2：纯积分支付)
	 * 
	 * @author lxl
	 * 
	 */
	public interface PAY_TYPE{
		/** 0:微信支付 */
        int WX = 0;
		/** 1：钱包支付 */
        int PURSE = 1;
		/** 2：纯积分支付 */
        int ONLYSCORE = 2;
	}
	
	/**
	 * 支付状态(0未支付；1已支付)
	 * 
	 * @author lxl
	 * 
	 */
	public interface PAY_STATUS{
		/** 0:未付款 */
        int UNPAY = 0;
		/** 1：已付款 */
        int PAYD = 1;
	}
	
	/**
	 * 订单总状态(0未付款；1已付款；2商家已确认；3商家已取消；4派送中；5已完成；6客户取消订单；7,已接单;8：超时取消订单)
	 * 
	 * @author lxl
	 * 
	 */
	public interface ORDER_GROUP_STATUS{
		/** 0:未付款 */
        int UNPAY = 0;
		/** 1：已付款 */
        int PAYD = 1;
		/** 2：商家已确认 */
        int CONFIRM = 2;
		/** 3：商家已取消*/
        int BUSSINESS_CANCELE = 3;
		/** 4：派送中 */
        int SENDING = 4;
		/** 5：已完成 */
        int SUCCESS = 5;
		/** 6：客户取消订单 */
        int CUSTOMER_CANCELE = 6;
		/** 7：已接单 */
        int RECEIVED = 7;
		/** 8：超时取消订单 */
        int TIMEOUT_CANCELE = 8;
		/** 9：客服操作退款 */
        int KFCZ_REFUND = 9;
		/** 10：物流取消待重新发单 */
        int WLQXCXFD = 10;
	}
	
	/**
	 * 在线支付退款状态(0、无退款 1、等待退款 2、正在退款 3、已退款 4、退款失败 5、未确定 6、转入代发)
	 * 
	 * @author lxl
	 * 
	 */
	public interface ORDER_REFUND_STATUS{
		/** 0:无退款  */
        int NO_REFUND = 0;
		/** 1：等待退款 */
        int WAIT_REFUND = 1;
		/** 2：正在退款 */
        int REFUNDDING = 2;
		/** 3：已退款 */
        int REFUNDED = 3;
		/** 4：退款失败 */
        int FAIL_REFUND = 4;
		/** 5：未确定 */
        int UNCONFIRM = 5;
		/** 6：转入代发 */
        int ZRDF = 6;
	}
	
	/**
	 * 手机号注册会员状态（0:注册失败；1注册成功）
	 * 
	 * @author lxl
	 * 
	 */
	public interface REGIST_PHONE_STATUS{
		/** 注册失败*/
        int FAIL = 0;
		/** 注册成功*/
        int SUCCESS = 1;
	}
	
	/**
	 * 积分的获取和使用途径
	 * 消费("101"), 储值("102"), 加速("103"), 调整("104"), 转出("105"), 转入("106"), 兑奖("107"), 抵扣("108"), 抽奖("109"), 
	 * 登录("201"), 评价("202"), 清除("203"), 开卡("204"), 注册("205"), 便民("206"), 储值转积分("207"), 赠送("208"), 积分转储值("209");
	 * @author lxl
	 * 
	 */
	public interface SCORE_WAY{
		/** 消费*/
        int SPENDING = 101;
		/** 储值*/
        int STOREVALUE = 102;
		/** 加速*/
        int SPEED_UP = 103;
		/** 调整*/
        int ADJUST = 104;
		/** 转出*/
        int TURN_OUT = 105;
		/** 转入*/
        int TURN_IN = 106;
		/** 兑奖*/
        int CASH_PRIZE = 107;
		/** 抵扣*/
        int DEDUCT = 108;
		/** 抽奖*/
        int RAFFLE = 109;
		/** 登录*/
        int LOGIN = 201;
		/** 评价*/
        int RATED = 202;
		/** 清除*/
        int CLEAR = 203;
		/** 开卡*/
        int OPEN_CARD = 204;
		/** 注册*/
        int REGISTE = 205;
		/** 便民*/
        int CONVENIENCE = 206;
		/** 储值转积分*/
        int STORECONVERSCORE = 207;
		/** 赠送*/
        int GIFTS = 208;
		/** 积分转储值*/
        int SCOREVONVERSTORE = 209;
	}
	/**
	 * 团购状态描述
	 * 
	 * @author xiangsy
	 * 
	 */
	public interface PINTUAN_SHOW_LABEL{
		//组团中
        String PINTUANING = "组团中";
		//已成团
        String  PINTUANOVER= "已成团";
		//组团失败
        String PINTUANFALI = "组团失败";

		
	}
	
	/**
	 * 团购状态描述
	 * 
	 * @author xiangsy
	 * 
	 */
	public interface PINTUAN_SHOW_VAL{
		//组团中
        int PINTUANINGVAL = 0;
		//已成团
        int  PINTUANOVERVAL= 1;
		//组团失败
        int PINTUANFALIVAL = 2;
		
	}
	
	/**
	 * 团购有效时间
	 * 
	 * @author xiangsy
	 * 
	 */
	public interface PINTUAN_TIME_LIMIT{
		//拼团有效时间(小时)
        int TIMELIMITNUM = 10;
		
	}
	
	/**
	 * 返回结果状态字符窜
	 * 
	 * @author xiangsy
	 * 
	 */
	public interface RESULT_TEXT{
		String OK = "ok";
		String FAIL = "fail";
	}
	
	
	/**
	 * 积分订单状态(0未支付；1已完成)
	 * 
	 * @author lxl
	 * 
	 */
	public interface SCORE_STATUS{
		/** 未支付*/
        int UNPAY = 0;
		/** 已完成*/
        int SUCCESS = 1;
	}
	
	/**
	 * 卡券使用状态:0未使用；1已使用
	 * 
	 * @author lxl
	 * 
	 */
	public interface CARD_USED_STATUS{
		/** 0未使用*/
        int UNUSED = 0;
		/** 1已使用*/
        int USED = 1;
		/** 2客服已核销(废弃)*/
        int KEFU_VERIFICATIONED = 2;
	}
	
	/**
	 * 是否线上使用：0线上；1线下
	 * 
	 * @author lxl
	 * 
	 */
	public interface CARD_USED_PLACE{
		/** 0线上*/
        int ONLINE = 0;
		/** 1线下*/
        int OFFLINE = 1;
	}
	
	/**
	 * 商品发货类型：0及时送；1预约配送
	 * 
	 * @author lxl
	 * 
	 */
	public interface PRODUCT_SEND_TYPE{
		/** 0及时送*/
        int IMMEDIATE = 0;
		/** 1次日达*/
        int PREPARE = 1;
	}
	
	/**
	 * 订单配送方式：0立即配送；1预约配送；2立即配送+预约配送
	 * 
	 * @author lxl
	 * 
	 */
	public interface ORDER_SEND_TYPE{
		/** 0立即配送*/
        int IMMEDIATE_SEND = 0;
		/** 1预约配送*/
        int PREPARE_SEND = 1;
		/** 2立即配送+预约配送*/
        int DOUBLE = 2;
	}
	
	/**
	 * 订单生成 返回 CODE
	 * 
	 * @author lxl
	 * 
	 */
	public interface ORDER_RETURN_CODE{
		/** 10000 全部补货中*/
        int PRODUCT_ALL_LOW_STOCK = 10000;
		/** 10001 部分库存不足*/
        int PRODUCT_LOW_STOCK = 10001;
		/** 10002订单金额小于起送金额*/
        int LOW_LIMIT_AMOUNT = 10002;
		/** 10003商品券对应的商品库存不足,请稍后再试*/
        int LOW_CARD_COUPON_PRODUCT = 10003;
		/** 10004传入的优惠券不存在*/
        int NO_CARD_COUPON = 10004;
		/** 10005该商品券对应的商品不存在,请稍后再试*/
        int NO_CARD_COUPON_PRODUCT = 10005;
		/** 10006该门店处于休息状态，暂不支持下单*/
        int MALL_CLOSED = 10006;
	}
	

	/**
	 * 退款方式: 1、人工退款 2、自动退款
	 * 
	 * @author lxl
	 * 
	 */
	public interface REFUND_TYPE{
		/** 1、人工退款*/
        int ARTIFICIAL = 1;
		/** 2、自动退款*/
        int AUTO = 2;
	}
	
	/**
	 * 退款来源:0取消订单；1手工退款
	 * 
	 * @author lxl
	 * 
	 */
	public interface REFUND_SOURCE{
		/** 0取消订单*/
        int CANCEL = 0;
		/** 1手工退款*/
        int ARTIFICIAL = 1;
	}
	
	/**
	 * 审核状态：1.未审核 2.已审核 3.取消
	 * 
	 * @author lxl
	 * 
	 */
	public interface REFUND_STATUS{
		/** 1.未审核*/
        int UNREVIEW = 1;
		/** 2.已审核*/
        int REVIEWED = 2;
		/** 3.取消*/
        int CANCEL = 3;
	}
	
	public interface PAGE{
		/** 每页条数*/
        int SIZE = 5;
	}
	
	/**
	 * 来源类型：0领券中心；1积分兑换
	 * 
	 * @author lxl
	 * 
	 */
	public interface SCORE_SOURCE_TYPE{
		/** 0.领券中心*/
        int LQZX = 0;
		/** 1.积分兑换*/
        int JFDH = 1;
		/** 2.会员活动*/
        int HYHD = 2;
	}
	
	/**
	 * 尾货清仓：活动状态:1、销售中；2、已售罄；3、已结束 (在列表中将已售罄商品显示顺序调到最后)
	 * 
	 * @author lxl
	 * 
	 */
	public interface TAIL_GOODS_CLEAR_ACTIVITY_STATUS{
		/** 1、销售中*/
        int SELLING = 1;
		/** 2、已售罄*/
        int SELLOUT = 2;
		/** 3、已结束*/
        int SELLEND = 3;
		/** 4、等待开始*/
        int WAITTING = 4;
	}
	
	/**
	 * 状态(0失败，1成功)
	 * 
	 * @author lxl
	 * 
	 */
	public interface TRAN_STATUS{
		/** 0未完成*/
        int UNSUCCESS = 0;
		/** 1已完成*/
        int SUCCESSED = 1;
	}
	
	/**
	 * 状态(0失败，1成功)
	 * 
	 * @author lxl
	 * 
	 */
	public interface SHARE_PAGE{
		/** 0未完成*/
        String INVITEURL = "index.html";
	}
	
	/**
	 * 钱包操作动作(1充值；2消费；3退款)
	 * 
	 * @author lxl
	 * 
	 */
	public interface ACCOUNTCASH_ACTION{
		/** 1充值*/
        int DEPOSIT_ACCOUNT = 1;
		/** 2消费*/
        int SPEND_ACCOUNT = 2;
		/** 3退款*/
        int REFUND_ACCOUNT = 3;
	}

	/**
	 * 拼团运费
	 * 
	 * @author xsy
	 * 
	 */
	public interface PIN_TUAN{
		/** 运费*/
        String SEND_FEE = "5";
		Integer QUANTITY = 1;
	}
	
	/**
	 * 卡券类型：3.代金券 1.商品券 5.免运费券
	 * 
	 * @author lxl
	 * 
	 */
	public interface CARD_COUPON_TYPE{
		/** 3.代金券*/
        int DJQ = 3;
		/** 1.商品券*/
        int SPQ = 1;
		/** 5.免运费券*/
        int MYFQ = 5;
		/** 6.链接券*/
        int THIRD_LJQ = 6;
		/** 7.券码*/
        int THIRD_QM = 7;
	}
	
	
	public interface THIRD_CALLBACK_URL{
		/** 达达回调接口url**/
        String DADACALL="http://www.linjia-cvs.cn/linjia_1/forOut/dadaPushData";
		//测试url
//		final static String DADACALL="http://b2c.linjia-cvs.cn/linjia_1/forOut/dadaPushData";
	}
	

	/**
	 * 活动类型(1,满减；2，商品折扣；3第二件半价；4，加钱换购；5，免运费；6，会员促销)
	 * 
	 * @author lxl
	 * 
	 */
	public interface ACTIVITY_TYPE{
		/** 1,满减*/
        int FULL_REDUCE = 1;
		/** 2，商品折扣*/
        int PRO_DISCOUNT = 2;
		/** 3，第二件半价*/
        int SEC_PRO_DISCOUNT = 3;
		/** 4，加钱换购*/
        int TRADE_PRODUCT = 4;
		/** 5，免运费*/
        int FREE_FREIGHT = 5;
		/** 6，会员促销*/
        int MEMBER_PROMOTION = 6;
		/** 7，秒杀促销*/
        int MIAOSHA = 7;
		/** 8，尾货清仓*/
        int TAIL_GOODS_CLEAR = 8;
	}
	
	/**
	 * 异常短信接收人
	 * 
	 * @author lxl
	 * 
	 */
	public interface ERROR_WARN_CONFIG{
	
		String	ERROR_RECIEVE_PHONE="13681142818";
	}
}
