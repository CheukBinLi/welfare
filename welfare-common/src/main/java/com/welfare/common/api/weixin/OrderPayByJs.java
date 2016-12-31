package com.welfare.common.api.weixin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.welfare.common.api.weixin.common.RandomStringGenerator;
import com.welfare.common.api.weixin.common.Signature;
import com.welfare.common.api.weixin.common.XMLParser;
import com.welfare.common.api.weixin.protocol.pay_protocol.UnifiedOrderReqData;


/**
 * JS(微信公众号)方式支付订单
 */
public class OrderPayByJs {
	private final Logger logger = LoggerFactory.getLogger(OrderPayByJs.class);

	/**
	 * 微信公众号JS支付,获取订单支付的签名数据
	 */
	public HashMap getOrderPaySign(String appId, String mchId, String apiKey, String body, String attach, String orderId, double amount, String spBillCreateIP, String timeStart, String timeExpire, String notifyUrl, String limitPay, String weixinOpenId) throws Exception {
		HashMap hashMap = new HashMap();

		UnifiedOrderReqData unifiedOrderReqData = new UnifiedOrderReqData(appId, mchId, apiKey, body, attach, orderId, (int) (amount * 100), spBillCreateIP, timeStart, timeExpire, notifyUrl, limitPay, weixinOpenId);
		if (logger.isDebugEnabled())
			logger.debug("传入参数:" + appId + "      " + mchId + "      " + apiKey + "      " + body + "      " + attach + "      " + orderId + "      " + (int) (amount * 100) + "      " + spBillCreateIP + "      " + timeStart + "      " + timeExpire + "      " + notifyUrl + "      " + limitPay + "      " + weixinOpenId);
		String unifiedOrderResponse = WXPay.unifiedOrder(unifiedOrderReqData);
		if (logger.isDebugEnabled())
			logger.debug("unifiedOrderResponse: " + unifiedOrderResponse);
		// if
		// (Signature.checkIsSignValidFromResponseString(unifiedOrderResponse,
		// apiKey)) {
		Map unifiedOrderResponseMap = XMLParser.getMapFromXML(unifiedOrderResponse);
		String prepay_id = (String) unifiedOrderResponseMap.get("prepay_id");
		// if (StringUtils.isNotBlank(prepay_id)) {
		String nonceStr = RandomStringGenerator.getRandomStringByLength(32);
		String timeStamp = String.valueOf((System.currentTimeMillis() / 1000));
		String packageValue = "prepay_id=" + prepay_id;

		SortedMap<String, Object> signParams = new TreeMap();
		signParams.put("appId", appId);
		signParams.put("timeStamp", timeStamp);
		signParams.put("nonceStr", nonceStr);
		signParams.put("package", packageValue);
		signParams.put("signType", "MD5");

		String paySign = Signature.getSign(signParams, apiKey);

		hashMap.put("appId", appId);
		hashMap.put("timeStamp", timeStamp);
		hashMap.put("nonceStr", nonceStr);
		hashMap.put("package", packageValue);
		hashMap.put("paySign", paySign);
		hashMap.put("signType", "MD5");
		// } else {
		// logger.error("prepay_id值为空");
		// }
		// } else {
		// logger.error("微信签名校验失败");
		// }

		if (logger.isDebugEnabled())
			logger.debug(hashMap.toString());
		return hashMap;
	}

	public static void main(String[] agrs) {
		OrderPayByJs orderPayByJs = new OrderPayByJs();
		try {
			HashMap hashMap = orderPayByJs.getOrderPaySign("wx33bb40388bcc0a16", "1245667902", "beAZc6Bp3UobCaxOMegEzQoCSmt10nuK", "特殊", "1", "WWDDD123", new BigDecimal(1).doubleValue(), "127.0.0.1", "20160920082359", "20160920105059", "test.weimain.com", "34", "oRKlLt0nzuUuSLEzPKzBtiOe9TH8");
			String b = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
