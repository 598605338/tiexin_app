package com.linjia.web.thirdService.meituan.constants;

/**
 * Created by zhangzhidong on 15/11/10.
 */
public enum PoiQualificationEnum {

    BUSINESS_LICENSE(1, "营业执照"),
    CATERING_SERVICE_LICENSE(2, "餐饮服务许可证"),
    HEALTH_CERTIFICATE(3, "健康证"),
    CORPORATE_IDENTITY(4, "法人身份证");

    int type;
    String description;
    PoiQualificationEnum(int type, String description){
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "type:" + type + ", " + "description:" + description;
    }


}