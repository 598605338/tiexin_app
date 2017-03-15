package com.linjia.web.thirdService.bea.test;

import com.linjia.web.thirdService.bea.config.ElemeOpenConfig;
import com.linjia.web.thirdService.bea.config.RequestConstant;
import com.linjia.web.thirdService.bea.request.ElemeQueryOrderRequest;
import com.linjia.web.thirdService.bea.sign.OpenSignHelper;
import com.linjia.web.thirdService.bea.util.*;
import org.junit.Test;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 订单查询例子
 */
public class TestQueryOrder {

    @Test
    public void testQueryOrder() throws Exception {
        String appId = ElemeOpenConfig.appId;
        String url = ElemeOpenConfig.API_URL;
        String partner_order_code = "16093021383";  //推单时 第三方订单号
        String token = "fa6ad8d1-0a35-4c93-b16a-91d82030169f";

        ElemeQueryOrderRequest request = new ElemeQueryOrderRequest();
        ElemeQueryOrderRequest.ElemeQueryRequestData data = new ElemeQueryOrderRequest.ElemeQueryRequestData();
        data.setPartner_order_code(partner_order_code);
        request.setData(data);

//        int salt = RandomUtils.getInstance().generateValue(1000, 10000);
        request.setApp_id(ElemeOpenConfig.appId);
        request.setSalt(1398);

        /**
         * 生成签名
         */
        Map<String, Object> sigStr = new LinkedHashMap<>();      // 注意添加的顺序, 应该如下面一样各个key值顺序一致
        sigStr.put("app_id", appId);
        sigStr.put("access_token", token);        // 需要使用前面请求生成的token
        sigStr.put("data", request.getData());
        sigStr.put("salt", 1398);
        // 生成签名
        String sig = OpenSignHelper.generateBusinessSign(sigStr);
        request.setSignature(sig);

        String requestJson = JsonUtils.getInstance().objectToJson(request);

        url = url + RequestConstant.orderQuery;
        try {
            HttpClient.postBody(url, requestJson);
        } catch (Exception e) {
            throw new HttpClientRuntimeException("查询订单出现异常", e);
        }
    }
}
