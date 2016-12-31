package com.welfare.common.api.weixin.service;

import com.welfare.common.api.weixin.common.Configure;
import com.welfare.common.api.weixin.protocol.pay_protocol.UnifiedOrderReqData;

public class UnifiedOrderService extends BaseService {

    public UnifiedOrderService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.UNIFIED_ORDER);
    }

    /**
     * 请求支付服务
     *
     * @param unifiedOrderReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public String request(UnifiedOrderReqData unifiedOrderReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(unifiedOrderReqData);

        return responseString;
    }
}
