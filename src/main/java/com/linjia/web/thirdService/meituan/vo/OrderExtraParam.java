package com.linjia.web.thirdService.meituan.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by zhangzhidong on 15/10/29.
 */
public class OrderExtraParam {
	//活动id
	private String act_detail_id;
	
	//餐厅平均送餐时间，单位为分钟
	private String reduce_fee;
	
	@JsonIgnore
	private BigDecimal mt_charge;
	
	@JsonIgnore
	private BigDecimal poi_charge;
	
	//优惠说明
	private String remark;
	
	//活动类型
	private String type;
	
	//餐厅平均送餐时间，单位为分钟
	private String avg_send_time;
	
	private Long order_id;

    public String getAct_detail_id() {
        return act_detail_id;
    }

    public void setAct_detail_id(String act_detail_id) {
        this.act_detail_id = act_detail_id;
    }

    public String getAvg_send_time() {
        return avg_send_time;
    }

    public void setAvg_send_time(String avg_send_time) {
        this.avg_send_time = avg_send_time;
    }

    public String getReduce_fee() {
        return reduce_fee;
    }

    public void setReduce_fee(String reduce_fee) {
        this.reduce_fee = reduce_fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public BigDecimal getMt_charge() {
		return mt_charge;
	}

	public void setMt_charge(BigDecimal mt_charge) {
		this.mt_charge = mt_charge;
	}

	public BigDecimal getPoi_charge() {
		return poi_charge;
	}

	public void setPoi_charge(BigDecimal poi_charge) {
		this.poi_charge = poi_charge;
	}
    
}

