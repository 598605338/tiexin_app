package com.linjia.web.thirdService.bea.test;

import com.linjia.web.thirdService.bea.config.ElemeOpenConfig;
import com.linjia.web.thirdService.bea.config.RequestConstant;
import com.linjia.web.thirdService.bea.request.CancelOrderRequest;
import com.linjia.web.thirdService.bea.sign.OpenSignHelper;
import com.linjia.web.thirdService.bea.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 取消订单例子
 */
public class TestCancelOrder {
    private static final Log logger = LogFactory.getLog(TestCancelOrder.class);

    @Test
    public void testCancelOrder() throws IOException {
        String appId = ElemeOpenConfig.appId;
        String url = ElemeOpenConfig.API_URL;
        String partner_order_code = "1234567890jiaobuchong34";  //推单时 第三方订单号
        String token = "9ef6401c-0f9b-45d0-96e2-ee9a3a0d5657";

        CancelOrderRequest.CancelOrderRequstData data = new CancelOrderRequest.CancelOrderRequstData();
        data.setOrder_cancel_description("货品不新鲜");
        data.setOrder_cancel_reason_code(2);
        data.setPartner_order_code(partner_order_code);
        data.setOrder_cancel_time(new Date().getTime());

        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setData(data);

        int salt = RandomUtils.getInstance().generateValue(1000, 10000);

        Map<String, Object> sigStr = new LinkedHashMap<String, Object>();      // 注意添加的顺序, 应该如下面一样各个key值顺序一致
        sigStr.put("app_id", appId);
        sigStr.put("access_token", token);        // 需要使用前面请求生成的token
        sigStr.put("data", cancelOrderRequest.getData());
        sigStr.put("salt", salt);

        // 生成签名
        String sig = OpenSignHelper.generateBusinessSign(sigStr);
        cancelOrderRequest.setSignature(sig);

        cancelOrderRequest.setApp_id(appId);
        cancelOrderRequest.setSalt(salt);

        String requestJson = JsonUtils.getInstance().objectToJson(cancelOrderRequest);
        url = url + RequestConstant.orderCancel;
        try {
            String res = HttpClient.postBody(url, requestJson);
            logger.info(String.format("^_^, reponse data: %s", res));
        } catch (Exception e) {
            throw new HttpClientRuntimeException("取消订单出现异常", e);
        }
    }
}
